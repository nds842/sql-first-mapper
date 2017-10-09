package com.github.nds842.sqlfirst.apc;

import com.github.nds842.sqlfirst.base.MiscUtils;
import com.github.nds842.sqlfirst.base.QueryDesc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builder class for Dao classes - writes
 */
public class DaoWriter {
    
    /**
     * Reference to ProcessingEnvironment to write files and report errors
     */
    private final ProcessingEnvironment processingEnv;
    
    public DaoWriter(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        initVelocity();
    }

    public void write(
            List<QueryDesc> queryDescList,
            String baseDaoClassName,
            String baseDtoClassName,
            Map<String, String> implementMap) {

        Collection<List<QueryDesc>> groupsByClass = queryDescList.stream().collect(
                Collectors.groupingBy(
                        qDesc -> qDesc.getPackageName() + "." + qDesc.getClassName(),
                        Collectors.mapping(qDesc -> qDesc, Collectors.toList()))
        ).values();

        groupsByClass.forEach(x -> {
            createDaoClass(x, baseDaoClassName, implementMap);
            for (QueryDesc queryDesc : x) {
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

    private void createDaoClass(List<QueryDesc> queryDescList, String baseDaoClassName, Map<String, String> implementMap) {
        QueryDesc firstElement = queryDescList.iterator().next();

        VelocityContext context = new VelocityContext();
        String className = firstElement.getClassName();
        String packageName = firstElement.getPackageName();
        String daoClassName = className + "Dao";

        try {
            context.put("hasDtoClasses", queryDescList.stream().anyMatch(queryDesc -> queryDesc.hasRequest() || queryDesc.hasRequest()));
            context.put("queryDescList", queryDescList);
            context.put("daoClassName", daoClassName);
            String implementClassName = implementMap.get(packageName + "." + className);
            if (StringUtils.isNotBlank(implementClassName)) {
                processImplementsList(context, Collections.singleton(implementClassName));
            }
            context.put("classPackage", packageName);
            context.put("baseClassFullName", baseDaoClassName);
            context.put("baseClassSimpleName", MiscUtils.getLastWordAfterDot(baseDaoClassName));

            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + daoClassName);

            try (PrintWriter writer = new PrintWriter(builderFile.openWriter())) {
                Template template = Velocity.getTemplate("dao-class-template.vm", MiscUtils.UTF_8);
                template.merge(context, writer);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
