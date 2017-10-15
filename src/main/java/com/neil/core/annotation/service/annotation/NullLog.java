package com.neil.core.annotation.service.annotation;

import java.lang.annotation.*;

/**
 * @author wanghuajian 2015-5-25
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NullLog {
}
