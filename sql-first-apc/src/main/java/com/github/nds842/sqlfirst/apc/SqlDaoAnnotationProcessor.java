package com.github.nds842.sqlfirst.apc;

import com.github.nds842.sqlfirst.base.DaoBuilder;
import com.github.nds842.sqlfirst.base.QueryDesc;
import com.github.nds842.sqlfirst.parser.SuffixParser;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Annotation processor fot SqlSource and SqlSourceFile annotations
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

        Map<String, Set<String>> implementMap = prepareImplementMap(roundEnv);
        List<QueryDesc> queryDescList = processSqlSource(roundEnv);

        DaoBuilder daoBuilder = new DaoBuilder(processingEnv);
        //TODO move to settings
        String baseDaoClassName = "com.github.nds842.sqlfirst.base.BaseDao";
        String baseDtoClassName = "com.github.nds842.sqlfirst.base.BaseDto";
        daoBuilder.build(queryDescList, baseDaoClassName, baseDtoClassName, implementMap);

        return !queryDescList.isEmpty();
    }

    private Map<String, Set<String>> prepareImplementMap(RoundEnvironment roundEnv) {
        Map<String, Set<String>> implementMap = new HashMap<>();
        Elements elementUtils = processingEnv.getElementUtils();

        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(SqlSourceFile.class);
        for (Element element : annotatedElements) {
            SqlSourceFile ann = element.getAnnotation(SqlSourceFile.class);
            if (ann.implement()) {
                String key = element.getEnclosingElement().getSimpleName().toString() + "." + element.getSimpleName().toString();
                Set<String> set = implementMap.computeIfAbsent(key, k -> new HashSet<>());
                set.add(element.asType().toString());
            }
        }
        return implementMap;
    }

    private List<QueryDesc> processSqlSource(RoundEnvironment roundEnv) {
        List<QueryDesc> queryDescList = new ArrayList<>();

        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(SqlSource.class);
        for (Element element : annotatedElements) {

            Elements elementUtils = processingEnv.getElementUtils();
            String comment = elementUtils.getDocComment(element);
            String queryName = element.getSimpleName().toString();

            //TODO choose parser by annotation parameter
            QueryDesc queryDesc = new SuffixParser().parse(comment);

            queryDesc.setQueryName(queryName);
            String packageName = elementUtils.getPackageOf(element).toString();
            queryDesc.setPackageName(packageName);
            queryDesc.setClassName(element.getEnclosingElement().getSimpleName().toString());

            queryDescList.add(queryDesc);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "APC working on " + queryName, element);
        }
        return queryDescList;
    }
}
