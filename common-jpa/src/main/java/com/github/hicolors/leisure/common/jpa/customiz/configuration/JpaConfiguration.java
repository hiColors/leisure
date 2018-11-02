package com.github.hicolors.leisure.common.jpa.customiz.configuration;

import com.github.hicolors.leisure.common.jpa.customiz.interceptor.BizInterceptor;
import com.github.hicolors.leisure.common.jpa.customiz.repository.ColorsComplexRepository;
import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Jpa 配置类
 *
 * @author 李伟超
 * @date 2018/01/09
 */
@Configuration
@EnableJpaRepositories(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {JpaRepository.class}
                )
        },
        basePackages = "com.github.hicolors.leisure.common.jpa",
        repositoryBaseClass = ColorsComplexRepository.class
)
public class JpaConfiguration {
    @Bean
    public Interceptor bizInterceptor() {
        return new BizInterceptor();
    }
}