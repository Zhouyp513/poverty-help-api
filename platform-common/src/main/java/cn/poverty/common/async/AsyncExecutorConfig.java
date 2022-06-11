package cn.poverty.common.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 
 * @date 创建时间 2018/6/9
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncExecutorConfig implements AsyncConfigurer {


    /**
     * 配置完成后@Async注解就会使用该线程池，有需要的话可以在getAsyncExecutor()方法添加@Bean("线程池名字")注解指定线程池名字，最后在@Async("线程池名字")使用。
     * 配置拿到线程池
     * 
     * @date 2020-05-21
     * @param
     * @return
     */
    @Override
    @Bean("threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        log.info(">>>>>>>>>>>>>>>>>>>start asyncServiceExecutor<<<<<<<<<<<<<<<<<<");
        //使用VisibleThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(200);
        //配置最大线程数
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        //配置队列大小
        executor.setQueueCapacity(50);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-task-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    /*@Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        log.info("start asyncServiceExecutor");
        //使用VisiableThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(200);
        //配置最大线程数
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        //配置队列大小
        executor.setQueueCapacity(50);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }*/


}
