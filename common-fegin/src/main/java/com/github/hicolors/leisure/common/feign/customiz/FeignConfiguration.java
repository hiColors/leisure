package com.github.hicolors.leisure.common.feign.customiz;


import com.github.hicolors.leisure.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Feign 配置
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
@Configuration
public class FeignConfiguration {

    private static final String SPLIT_CHARACTER = ";";

    @Value("feign.penetrate.headers:user-agent")
    private String penetrateHeaders;

    @Bean
    @ConditionalOnMissingBean(Decoder.class)
    public Decoder feignDecoder() {
        HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter(JsonUtils.getObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    @ConditionalOnMissingBean(Encoder.class)
    public Encoder feignEncoder() {
        HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter(JsonUtils.getObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringEncoder(objectFactory);
    }

    @Bean
    @ConditionalOnMissingBean(ErrorDecoder.class)
    public ErrorDecoder errorDecoder() {
        return new ColorsErrorDecoder();
    }


    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override
                    protected boolean isHandler(Class<?> beanType) {
                        boolean flag = AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
                        return super.isHandler(beanType) && !flag;
                    }
                };
            }
        };
    }

    @Bean
    public RequestInterceptor headerInterceptor() {
        List<String> headerList = Arrays.asList(penetrateHeaders.split(SPLIT_CHARACTER));
        Set<String> headers = new HashSet<>();
        headerList.forEach(e -> headers.add(e.toLowerCase()));
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    for (String header : headers) {
                        if (StringUtils.equalsIgnoreCase(header, name)) {
                            requestTemplate.header(name, values);
                            break;
                        }
                    }
                }
            }
        };
    }

}
