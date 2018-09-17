package com.selcukc.mongo_rest.repositories.storable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Inherited
public @interface Storable {

    String value() default "";
}

