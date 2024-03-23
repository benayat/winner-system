package org.benaya.ai.winnersystem.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
