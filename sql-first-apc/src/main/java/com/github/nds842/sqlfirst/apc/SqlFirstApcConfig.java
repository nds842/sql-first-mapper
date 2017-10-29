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
    
    /**
     * @return which type of Dao class should be generated
     */
    DaoType daoType();
    
    /**
     * @return Base class for generated if empty - generated class would not extend
     */
    String baseDaoClassName() default "";
    
    /**
     * @return Query executor class name
     */
    String queryExecutorClassName() default "";
    
    /**
     * @return base dao request-response dto class name
     */
    String baseDtoClassName() default "com.github.nds842.sqlfirst.base.BaseDto";
    
}
