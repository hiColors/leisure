package com.github.hicolors.leisure.common.framework.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LoggerConfiguration {

    @Value("${access.logger.exclude.uris:/favicon.ico;/swagger**/**;/health/**;/webjars/**}")
    private String excludeUris;

    @Bean
    public LoggerFilter loggerFilter() {
        List<String> excludePatterns = Arrays.asList(excludeUris.split(";"));
        return new LoggerFilter(excludePatterns);
    }
}