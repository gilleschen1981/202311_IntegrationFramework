package com.opentext.itom.ucmdb.integration.push.framework;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {
    @Value("${push.threadpool.size}")
    private int pushThreadPoolSize;

    @Value("${push.batch.size}")
    private int pushBatchSize;
    @Value("${push.schedule.interval}")
    private int pushScheduleInterval;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(pushThreadPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("PushThread - ");
        executor.initialize();
        return executor;
    }

    public int getPushThreadPoolSize() {
        return pushThreadPoolSize;
    }

    public int getPushBatchSize() {
        return pushBatchSize;
    }

    public int getPushScheduleInterval() {
        return pushScheduleInterval;
    }
}
