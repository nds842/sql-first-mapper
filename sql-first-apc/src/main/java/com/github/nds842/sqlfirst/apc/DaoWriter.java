package com.github.nds842.sqlfirst.apc;

import com.github.nds842.sqlfirst.base.DaoDesc;
import com.github.nds842.sqlfirst.base.MiscUtils;
import com.github.nds842.sqlfirst.base.QueryDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builder class for Dao classes - writes
 */
public class DaoWriter {
    
    private static final String TARGET_GENERATED_SOURCES_ANNOTATIONS = "/target/generated-sources/annotations/";
    /**
     * Reference to ProcessingEnvironment to write files and report errors
     */
    private final ProcessingEnvironment processingEnv;
    private final Element globalConfigElement;
    
    public DaoWriter(ProcessingEnvironment processingEnv, Element globalConfigElement) {
        this.processingEnv = processingEnv;
        this.globalConfigElement = globalConfigElement;
        initVelocity();
    }

    public void write(
            List<DaoDesc> daoDescList,
            SqlFirstApcConfig sqlFirstApcConfig
    ) {
        String baseDtoClassName = sqlFirstApcConfig.baseDtoClassName();
        daoDescList.forEach(x -> {
            createDaoClass(x, sqlFirstApcConfig);
            for (QueryDesc queryDesc : x.getQueryDescList()) {
                writeDtoClass(queryDesc, true, baseDtoClassName);
                writeDtoClass(queryDesc, false, baseDtoClassName);
                writeResource(queryDesc);
            }
        });
    }
    
    
    /**
     * Write <query>.sql to resources folder
     *
     * @param queryDesc information on query
     */
    private void writeResource(QueryDesc queryDesc) {
        Filer filer = processingEnv.getFiler();
        try {
            FileObject o = filer.createResource(StandardLocation.CLASS_OUTPUT,
                    queryDesc.getPackageName() + ".sql", queryDesc.getMethodNameFirstUpper() + ".sql");
            Writer w = o.openWriter();
            w.append(queryDesc.getQuery());
            w.flush();
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Write Dto class to disk
     *
     * @param queryDesc description of query
     * @param isReq write request (true) or response (false)
     * @param baseDtoClassName name of parent dto class
     */
    private void writeDtoClass(
            QueryDesc queryDesc,
            boolean isReq,
            String baseDtoClassName
    ) {
        if (isReq && CollectionUtils.isEmpty(queryDesc.getRequestParamList())
                || !isReq && CollectionUtils.isEmpty(queryDesc.getResponseParamList())) {
            return;
        }
        try {
            VelocityContext context = new VelocityContext();

            context.put("fields", isReq ? queryDesc.getRequestParamList() : queryDesc.getResponseParamList());

            String methodNameUpper = StringUtils.capitalize(queryDesc.getMethodName());
            String dtoClassName = methodNameUpper + (isReq ? "Req" : "Res");
            String packageName = queryDesc.getPackageName() + ".dto";

            List<String> implementsList = isReq ? queryDesc.getReqImplementsList() : queryDesc.getResImplementsList();
            processImplementsList(context, new HashSet<>(implementsList));

            context.put("dtoClassName", dtoClassName);
            context.put("classPackage", packageName);
            context.put("classJavadoc", queryDesc.getClassJavadoc());
            context.put("baseDtoClass", baseDtoClassName);

            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + dtoClassName);

            try (PrintWriter writer = new PrintWriter(builderFile.openWriter())) {
                Template template = Velocity.getTemplate("dto-class-template.vm", MiscUtils.UTF_8);
                template.merge(context, writer);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void createDaoClass(DaoDesc daoDesc, SqlFirstApcConfig sqlFirstApcConfig) {
        String queryExecutorClassName = sqlFirstApcConfig.queryExecutorClassName();
        
        String baseDaoClassName = StringUtils.isBlank(daoDesc.getBaseDaoClassName()) ? sqlFirstApcConfig.baseDaoClassName() : daoDesc.getBaseDaoClassName();
        
        List<QueryDesc> queryDescList = daoDesc.getQueryDescList();
        QueryDesc firstElement = queryDescList.iterator().next();
        
        VelocityContext context = new VelocityContext();
        String className = firstElement.getClassName();
        String packageName = firstElement.getPackageName();
        String daoClassName = className + "Dao";
    
        String generatedSourcesLocation;
        try {
            context.put("stringUtils", StringUtils.class);
            context.put("hasDtoClasses", queryDescList.stream().anyMatch(queryDesc -> queryDesc.hasRequest() || queryDesc.hasRequest()));
            context.put("queryDescList", queryDescList);
            String implementClassName = daoDesc.getImplementClassName();//implementMap.get(packageName + "." + className);
            
            Set<String> implementsSet = new HashSet<>();
            if (StringUtils.isNotBlank(implementClassName)) {
                implementsSet.add(implementClassName);
            }
            
            context.put("baseClassFullName", baseDaoClassName);
            context.put("baseClassSimpleName", MiscUtils.getLastWordAfterDot(baseDaoClassName));
            
            context.put("queryExecutorClassFullName", queryExecutorClassName);
            context.put("queryExecutorClassName", MiscUtils.getLastWordAfterDot(queryExecutorClassName));

            String templateFileName;
            DaoType daoType = daoDesc.getDaoType();
            if (daoType == null || daoType == DaoType.USE_DEFAULT) {
                daoType = sqlFirstApcConfig.daoType();
            }
            switch (daoType) {
                case PLAIN_SQL:
                    templateFileName = "dao-class-plain-template.vm";
                    break;
                case SPRING_REPOSITORY:
                    templateFileName = "dao-class-spring-template.vm";
                    String targetClassName = daoDesc.getTargetClassName();
                    if (StringUtils.isNotBlank(targetClassName)) {
                        daoClassName = MiscUtils.getLastWordAfterDot(targetClassName);
                        packageName = StringUtils.substring(targetClassName, 0, -daoClassName.length() - 1);
                    } else if (daoClassName.endsWith("CustomDao")) {
                        daoClassName = StringUtils.substring(daoClassName, 0, -9) + "Impl";
                    }
                    implementsSet.add(daoDesc.getSourceClassName());
                    break;
                default:
                    throw new RuntimeException("Not supported DaoType " + daoType);
            }
    
            processImplementsList(context, implementsSet);
            String daoTestClassName = daoClassName + "Test";
            context.put("daoClassName", daoClassName);
            context.put("daoTestClassName", daoTestClassName);
            context.put("classPackage", packageName);
            String daoClassFullName = packageName + "." + daoClassName;
    
            generatedSourcesLocation = writeDao(context, templateFileName, daoClassFullName);
            if (daoType == DaoType.SPRING_REPOSITORY && StringUtils.isNotBlank(sqlFirstApcConfig.baseTest())) {
                context.put("baseTestFullName", sqlFirstApcConfig.baseTest());
                context.put("baseTestSimpleName", MiscUtils.getLastWordAfterDot(sqlFirstApcConfig.baseTest()));
                writeDaoTest(context, generatedSourcesLocation, packageName, daoTestClassName);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    
    }
    
    
    private void writeDaoTest(
            VelocityContext context,
            String generatedSourcesLocation,
            String packageName,
            String daoTestClassName) throws FileNotFoundException {
        if (!generatedSourcesLocation.endsWith(TARGET_GENERATED_SOURCES_ANNOTATIONS)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                    "Can not define source location for tests " + packageName + "." + daoTestClassName + " " + generatedSourcesLocation, globalConfigElement);
        }
        
        String testSourceDir = StringUtils.substring(generatedSourcesLocation, 0, -TARGET_GENERATED_SOURCES_ANNOTATIONS.length())
                + "/target/generated-test-sources/test-annotations/" + packageName.replace(".", File.separator);
        new File(testSourceDir).mkdirs();
        try (PrintWriter writer = new PrintWriter(testSourceDir + File.separator + daoTestClassName + ".java")) {
            Template template = Velocity.getTemplate("dao-test-spring-template.vm", MiscUtils.UTF_8);
            template.merge(context, writer);
        }
    }
    
    private String writeDao(VelocityContext context, String templateFileName, String daoClassFullName) throws IOException {
        String generatedSourcesLocation;
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(daoClassFullName);
        
        generatedSourcesLocation = StringUtils.substring(builderFile.toUri().getPath(), 0, -daoClassFullName.length() - 5);
        
        try (PrintWriter writer = new PrintWriter(builderFile.openWriter())) {
            Template template = Velocity.getTemplate(templateFileName, MiscUtils.UTF_8);
            template.merge(context, writer);
        }
        return generatedSourcesLocation;
    }
    
    private void processImplementsList(VelocityContext context, Set<String> implementsList) {
        if (CollectionUtils.isEmpty(implementsList)) {
            return;
        }
        context.put("implementsFullList", implementsList.stream().sorted().collect(Collectors.toList()));
        context.put("implementsNameList", implementsList.stream().map(MiscUtils::getLastWordAfterDot).sorted().collect(Collectors.toList()));
    }

    private void initVelocity() {
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("resource.loader", "class");
        velocityProperties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityProperties.setProperty("input.encoding", MiscUtils.UTF_8);
        velocityProperties.setProperty("output.encoding", MiscUtils.UTF_8);
        try {
            Velocity.init(velocityProperties);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
