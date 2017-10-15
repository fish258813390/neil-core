package com.neil.core.util;

import com.neil.core.config.AppVariable;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gocreater on 2016/9/1.
 */
public class MapUtil {

    public static Map<String, Object> formatMap(Map<String, String[]> parameterMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator<String> paramNameIt = parameterMap.keySet().iterator();
        while (paramNameIt.hasNext()) {
            String parameterName = paramNameIt.next();
            Object paramValue = parameterMap.get(parameterName);
            if (paramValue == null) {
                resultMap.put(parameterName, null);
            } else if (paramValue instanceof String[]) {
                resultMap.put(parameterName, StringUtil.arrayToStr((String[]) paramValue));
            } else {
                resultMap.put(parameterName, paramValue);
            }
        }
        return resultMap;
    }

    public static Object getParamByParamHeadForSingleKey(Map<String, Object> parameterMap, String paramNameHead, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = getParamByParamHead(parameterMap, paramNameHead, ignoreBlankValue);
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : resultMap.keySet()) {
            stringBuffer.append(key).append(AppVariable.COMMA_SPLIT_TAG);
        }
        if (stringBuffer.indexOf(AppVariable.COMMA_SPLIT_TAG) > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static Object getParamByParamHeadAndEndForSingleKey(Map<String, Object> parameterMap, String paramNameHead, String paramNameEnd, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = getParamByParamHeadAndEnd(parameterMap, paramNameHead, paramNameEnd, ignoreBlankValue);
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : resultMap.keySet()) {
            stringBuffer.append(key).append(AppVariable.COMMA_SPLIT_TAG);
        }
        if (stringBuffer.indexOf(AppVariable.COMMA_SPLIT_TAG) > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static String getParamByParamHeadForSingleValue(Map<String, Object> parameterMap, String paramNameHead, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = getParamByParamHead(parameterMap, paramNameHead, ignoreBlankValue);
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : resultMap.keySet()) {
            stringBuffer.append(resultMap.get(key)).append(AppVariable.COMMA_SPLIT_TAG);
        }
        if (stringBuffer.indexOf(AppVariable.COMMA_SPLIT_TAG) > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static String getParamByParamHeadAndEndForSingleValue(Map<String, Object> parameterMap, String paramNameHead, String paramNameEnd, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = getParamByParamHeadAndEnd(parameterMap, paramNameHead, paramNameEnd, ignoreBlankValue);
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : resultMap.keySet()) {
            stringBuffer.append(resultMap.get(key)).append(AppVariable.COMMA_SPLIT_TAG);
        }
        if (stringBuffer.indexOf(AppVariable.COMMA_SPLIT_TAG) > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static Map<String, Object> getParamByParamHead(Map<String, Object> parameterMap, String paramNameHead, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator<String> paramNameIt = parameterMap.keySet().iterator();
        while (paramNameIt.hasNext()) {
            String parameterName = paramNameIt.next();
            Object paramValue = parameterMap.get(parameterName);
            if (parameterName.indexOf(paramNameHead) == 0) {
                String trueParamName = parameterName.substring(paramNameHead.length(), parameterName.length());
                processValue(resultMap, trueParamName, paramValue, ignoreBlankValue);
            }
        }
        return resultMap;
    }

    public static Map<String, Object> getParamByParamHeadAndEnd(Map<String, Object> parameterMap, String paramNameHead, String paramNameEnd, boolean ignoreBlankValue) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator<String> paramNameIt = parameterMap.keySet().iterator();
        while (paramNameIt.hasNext()) {
            String parameterName = paramNameIt.next();
            Object paramValue = parameterMap.get(parameterName);
            if (parameterName.indexOf(paramNameHead) == 0 && parameterName.lastIndexOf(paramNameEnd) > 0) {
                String trueParamName = parameterName.substring(paramNameHead.length(), parameterName.length());
                trueParamName = trueParamName.substring(0, trueParamName.length() - paramNameEnd.length());
                processValue(resultMap, trueParamName, paramValue, ignoreBlankValue);
            }
        }
        return resultMap;
    }

    private static void processValue(Map<String, Object> resultMap, String paramName, Object paramValue, boolean ignoreBlankValue) {
        if (!ignoreBlankValue && paramValue == null) {
            resultMap.put(paramName, paramValue);
        } else if (paramValue != null && paramValue instanceof String) {
            if (!ignoreBlankValue && StringUtils.isBlank(paramValue.toString())) {
                resultMap.put(paramName, paramValue);
            } else if (StringUtils.isNotBlank(paramValue.toString())) {
                resultMap.put(paramName, paramValue);
            }
        } else if (paramValue != null && paramValue instanceof String[] && ((String[]) paramValue).length > 1) {
            resultMap.put(paramName, StringUtil.arrayToStr((String[]) paramValue));
        } else if (paramValue != null && paramValue instanceof String[] && ((String[]) paramValue).length == 1) {
            if (!ignoreBlankValue && StringUtils.isBlank(((String[]) paramValue)[0])) {
                resultMap.put(paramName, ((String[]) paramValue)[0]);
            } else if (StringUtils.isNotBlank(((String[]) paramValue)[0])) {
                resultMap.put(paramName, ((String[]) paramValue)[0]);
            }
        } else if (paramValue != null && paramValue instanceof String[] && ((String[]) paramValue).length > 1) {
            resultMap.put(paramName, StringUtil.arrayToStr((String[]) paramValue));
        }
    }

}
