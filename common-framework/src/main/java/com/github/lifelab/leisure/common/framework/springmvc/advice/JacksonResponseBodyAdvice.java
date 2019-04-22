package com.github.lifelab.leisure.common.framework.springmvc.advice;

import com.github.lifelab.leisure.common.exception.ResourceNotFoundException;
import com.github.lifelab.leisure.common.framework.springmvc.json.JsonFilterProviderBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * JacksonResponseBodyAdvice
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 7)
@ControllerAdvice
public class JacksonResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Class returnType = Objects.requireNonNull(methodParameter.getMethod()).getReturnType();
        return Object.class.isAssignableFrom(returnType);
    }

    @Override
    public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (ClassUtils.isPrimitiveOrWrapper(Objects.requireNonNull(methodParameter.getMethod()).getReturnType())) {
            return returnValue;
        } else if (returnValue == null && StringUtils.equalsIgnoreCase(Objects.requireNonNull(serverHttpRequest.getMethod()).name(), HttpMethod.GET.name())) {
            throw new ResourceNotFoundException("The object of the query does not exist!");
        } else if (mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(returnValue);
            mappingJacksonValue.setFilters(JsonFilterProviderBuilder.getFilterProvider(methodParameter));
            return mappingJacksonValue;
        }
        return returnValue;
    }

}