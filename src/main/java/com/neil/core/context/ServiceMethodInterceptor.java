/**
 *
 */
package com.neil.core.context;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import com.alibaba.fastjson.JSON;
import com.neil.commons.dto.RequestHead;
import com.neil.commons.exception.BusinessException;
import com.neil.core.annotation.service.annotation.DataSource;
import com.neil.core.annotation.service.annotation.HistoryDataParam;
import com.neil.core.annotation.service.annotation.NullLog;
import com.neil.core.annotation.service.annotation.Slave;
import com.neil.core.annotation.service.annotation.Statistics;
import com.neil.core.db.datasource.JdbcContext;

/**
 * @author neil 2015-5-21
 *
 */
@Deprecated
public class ServiceMethodInterceptor implements MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice{
    private static final Logger log = LoggerFactory.getLogger(ServiceMethodInterceptor.class);

    private String requestId = "";
    private String requestType = "接收请求";
    private String responseType = "响应请求";
    private RequestHead requestHead = null;
    private long startTime = System.currentTimeMillis();
    /* (non-Javadoc)
     * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void afterReturning(Object returnValue, Method method,
                               Object[] args, Object target) throws Throwable {
        if(!method.isAnnotationPresent(NullLog.class)){
            Object [] obj = {requestHead};
            log.info(getLogStr(responseType,target, method, obj, returnValue));
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void before(Method method, Object[] args, Object target)
            throws Throwable {

        if(target.getClass().getName().contains("com.alibaba.dubbo.common.bytecode")){
            requestType = "请求服务端";
            responseType = "服务端响应";
        }else{
            requestType = "接收请求";
            responseType = "响应请求";
        }

        for (Object object : args) {
            if(object instanceof RequestHead){
                requestHead = (RequestHead) object;
                break;
            }
        }
        requestId = requestHead.getRequestId();
        requestHead.validateRequestHead();
        if(method.isAnnotationPresent(Slave.class)){
//			log.info("读库执行SQL:"+SpringApplicationContext.dd.getConnection().getMetaData().getURL()+":"+SpringApplicationContext.dd.getConnection().getMetaData().getUserName());
            JdbcContext.setSlave();
        }else if(method.isAnnotationPresent(Statistics.class)){
            JdbcContext.setJdbcType("statistics");
        }else if(method.isAnnotationPresent(DataSource.class)){
            DataSource dataSource = method.getAnnotation(DataSource.class);
            JdbcContext.setJdbcType(dataSource.value());
        }else if(method.isAnnotationPresent(HistoryDataParam.class)){
            for (Object object : args) {
                HistoryDataParam historyDataParam = method.getAnnotation(HistoryDataParam.class);
                //historyDataParam
            }
        }else{
//			log.info("写库执行SQL:"+SpringApplicationContext.dd.getConnection().getMetaData().getURL()+":"+SpringApplicationContext.dd.getConnection().getMetaData().getUserName());
            JdbcContext.setMaster();
        }

        if(!method.isAnnotationPresent(NullLog.class)){
            startTime = System.currentTimeMillis();
            log.info(getLogStr(requestType,target, method, args, null));
        }
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
        if(ex instanceof BusinessException){
            log.error(getLogStr(responseType,target, method, args, null),ex);
        }else{
            log.error(getLogStr(responseType,target, method, args, null),ex);
        }
    }


    private String getLogStr(String type,Object target,Method method,Object [] args,Object returnValue){
        StringBuilder sbu = new StringBuilder();
        sbu.append("{\"类型\":\""+type+"\"},");
        sbu.append("{\"ID\":\""+requestId+"\"},");
        sbu.append("{\"耗时\":\""+(System.currentTimeMillis() - startTime)+"\"},");
        sbu.append("{\"类\":\""+target.getClass().getName()+"\"},");
        sbu.append("{\"方法\":\""+method.getName()+"\"},");
        sbu.append("{\"参数\":\""+JSON.toJSONString(args)+"\"}");
        if(returnValue != null){
            sbu.append(",{\"返回值\":\""+JSON.toJSONString(returnValue)+"\"}");
        }
        return sbu.toString();
    }
}
