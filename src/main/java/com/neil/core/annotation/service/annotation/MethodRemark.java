package com.neil.core.annotation.service.annotation;


import java.lang.annotation.*;

/**
 * @author neil 2015-5-19
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodRemark {
    public String value();
}
