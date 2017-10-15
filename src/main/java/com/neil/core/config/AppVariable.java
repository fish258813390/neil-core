package com.neil.core.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 */
public class AppVariable {

    public static final String COMMA_SPLIT_TAG = ",";

    public static final String UNDERLINE_SPLIT_TAG = "_";

    public static String addHeadEndComma(String property) {
        if (StringUtils.isNotBlank(property)) {
            StringBuffer newValueBuffer = new StringBuffer();
            String splitTag = property.substring(0, 1);
            if (!AppVariable.COMMA_SPLIT_TAG.equals(splitTag)) {
                newValueBuffer.append(AppVariable.COMMA_SPLIT_TAG);
            }
            newValueBuffer.append(property);
            int valueStringLength = property.length();
            splitTag = property.substring(valueStringLength - 1, valueStringLength);
            if (!AppVariable.COMMA_SPLIT_TAG.equals(splitTag)) {
                newValueBuffer.append(AppVariable.COMMA_SPLIT_TAG);
            }
            return newValueBuffer.toString();
        } else {
            return property;
        }
    }

    public static boolean itemExitsMoreValue(Map map, String itemKey) {
        if (map != null && map.containsKey(itemKey)) {
            Object itemValue = map.get(itemKey);
            if (itemValue != null && !"".equals(itemValue) && itemValue.toString().indexOf(",") > 0) {
                return true;
            }
        }
        return false;
    }
}
