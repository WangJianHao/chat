package com.sen.chat.chatserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 线程池配置
 * @author: sensen
 * @date: 2023/6/31 20:46
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {

    private static final int CORE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    private static final int MAX_THREAD_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;

    private static final int WORK_QUEUE_CAPACITY = 1000;

    private static final int KEEP_ALIVE_SECONDS = 60;
    /**
     * 项目共用线程池
     */
    public static final String COMMON_EXECUTOR = "commonExecutor";
    /**
     * websocket通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return commonExecutor();
    }

    @Bean(COMMON_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_THREAD_SIZE);
        executor.setMaxPoolSize(MAX_THREAD_SIZE);
        executor.setQueueCapacity(WORK_QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix("common-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.initialize();
        return executor;
    }

    @Bean(WS_EXECUTOR)
    public ThreadPoolTaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_THREAD_SIZE);
        executor.setMaxPoolSize(MAX_THREAD_SIZE);
        executor.setQueueCapacity(WORK_QUEUE_CAPACITY);//支持同时推送1000人
        executor.setThreadNamePrefix("websocket-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//满了直接丢弃，默认为不重要消息推送
        executor.initialize();
        return executor;
    }
}
