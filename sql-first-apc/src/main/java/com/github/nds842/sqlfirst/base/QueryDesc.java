package com.github.nds842.sqlfirst.base;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class QueryDesc {

    private String query;
    private String queryName;
    private String methodName;
    private List<ParamDesc> responseParamList;
    private List<ParamDesc> requestParamList;
    private String methodJavadoc;
    private String classJavadoc;
    private String packageName;
    private String className;

    public void setResponseParamList(List<ParamDesc> responseParamList) {
        this.responseParamList = responseParamList;
    }

    public List<ParamDesc> getResponseParamList() {
        return responseParamList;
    }

    public void setRequestParamList(List<ParamDesc> requestParamList) {
        this.requestParamList = requestParamList;
    }

    public List<ParamDesc> getRequestParamList() {
        return requestParamList;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        this.methodJavadoc = MiscUtils.prepareJavadoc(query, 4);
        this.classJavadoc = MiscUtils.prepareJavadoc(query, 0);
    }

    public String getClassJavadoc() {
        return classJavadoc;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
        this.methodName = MiscUtils.prepareNameString(queryName);
    }

    public String getMethodNameFirstUpper() {
        return StringUtils.capitalize(methodName);
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getMethodJavadoc() {
        return methodJavadoc;
    }

    public boolean hasRequest(){
        return CollectionUtils.isNotEmpty(requestParamList);
    }

    public boolean hasResponse(){
        return CollectionUtils.isNotEmpty(responseParamList);
    }

    public String responseClass() {
        return getMethodNameFirstUpper() + "Res";
    }

    public String requestClass() {
        return getMethodNameFirstUpper() + "Req";
    }

    public String getMethodNameUnderscores(){
        return MiscUtils.underscores(methodName);
    }

    public String getQueryEscaped(){
        return MiscUtils.escape(query);
    }

    public String getQueryName() {
        return queryName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "req: " + requestParamList + ", res: "+responseParamList;
    }

}
