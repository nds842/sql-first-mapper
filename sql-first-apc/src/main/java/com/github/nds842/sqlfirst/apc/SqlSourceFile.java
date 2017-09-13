package com.github.nds842.sqlfirst.apc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SqlSourceFile {

    /**
     * Put annotation with implement = true to make generated Dao class implement interface containing this annotation
     * Beware - you will have to put correct names of input and output parameters to compile the project!
     *
     * @return true if dao must implement this interface
     */
    boolean implement() default false;
}
