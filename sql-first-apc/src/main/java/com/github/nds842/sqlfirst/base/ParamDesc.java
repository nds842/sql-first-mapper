package com.github.nds842.sqlfirst.base;


import org.apache.commons.lang.StringUtils;

/**
 * ParamDesc contains description of sql query parameter either request or response
 */
public class ParamDesc implements Comparable<ParamDesc> {
    
    /**
     * Index of parameter in query
     */
    private final int index;
    
    /**
     * Type of column
     */
    private final Class<?> columnType;
    
    /**
     * Real name of column
     */
    private final String realName;
    
    /**
     * Normalized name of column
     */
    private final String name;

    public ParamDesc(int index, Class<?> columnType, String name, String realName) {
        this.columnType = columnType;
        this.index = index;
        this.realName = realName;
        this.name = MiscUtils.prepareNameString(name);
    }

    public int getIndex() {
        return index;
    }

    public Class<?> getColumnType() {
        return columnType;
    }
    
    public String getColumnTypeName() {
        return columnType.getName();
    }

    public String getName() {
        return name;
    }

    public String getNameFirstUpper() {
        return StringUtils.capitalize(this.name);
    }

    public String getNameUnderscores() {
        return MiscUtils.underscores(this.name);
    }

    public String getRealName() {
        return realName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParamDesc paramDesc = (ParamDesc) o;

        return realName.equals(paramDesc.realName);
    }

    @Override
    public int hashCode() {
        return realName.hashCode();
    }

    @Override
    public int compareTo(ParamDesc paramDesc) {
        return paramDesc.getName().compareTo(paramDesc.getName());
    }

    public String getSimpleTypeName() {
        return columnType.getSimpleName();
    }
    
    @Override
    public String toString() {
        return "[" + columnType.getSimpleName() + (realName == null ? "" : ", " + realName) + (name == null ? "" : ", " + name) + "]";
    }
}
