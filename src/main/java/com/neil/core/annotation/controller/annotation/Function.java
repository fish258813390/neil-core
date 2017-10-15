package com.neil.core.annotation.controller.annotation;

import java.lang.annotation.*;

/**
 * @author hutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Function {
    public String code();
    public String name();
}
