package com.neil.core.annotation.service.annotation;

import java.lang.annotation.*;

/**
 * @author neil 2015-6-8
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HistoryDataParam {
    public String rootClassName();
    public String firstClassName();
    public String secondClassName();
    public String days();
    public String timeParam();
    public String dataSourceName();
}
