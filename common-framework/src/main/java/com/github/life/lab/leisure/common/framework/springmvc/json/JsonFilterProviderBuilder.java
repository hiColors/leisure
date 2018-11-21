package com.github.life.lab.leisure.common.framework.springmvc.json;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.life.lab.leisure.common.framework.springmvc.json.annotation.JsonBeanFilter;
import com.github.life.lab.leisure.common.framework.springmvc.json.annotation.JsonResultFilter;
import com.github.life.lab.leisure.common.utils.json.ColorsBeanPropertyFilter;
import com.google.common.collect.Maps;
import org.springframework.core.MethodParameter;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * Json Filter 构建器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/13
 */
public class JsonFilterProviderBuilder {

    private static final String DEFAULT_PROVIDER_KEY = "default_provider_key";

    private static final ConcurrentMap<String, FilterProvider> PROVIDERS = Maps.newConcurrentMap();

    static {
        PROVIDERS.put(DEFAULT_PROVIDER_KEY, new SimpleFilterProvider().setFailOnUnknownId(false));
    }

    private static FilterProvider getFilterProvider(JsonResultFilter jsonResultFilters) {
        String key = jsonResultFilters.toString();
        if (PROVIDERS.containsKey(key)) {
            return PROVIDERS.get(key);
        }
        SimpleFilterProvider provider = new SimpleFilterProvider().setFailOnUnknownId(false);
        ColorsBeanPropertyFilter propertyFilter = new ColorsBeanPropertyFilter();
        for (JsonBeanFilter filter : jsonResultFilters.values()) {
            propertyFilter.mixin(filter.clazz()).includes(filter.includes()).excludes(filter.excludes());
        }
        provider.setDefaultFilter(propertyFilter);
        PROVIDERS.putIfAbsent(key, provider);
        return provider;
    }

    public static FilterProvider getFilterProvider(MethodParameter methodParameter) {
        JsonResultFilter jsonResultFilter = Objects.requireNonNull(methodParameter.getMethod()).getAnnotation(JsonResultFilter.class);
        if (jsonResultFilter == null) {
            return PROVIDERS.get(DEFAULT_PROVIDER_KEY);
        }
        return getFilterProvider(jsonResultFilter);
    }

}
