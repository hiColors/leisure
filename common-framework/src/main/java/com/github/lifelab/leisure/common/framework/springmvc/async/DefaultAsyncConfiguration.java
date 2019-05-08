package com.github.lifelab.leisure.common.framework.springmvc.async;

import brave.Tracer;
import com.github.lifelab.leisure.common.framework.utils.EnvHelper;
import com.github.lifelab.leisure.common.framework.warning.WarningService;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * DefaultAsyncConfiguration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-05-06
 */
@Configuration
@EnableAsync
public class DefaultAsyncConfiguration implements AsyncConfigurer {

    @Autowired
    private WarningService dingTalkWarningService;

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new DingtalkAsyncUncaughtExceptionHandler(dingTalkWarningService);
    }
}
