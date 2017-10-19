package com.github.nds842.sqlfirst.base;


import com.github.nds842.sqlfirst.apc.DaoType;

import java.util.List;

public class DaoDesc {
    List<QueryDesc> queryDescList;
    private String implementClassName;
    private String sourceClassName;
    private DaoType daoType;
    
    public List<QueryDesc> getQueryDescList() {
        return queryDescList;
    }
    
    public void setQueryDescList(List<QueryDesc> queryDescList) {
        this.queryDescList = queryDescList;
    }
    
    public String getImplementClassName() {
        return implementClassName;
    }
    
    public void setImplementClassName(String implementClassName) {
        this.implementClassName = implementClassName;
    }
    
    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }
    
    public String getSourceClassName() {
        return sourceClassName;
    }
    
    public DaoType getDaoType() {
        return daoType;
    }
    
    public void setDaoType(DaoType daoType) {
        this.daoType = daoType;
    }
}
