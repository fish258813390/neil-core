package com.neil.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware{
    public static ApplicationContext applicationContext;
    public static DataSourceTransactionManager transactionManager;
    @Override
    public void setApplicationContext(ApplicationContext applicationContexta)
            throws BeansException {
        applicationContext = applicationContexta;
        try {
            transactionManager = (DataSourceTransactionManager)SpringApplicationContext.applicationContext.getBean("transactionManager");
        } catch (Exception e) {
        }
    }

}
