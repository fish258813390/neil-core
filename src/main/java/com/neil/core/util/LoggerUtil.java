package com.neil.core.util;

import org.slf4j.Logger;

/**
 * Created by gocreater on 2016/10/24.
 */
public class LoggerUtil {

    public static void error(Logger logger, String customMessage, Exception exception) {
        if (exception.getMessage() != null) {
            logger.error(customMessage + ",ERROR:" + exception.getMessage(), exception);
        } else {
            logger.error(customMessage + ",ERROR:无明确异常消息，请查看异常堆栈信息", exception);
        }

    }

    public static void warn(Logger logger, String customMessage, Exception exception) {
        if (exception.getMessage() != null) {
            logger.warn(customMessage + ",ERROR:" + exception.getMessage(), exception);
        } else {
            logger.warn(customMessage + ",ERROR:无明确异常消息，请查看异常堆栈信息", exception);
        }
    }
}
