package com.github.hicolors.leisure.common.framework.configuration;

import com.github.hicolors.leisure.common.framework.utils.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * CommonFrameworkAutoConfigration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Configuration
@ComponentScan(basePackages = {"com.github.hicolors.leisure.common.framework"})
public class CommonFrameworkAutoConfigration {

    @Bean
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }


}