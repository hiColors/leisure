package com.github.hicolors.leisure.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.hicolors.leisure.common.utils.json.ColorsBeanPropertyFilter;
import com.github.hicolors.leisure.common.utils.json.DateDeserializer;
import com.github.hicolors.leisure.common.utils.json.DateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * JSONUtils
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/8
 */
public class JSONUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        //驼峰转下划线
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                //为空的字段不序列化
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 当找不到对应的序列化器时 忽略此字段
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 允许非空字段
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                // 允许单引号
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                // 转义字符异常
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
                // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 默认日期转换方式
                .registerModule(new SimpleModule()
                        .addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss.SSS"))
                        .addDeserializer(Date.class, new DateDeserializer()));
    }


    public static String serialize(Object object) {
        return serialize(object, builder -> builder);
    }

    public static String serializeExcludes(Object object, String... excludes) {
        return serialize(object, builder -> builder.excludes(excludes));
    }

    public static String serializeIncludes(Object object, String... includes) {
        return serialize(object, builder -> builder.includes(includes));
    }

    public static String serializeFilterProvider(Object object, FilterProvider filterProvider) {
        try {
            return objectMapper.writer(filterProvider).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private static String serialize(Object object, FilterLoader filterLoader) {
        if (Objects.isNull(object)) { return ""; }
        try {
            return objectMapper.writer(new SimpleFilterProvider().setFailOnUnknownId(false).setDefaultFilter(filterLoader.setup(ColorsBeanPropertyFilter.newBuilder(object.getClass())).build())).writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }


    public static JsonNode deserialize(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public static <T> T[] deserialize(String json, T[] classed) {
        try {
            return (T[]) objectMapper.readValue(json, classed.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        try {
            return (T) objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public static Class minix(Class<?> clazz) {
        if (objectMapper.findMixInClassFor(clazz) == null) {
            objectMapper.addMixIn(clazz, ColorsBeanPropertyFilter.class);
        }
        return clazz;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public interface FilterLoader {
        /**
         * 装载 json filter
         *
         * @param builder
         * @return ColorsBeanPropertyFilter.Builder
         */
        ColorsBeanPropertyFilter.Builder setup(ColorsBeanPropertyFilter.Builder builder);
    }

}
