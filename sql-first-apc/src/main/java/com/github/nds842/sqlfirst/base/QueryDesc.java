package com.github.nds842.sqlfirst.base;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL query description
 */
public class QueryDesc {

    private String query;
    private String queryName;
    private String className;
    private String packageName;

    private List<ParamDesc> responseParamList;
    private List<ParamDesc> requestParamList;
    private List<String> reqImplementsList = new ArrayList<>();
    private List<String> resImplementsList = new ArrayList<>();

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
        return MiscUtils.prepareNameString(queryName);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getClassJavadoc() {
        return MiscUtils.prepareJavadoc(query, 0);
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getMethodNameFirstUpper() {
        return StringUtils.capitalize(getMethodName());
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    /**
     * @return Javadoc for Dao class method
     */
    public String getMethodJavadoc() {
        return MiscUtils.prepareJavadoc(query, 4);
    }

    public boolean hasRequest() {
        return CollectionUtils.isNotEmpty(requestParamList);
    }

    public boolean hasResponse() {
        return CollectionUtils.isNotEmpty(responseParamList);
    }

    public String responseClass() {
        return getMethodNameFirstUpper() + "Res";
    }

    public String requestClass() {
        return getMethodNameFirstUpper() + "Req";
    }

    public String getMethodNameUnderscores() {
        return MiscUtils.underscores(getMethodName());
    }

    public String getQueryEscaped() {
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

    public List<String> getReqImplementsList() {
        return reqImplementsList;
    }

    public void setReqImplementsList(List<String> reqImplementsList) {
        this.reqImplementsList = reqImplementsList;
    }

    public List<String> getResImplementsList() {
        return resImplementsList;
    }

    public void setResImplementsList(List<String> resImplementsList) {
        this.resImplementsList = resImplementsList;
    }

    @Override
    public String toString() {
        return queryName + " req: " + requestParamList + ", res: " + responseParamList;
    }
}
