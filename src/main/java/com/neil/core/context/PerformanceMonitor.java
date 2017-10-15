package com.neil.core.context;

import com.alibaba.fastjson.JSON;
import com.neil.commons.model.PerformanceMonitorModel;
import com.neil.core.jms.SendMessageCreator;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.core.JmsTemplate;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMonitor implements ApplicationListener<ContextRefreshedEvent> {
    private String serviceName;
    private String serviceRemark;
    private List<DataSource> dataSourceList;
    private JmsTemplate jmsTemplate;
    private int threadSleepMillionSeconds;

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceRemark(String serviceRemark) {
        this.serviceRemark = serviceRemark;
    }

    public void setDataSourceList(List<DataSource> dataSourceList) {
        this.dataSourceList = dataSourceList;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setThreadSleepMillionSeconds(int threadSleepMillionSeconds) {
        this.threadSleepMillionSeconds = threadSleepMillionSeconds;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Thread thread = new Thread(new PerformanceMonitorThread(serviceName, serviceRemark,
                    dataSourceList, jmsTemplate, threadSleepMillionSeconds));
            thread.start();
        }
    }
}

class PerformanceMonitorThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitorThread.class);
    private String serviceName;
    private String serviceRemark;
    private List<DataSource> dataSourceList;
    private JmsTemplate jmsTemplate;
    private int threadSleepMillionSeconds;

    public PerformanceMonitorThread(String serviceName, String serviceRemark, List<DataSource> dataSourceList,
                                    JmsTemplate jmsTemplate, int threadSleepMillionSeconds) {
        this.serviceName = serviceName;
        this.serviceRemark = serviceRemark;
        this.dataSourceList = dataSourceList;
        this.jmsTemplate = jmsTemplate;
        this.threadSleepMillionSeconds = threadSleepMillionSeconds;
    }

    @Override
    public void run() {
        List<String> list = new ArrayList<>();
        while (true) {
            try {
                list.clear();
                Thread.sleep(threadSleepMillionSeconds);
                if (dataSourceList != null && dataSourceList.size() > 0) {
                    for (DataSource dataSource : dataSourceList) {
                        PerformanceMonitorModel model = baseInfo();
                        PoolConfiguration poolConfiguration = dataSource.getPoolProperties();
                        if (poolConfiguration != null) {
                            model.setDriverClassName(poolConfiguration.getDriverClassName());
                            model.setUrl(poolConfiguration.getUrl());
                            model.setInitialSize(poolConfiguration.getInitialSize());
                            model.setMaxActive(poolConfiguration.getMaxActive());
                            model.setMaxIdle(poolConfiguration.getMaxIdle());
                            model.setMinIdle(poolConfiguration.getMinIdle());
                            model.setMaxWait(poolConfiguration.getMaxWait());
                            model.setUsername(poolConfiguration.getUsername());
                        }
                        ConnectionPool pool = dataSource.getPool();
                        if (pool != null) {
                            model.setPoolName(pool.getName());
                            model.setWaitCount(pool.getWaitCount());
                            model.setTotalSize(pool.getSize());
                            model.setBusy(pool.getActive());
                            model.setIdle(pool.getIdle());
                        }
                        list.add(JSON.toJSONString(model));
                    }
                } else {
                    PerformanceMonitorModel model = baseInfo();
                    list.add(JSON.toJSONString(model));
                }
                if (jmsTemplate != null) {
                    for (String str : list) {
                        jmsTemplate.send("service-performance-monitor-queue", new SendMessageCreator(str));
                    }
                } else {
                    log.warn("jmsTemplate is null");
                }
            } catch (Exception e) {
                log.error("服务监控程序发生异常：", e);
            }
        }
    }

    PerformanceMonitorModel baseInfo() throws Exception {
        PerformanceMonitorModel model = new PerformanceMonitorModel();
        model.setServiceName(serviceName);
        model.setServiceRemark(serviceRemark);

        double maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        double freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        double totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);

        model.setMaxMemory(maxMemory);
        model.setFreeMemory(freeMemory);
        model.setTotalMemory(totalMemory);
        model.setServerId(ManagementFactory.getRuntimeMXBean().getName());
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent())
            ;
        int totalThread = parentThread.activeCount();

        model.setTotalThread(totalThread);
        return model;
    }
}



