package com.github.hicolors.leisure.common.feign.customiz.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class GetPojoParamRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // feign 不支持 GET 方法传 POJO, json body转query
        if (template.method().equals(HttpMethod.GET.name()) && template.body() != null) {
            try {
                JsonNode jsonNode = JsonUtils.getObjectMapper().readTree(template.body());
                template.body(null);

                Map<String, Collection<String>> queries = new HashMap<>();
                buildQuery(jsonNode, "", queries);
                template.queries(queries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {
        // 叶子节点
        if (!jsonNode.isContainerNode()) {
            if (jsonNode.isNull()) {
                return;
            }
            Collection<String> values = queries.get(path);
            if (null == values) {
                values = new ArrayList<>();
                queries.put(path, values);
            }
            values.add(jsonNode.asText());
            return;
        }
        // 数组节点
        if (jsonNode.isArray()) {
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), path, queries);
            }
        } else {
            Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (StringUtils.isNotBlank(path)) {
                    buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);

                }
                // 根节点
                else {
                    buildQuery(entry.getValue(), entry.getKey(), queries);
                }
            }
        }
    }
}