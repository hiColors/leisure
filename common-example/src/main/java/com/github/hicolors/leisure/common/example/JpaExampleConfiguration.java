package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.jpa.customiz.repository.ColorsComplexRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                JpaRepository.class
                        }
                )
        },
        basePackages = "com.github.hicolors.leisure.common.example",
        repositoryBaseClass = ColorsComplexRepository.class
)
/**
 * JpaExampleConfiguration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class JpaExampleConfiguration {
}