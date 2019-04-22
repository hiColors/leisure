package com.github.lifelab.leisure.common.metrics.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CommonMetricsAutoConfigration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Configuration
public class CommonMetricsAutoConfigration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}