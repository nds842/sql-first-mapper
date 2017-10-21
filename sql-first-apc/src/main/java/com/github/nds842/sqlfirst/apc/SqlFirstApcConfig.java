package com.github.nds842.sqlfirst.apc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * APC settings for module
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SqlFirstApcConfig {
    
    String baseDaoClassName();
    
    String baseDtoClassName() default "com.github.nds842.sqlfirst.base.BaseDto";
    
}