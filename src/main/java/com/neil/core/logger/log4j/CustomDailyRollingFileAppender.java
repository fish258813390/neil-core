package com.neil.core.logger.log4j;

import org.apache.log4j.DailyRollingFileAppender;

import java.io.File;

/**
 * fixed not create mkdir
 * Created by gocreater on 2016/9/28.
 */
public class CustomDailyRollingFileAppender extends DailyRollingFileAppender {

    @Override
    public void setFile(String file) {
        String filePath = file;
        File fileCheck = new File(filePath);
        if (!fileCheck.exists()) {
            fileCheck.getParentFile().mkdirs();
        }
        super.setFile(filePath);
    }

}
