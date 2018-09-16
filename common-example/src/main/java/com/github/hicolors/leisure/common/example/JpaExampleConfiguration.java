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
public class JpaExampleConfiguration {
}