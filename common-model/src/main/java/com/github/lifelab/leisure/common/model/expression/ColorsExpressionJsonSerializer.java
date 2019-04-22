package com.github.lifelab.leisure.common.model.expression;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ColorsExpressionJsonSerializer
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/30
 */
public class ColorsExpressionJsonSerializer extends JsonSerializer<ColorsExpression> {
    @Override
    public void serialize(ColorsExpression ce, JsonGenerator jg, SerializerProvider serializerProvider) throws IOException {
        jg.writeObject(toMap(ce));
    }

    private Map<String, String> toMap(ColorsExpression ce) {
        Map<String, String> maps = new HashMap<>(2);
        maps.put(ce.getFilterName(), StringUtils.join(ce.getFilterValues(), ","));
        return maps;
    }
}
