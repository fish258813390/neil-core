/**
 *
 */
package com.neil.core.context;

import com.alibaba.fastjson.JSON;
import com.neil.core.annotation.service.annotation.MethodRemark;
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
public class MethodLogInterceptor implements MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice{
    private static final Logger log = LoggerFactory.getLogger(MethodLogInterceptor.class);
    private long startTime = System.currentTimeMillis();
    private Boolean writeRequestLog = new Boolean(ApplicationConfig.getProperty("write.request.log"));
    /* (non-Javadoc)
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        startTime = System.currentTimeMillis();
        if(writeRequestLog){
            log.info(createLogString("REQUEST",target, method, args, null));
        }

    }
    /* (non-Javadoc)
     * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if(writeRequestLog){
            log.info(createLogString("RESPONSE",target, method, args, returnValue));
        }

    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
        log.error(createLogString("RESPONSE",target, method, args, null),ex);
    }


    private String createLogString(String type,Object target,Method method,Object [] args,Object returnValue){
        StringBuilder sbu = new StringBuilder();
        sbu.append("[{\"TYPE\":").append("\"").append(type).append("\"},")
                .append("{\"METHOD_NAME\":").append("\"").append(method.getName()).append("\"},");
        if(type.equals("RESPONSE")){
            sbu.append("{\"USE_TIME\":").append("\"").append(System.currentTimeMillis()-startTime).append("\"},");
        }
        if(method.isAnnotationPresent(MethodRemark.class)){
            sbu.append("{\"METHOD_REMARK\":").append("\"").append(method.getAnnotation(MethodRemark.class).value()).append("\"},");
        }else{
            sbu.append("{\"METHOD_REMARK\":").append("\"").append("NULL_REMARK").append("\"},");
        }

        String argsString = JSON.toJSONString(args);
        if(type.equals("RESPONSE")){
            sbu.append("{\"PARAMS\":").append(argsString).append("},");
            if(returnValue != null){
                String returnString = JSON.toJSONString(returnValue);
                sbu.append("{\"RETURN_VALUE\":").append(returnString).append("}]");
            }else{
                sbu.append("{\"RETURN_VALUE\":").append("\"").append("NULL").append("\"}]");
            }
        }else{
            sbu.append("{\"PARAMS\":").append(argsString).append("},");
        }
        return sbu.toString();
    }
}
