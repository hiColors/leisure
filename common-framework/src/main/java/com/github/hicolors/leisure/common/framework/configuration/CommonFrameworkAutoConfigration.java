package com.github.hicolors.leisure.common.framework.configuration;

import com.github.hicolors.leisure.common.framework.utils.EnvHelper;
import com.github.hicolors.leisure.common.framework.utils.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * CommonFrameworkAutoConfigration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Configuration
@ComponentScan(
        basePackages = {"com.github.hicolors.leisure.common.framework"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {RestControllerAdvice.class})
        }
)
public class CommonFrameworkAutoConfigration {

    @Bean
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }

    @Bean
    public EnvHelper envHelper() {
        return new EnvHelper();
    }

}