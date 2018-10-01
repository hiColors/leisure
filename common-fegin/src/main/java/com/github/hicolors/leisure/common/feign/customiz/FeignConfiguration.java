package com.github.hicolors.leisure.common.feign.customiz;


import com.github.hicolors.leisure.common.feign.customiz.code.ColorsErrorDecoder;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Feign 配置
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
@Configuration
public class FeignConfiguration {

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

}