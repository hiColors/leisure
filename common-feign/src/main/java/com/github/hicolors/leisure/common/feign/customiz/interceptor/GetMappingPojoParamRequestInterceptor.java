package com.github.hicolors.leisure.common.feign.customiz.interceptor;

import com.github.hicolors.leisure.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Feign Client Get 请求 参数构造器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/30
 */
@Component
public class GetMappingPojoParamRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (template.method().equals(HttpMethod.GET.name()) && template.body() != null) {
            String json = new String(template.body());
            template.body(null);
            Map<String, Collection<String>> queries = JsonUtils.buildMap(json);
            template.queries(queries);
        }
    }
}