package com.github.nds842.sqlfirst.base;

import java.util.Map;

/**
 * Parent of all generated Dto classes
 */
public abstract class BaseDto {
    
    /**
     * Technical method to obtain values from underlying map
     *
     * @param map map to get parameters from
     * @param paramName name of key
     * @param <T> type of result
     * @return value from map
     */
    @SuppressWarnings("unchecked")
    protected <T> T getValue(Map<String, Object> map, String paramName) {
        return (T) map.get(paramName);
    }
    
    /**
     * @return get dto values as unmodifiable map
     */
    public abstract Map<String, Object> toMap();
}
