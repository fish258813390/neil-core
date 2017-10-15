/**
 *
 */
package com.neil.core.context;

import com.neil.core.annotation.service.annotation.DataSource;
import com.neil.core.db.datasource.JdbcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author wanghuajian 2016年9月13日
 *
 */
public class DataSourceMethodInterceptor implements MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice{
    private static final Logger log = LoggerFactory.getLogger(DataSourceMethodInterceptor.class);
    /* (non-Javadoc)
     * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if(method.isAnnotationPresent(DataSource.class)){
            DataSource dataSource = method.getAnnotation(DataSource.class);
            JdbcContext.setJdbcType(dataSource.value());
        }else{
//			log.info("写库执行SQL:"+SpringApplicationContext.dd.getConnection().getMetaData().getURL()+":"+SpringApplicationContext.dd.getConnection().getMetaData().getUserName());
            JdbcContext.setMaster();
        }
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
    }

}
