package com.example.demo.config;

import com.gobrs.async.core.threadpool.GobrsAsyncThreadPoolFactory;
import com.gobrs.async.core.threadpool.GobrsThreadPoolConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig extends GobrsThreadPoolConfiguration {

    @Override
    protected void doInitialize(GobrsAsyncThreadPoolFactory factory) {
        /**
         * 自定义线程池
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue());

//          ExecutorService executorService = Executors.newCachedThreadPool();
        factory.setDefaultThreadPoolExecutor(threadPoolExecutor);
        //  factory.setThreadPoolExecutor("ruleName",threadPoolExecutor); // 设置规则隔离的线程池 ruleName 为 yml中配置的规则名称
    }
}

