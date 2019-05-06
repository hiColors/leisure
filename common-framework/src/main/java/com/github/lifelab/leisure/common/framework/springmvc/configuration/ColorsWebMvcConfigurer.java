package com.github.lifelab.leisure.common.framework.springmvc.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.lifelab.leisure.common.framework.springmvc.advice.AbstractExceptionHandlerAdvice;
import com.github.lifelab.leisure.common.framework.springmvc.advice.DefaultExceptionHandlerAdvice;
import com.github.lifelab.leisure.common.framework.springmvc.method.ColorsExpressionArgumentResolver;
import com.github.lifelab.leisure.common.model.expression.ColorsExpression;
import com.github.lifelab.leisure.common.model.expression.ColorsExpressionJsonSerializer;
import com.github.lifelab.leisure.common.utils.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


/**
 * ColorsWebMvcConfigurer
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@EnableWebMvc
@EnableAspectJAutoProxy
@Configuration
@Order(value = ColorsWebMvcConfigurer.ORDER)
public class ColorsWebMvcConfigurer implements WebMvcConfigurer {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 32;


    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "HEAD", "PATCH", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true)
                .maxAge(3600L);
    }

    /**
     * 允许参数路径后带 .
     *
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    /**
     * 内容协商-取消路径后缀的内容协商
     * <p>
     * <p>
     * configurer.favorPathExtension(false) //禁用路径扩展
     * .favorParameter(false)
     * .ignoreAcceptHeader(true) //禁用 accept header
     * .useJaf(false) //不要使用JAF，而是手动地指定媒体类型映射
     * .defaultContentType(MediaType.APPLICATION_JSON);
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    /**
     * 针对 swagger 的特殊处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ColorsExpressionArgumentResolver());
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        objectMapper.registerModule(new SimpleModule().addSerializer(ColorsExpression.class, new ColorsExpressionJsonSerializer()));
        return objectMapper;
    }

    @ConditionalOnMissingBean(AbstractExceptionHandlerAdvice.class)
    @Bean
    public DefaultExceptionHandlerAdvice defaultExceptionHandlerAdvice() {
        return new DefaultExceptionHandlerAdvice();
    }
}