package com.github.lifelab.leisure.common.framework.swagger;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.MessageFormat;
import java.util.List;

/**
 * SwaggerConfiguration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

    @Value("${spring.application.name:leisure}")
    private String applicationName;

    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * swagger 默认配置，如果服务有自定义会覆盖此 bean。
     *
     * @return
     */

    @Autowired(required = false)
    private List<Parameter> parameters;

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerProperties.getStatus())
                .apiInfo(new ApiInfoBuilder()
                        .title(MessageFormat.format(swaggerProperties.getTitle(), applicationName))
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(CollectionUtils.isEmpty(parameters) ? Lists.newArrayList() : parameters);
    }
}