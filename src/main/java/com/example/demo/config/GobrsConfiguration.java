package com.example.demo.config;

import com.gobrs.async.core.autoconfig.GobrsPropertyAutoConfiguration;
import com.gobrs.async.core.config.GobrsAsyncRule;
import com.gobrs.async.core.config.GobrsConfig;
import com.gobrs.async.core.property.GobrsAsyncProperties;
import com.gobrs.async.core.property.LogConfig;
import com.gobrs.async.core.property.RuleConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = "com.gobrs.async.core.autoconfig", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GobrsPropertyAutoConfiguration.class))
public class GobrsConfiguration{
    @Bean
    public GobrsConfig gobrsConfig(GobrsAsyncProperties properties) {

        GobrsConfig gobrsConfig = new GobrsConfig();
        gobrsConfig.setEnable(properties.isEnable());
        gobrsConfig.setSplit(properties.getSplit());
        gobrsConfig.setPoint(properties.getPoint());
        gobrsConfig.setParamContext(gobrsConfig.isParamContext());
        gobrsConfig.setTimeout(properties.getTimeout());
        gobrsConfig.setRelyDepend(properties.isRelyDepend());
        gobrsConfig.setTimeoutCoreSize(properties.getTimeoutCoreSize());
        GobrsConfig.ThreadPool assignThreadPool = threadPool(properties.getThreadPool());
        if (Objects.nonNull(assignThreadPool)) {
            gobrsConfig.setThreadPool(assignThreadPool);
        }
        if (properties.getRules() == null) {
            properties.setRules(new ArrayList());
        }
        List<RuleConfig> rules = properties.getRules();
        List<GobrsAsyncRule> rList = rules.stream().map(x -> {
            GobrsAsyncRule r = new GobrsAsyncRule();
            LogConfig logConfig = x.getLogConfig();
            if (Objects.nonNull(logConfig)) {
                r.setErrLogabled(logConfig.getErrLogabled());
                r.setCostLogabled(logConfig.getCostLogabled());
            }

            GobrsAsyncProperties.ThreadPool threadPool = x.getThreadPool();
            if (Objects.nonNull(threadPool)) {
                GobrsConfig.ThreadPool asThreadPool = threadPool(threadPool);
                if (Objects.nonNull(asThreadPool)) {
                    r.setThreadPool(asThreadPool);
                }
            }
            r.setCatchable(x.isCatchable());
            r.setName(x.getName());
            r.setContent(x.getContent());
            r.setTaskInterrupt(x.isTaskInterrupt());
            r.setTransaction(x.isTransaction());
            r.setInterruptionImmediate(x.isInterruptionImmediate());
            return r;
        }).collect(Collectors.toList());
        gobrsConfig.setRules(rList);
        return gobrsConfig;
    }

    /**
     *
     */
    private GobrsConfig.ThreadPool threadPool(GobrsAsyncProperties.ThreadPool threadPool) {
        if (Objects.nonNull(threadPool)) {
            GobrsConfig.ThreadPool tp = new GobrsConfig.ThreadPool();
            tp.setCorePoolSize(threadPool.getCorePoolSize());
            tp.setMaxPoolSize(threadPool.getMaxPoolSize());
            tp.setKeepAliveTime(threadPool.getKeepAliveTime());
            tp.setTimeUnit(threadPool.getTimeUnit());
            tp.setExecuteTimeOut(threadPool.getExecuteTimeOut());
            tp.setCapacity(threadPool.getCapacity());
            tp.setWorkQueue(threadPool.getWorkQueue());
            tp.setThreadNamePrefix(threadPool.getThreadNamePrefix());
            tp.setAllowCoreThreadTimeOut(threadPool.getAllowCoreThreadTimeOut());
            tp.setRejectedExecutionHandler(threadPool.getRejectedExecutionHandler());
            return tp;
        }
        return null;
    }
}
