package org.benaya.ai.winnersystem.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class SchedulersConfig {
    @Bean(name = "concurrentExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor simpleExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(2);
        threadPoolTaskExecutor.setQueueCapacity(30);
        threadPoolTaskExecutor.setThreadNamePrefix("concurrent-");
        return threadPoolTaskExecutor;
    }

    @Bean(name = "concurrentVirtualThreadExecutor")
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor(@Qualifier("concurrentExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor(threadPoolTaskExecutor);
        executor.setVirtualThreads(true);
        executor.setThreadNamePrefix("concurrent-vt-");
        return executor;
    }

    @Bean(name = "blockingExecutor", destroyMethod = "shutdown")
    public ScheduledExecutorService blockingExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean("virtualThreadsAsyncScheduler")
    public SimpleAsyncTaskScheduler simpleAsyncTaskScheduler(@Qualifier("concurrentExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        SimpleAsyncTaskScheduler scheduler = new SimpleAsyncTaskScheduler();
        scheduler.setTargetTaskExecutor(threadPoolTaskExecutor);
        scheduler.setVirtualThreads(true);
        scheduler.setThreadNamePrefix("concurrent-scheduler-");
        return scheduler;
    }

    @Bean(destroyMethod = "shutdown")
    @Primary
    public ThreadPoolTaskExecutor primaryThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("thread-");
        executor.initialize();
        return executor;
    }
}
