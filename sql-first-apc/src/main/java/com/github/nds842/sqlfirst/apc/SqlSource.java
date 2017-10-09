package com.github.nds842.sqlfirst.apc;

import com.github.nds842.sqlfirst.base.SQLSourceType;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map SQL queries
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface SqlSource {

    /**
     * Interfaces to implement for SQL query result items
     */
    Class<?>[] resImpl() default {};

    /**
     * Interfaces to implement for SQL query param item
     */
    Class<?>[] reqImpl() default {};

    /**
     * Source of raw SQL text
     */
    SQLSourceType sqlSourceType() default SQLSourceType.JAVADOC;
    
    /**
     * @return name of file containing SQL for sqlSourceType == FILE
     */
    String sqlSourceFile() default StringUtils.EMPTY;
}
