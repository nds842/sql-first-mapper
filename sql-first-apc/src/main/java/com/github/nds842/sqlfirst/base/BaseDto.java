package com.github.nds842.sqlfirst.base;

import java.util.Map;

/**
 * Parent of all generated Dto classes
 */
public abstract class BaseDto {

    @SuppressWarnings("unchecked")
    protected <T> T getValue(Map<String, Object> map, String paramName) {
        return (T) map.get(paramName);
    }

    public abstract Map<String, Object> toMap();

}
