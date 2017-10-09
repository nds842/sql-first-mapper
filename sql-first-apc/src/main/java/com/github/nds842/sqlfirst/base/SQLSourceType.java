package com.github.nds842.sqlfirst.base;

/**
 * Type of SQL source
 */
public enum SQLSourceType {
    /**
     * Take SQL query as javadoc of annotated method
     */
    JAVADOC,
    
    /**
     * Take SQL query from file
     */
    FILE
}
