package com.github.nds842.sqlfirst.apc;

import com.github.nds842.sqlfirst.base.DaoDesc;
import com.github.nds842.sqlfirst.base.MiscUtils;
import com.github.nds842.sqlfirst.base.QueryDesc;
import com.github.nds842.sqlfirst.parser.SuffixParser;
import com.google.auto.service.AutoService;
import org.apache.commons.io.IOUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Annotation processor for SqlSource and SqlSourceFile annotations
 */
@SuppressWarnings("unused")
@SupportedAnnotationTypes({"com.github.nds842.sqlfirst.apc.SqlSource", "com.github.nds842.sqlfirst.apc.SqlSourceFile"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class SqlDaoAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
    
        List<DaoDesc> daoDescList = processSqlSource(roundEnv);
        prepareDaoClassImplementMap(daoDescList, roundEnv);

        DaoWriter daoWriter = new DaoWriter(processingEnv);
        //TODO move to settings
        String baseDaoClassName = "com.github.nds842.sqlfirst.base.BaseDao";
        String baseDtoClassName = "com.github.nds842.sqlfirst.base.BaseDto";
        daoWriter.write(daoDescList, baseDaoClassName, baseDtoClassName);

        return !daoDescList.isEmpty();
    }

    /**
     * Prepare Dao interfaces from {@link SqlSourceFile} annotation
     *
     * @param daoDescList dao description
     * @param roundEnv annotation processor environment
     */
    private void prepareDaoClassImplementMap(List<DaoDesc> daoDescList, RoundEnvironment roundEnv) {
        Map<String, DaoDesc> daoDescMap = daoDescList.stream().collect(Collectors.toMap(DaoDesc::getSourceClassName, x -> x));
     
        Elements elementUtils = processingEnv.getElementUtils();

        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(SqlSourceFile.class);
        for (Element element : annotatedElements) {
            SqlSourceFile ann = element.getAnnotation(SqlSourceFile.class);
            String key = elementUtils.getPackageOf(element).getQualifiedName().toString() + "." + element.getSimpleName().toString();
            DaoDesc daoDesc = daoDescMap.get(key);
            if (daoDesc == null){
                continue;
            }
            if (ann.implement() || ann.value() == DaoType.SPRING_REPOSITORY) {
                daoDesc.setImplementClassName(element.asType().toString());
            }
            daoDesc.setDaoType(ann.value());
        }
    }

    /**
     * Method creates query descriptions from {@link SqlSource} annotated methods
     *
     * @param roundEnv annotation processor environment
     * @return list of query descriptions
     */
    private List<DaoDesc> processSqlSource(RoundEnvironment roundEnv) {
        List<QueryDesc> queryDescList = new ArrayList<>();


        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(SqlSource.class);
        for (Element element : annotatedElements) {
            Elements elementUtils = processingEnv.getElementUtils();
            String queryName = element.getSimpleName().toString();
            SqlSource ann = element.getAnnotation(SqlSource.class);

            String sqlAsString;
            switch (ann.sqlSourceType()) {
                case FILE:
                    try {
                        FileObject resource = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", ann.sqlSourceFile());
                        InputStream inputStream = resource.openInputStream();
                        sqlAsString = IOUtils.toString(inputStream, MiscUtils.UTF_8);
                    } catch (IOException e) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Can not find file: " + ann.sqlSourceFile(), element);
                        continue;
                    }
                    break;
                case JAVADOC:
                    sqlAsString = elementUtils.getDocComment(element);
                    break;
                default:
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Not supported source: " + ann.sqlSourceType(), element);
                    continue;
            }

            //TODO choose parser by annotation parameter
            QueryDesc queryDesc = new SuffixParser().parse(sqlAsString);

            queryDesc.setReqImplementsList(getTypesFromAnnotation(element, true));
            queryDesc.setResImplementsList(getTypesFromAnnotation(element, false));
            queryDesc.setQueryName(queryName);
            String packageName = elementUtils.getPackageOf(element).toString();
            queryDesc.setPackageName(packageName);
            queryDesc.setClassName(element.getEnclosingElement().getSimpleName().toString());

            queryDescList.add(queryDesc);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "APC working on " + queryName, element);
        }
    
        return queryDescList.stream().collect(
                Collectors.groupingBy(
                        qDesc -> qDesc.getPackageName() + "." + qDesc.getClassName(),
                        Collectors.mapping(qDesc -> qDesc, Collectors.toList()))
        ).values().stream().map(x ->
        {
            DaoDesc daoDesc = new DaoDesc();
            daoDesc.setQueryDescList(x);
            QueryDesc qDesc = x.get(0);
            daoDesc.setSourceClassName(qDesc.getPackageName() + "." + qDesc.getClassName());
            return daoDesc;
        }).collect(Collectors.toList());
    }

    /**
     * At compile time classes can not be obtained from annotation directly.
     * This method extracts class information from annotation mirrors.
     *
     * @param element element annotated with {@link SqlSource} annotation
     * @param isReq   true for request, false for result parameters
     * @return list of class names from {@link SqlSource} annotation
     */
    private List<String> getTypesFromAnnotation(Element element, boolean isReq) {
        TypeMirror sqlSourceTypeMirror = processingEnv.getElementUtils().getTypeElement(SqlSource.class.getName()).asType();
        //method names from SqlSource annotation
        String methodName = isReq ? "reqImpl" : "resImpl";
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        List<String> typeNames = new ArrayList<>();
        for (AnnotationMirror am : annotationMirrors) {
            if (!am.getAnnotationType().equals(sqlSourceTypeMirror)) {
                continue;
            }
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                if (!methodName.equals(entry.getKey().getSimpleName().toString())) {
                    continue;
                }
                AnnotationValue impResVal = entry.getValue();
                impResVal.accept(new SimpleAnnotationValueVisitor8<Void, Void>() {
                    @Override
                    public Void visitArray(List<? extends AnnotationValue> list, Void s) {
                        for (AnnotationValue val : list) {
                            TypeMirror typeMirror = (TypeMirror) val.getValue();
                            typeNames.add(typeMirror.toString());
                        }
                        return null;
                    }
                }, null);
                break;
            }
        }
        return typeNames;
    }
}
