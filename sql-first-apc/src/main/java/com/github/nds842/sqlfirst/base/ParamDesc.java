package com.github.nds842.sqlfirst.base;


import org.apache.commons.lang.StringUtils;

public class ParamDesc implements Comparable<ParamDesc> {

    private final int index;
    private Class<?> columnType;
    private String columnTypeName;
    private String realName;
    private String name;
    private String nameFirstUpper;
    private String nameUnderscores;

    public ParamDesc(int index, Class<?> columnType, String name) {
        this(index, columnType, name, name);
    }


    public ParamDesc(int index, Class<?> columnType, String name, String realName) {
        this.columnType = columnType;
        this.columnTypeName = columnType.getName();
        this.index = index;
        this.realName = realName;
        this.setName(name);
    }

    public int getIndex() {
        return index;
    }

    public Class<?> getColumnType() {
        return columnType;
    }

    public void setColumnType(Class<?> columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String getName() {
        return name;
    }

    public String getNameFirstUpper() {
        return nameFirstUpper;
    }

    public String getNameUnderscores() {
        return nameUnderscores;
    }


    public String getRealName() {
        return realName;
    }

    @Override
    public String toString() {
        return "[" + columnType.getSimpleName() + (realName == null ? "" : ", " + realName) + (name == null ? "" : ", " + name) + "]";
    }

    public void setName(String name) {
        this.name = MiscUtils.prepareNameString(name);
        this.nameFirstUpper = StringUtils.capitalize(this.name);
        this.nameUnderscores = MiscUtils.underscores(this.name);
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

    public String simpleTypeName() {
        return columnType.getSimpleName();
    }
}
