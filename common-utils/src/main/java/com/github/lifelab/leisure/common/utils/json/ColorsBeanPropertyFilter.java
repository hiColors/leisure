package com.github.lifelab.leisure.common.utils.json;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.github.lifelab.leisure.common.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * ColorsBeanPropertyFilter
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@JsonFilter("colorsBeanPropertyFilter")
public class ColorsBeanPropertyFilter extends SimpleBeanPropertyFilter {

    private Set<Class<?>> clazzs = Sets.newConcurrentHashSet();

    private Map<Class<?>, Set<String>> includeMap = Maps.newConcurrentMap();

    private Map<Class<?>, Set<String>> excludeMap = Maps.newConcurrentMap();

    public static Builder newBuilder(Class clazz) {
        return new Builder(clazz);
    }

    /**
     * 包含字段
     *
     * @param clazz
     * @param fields
     */
    public ColorsBeanPropertyFilter includes(Class<?> clazz, String... fields) {
        return addToMap(includeMap, clazz, fields);
    }

    /**
     * 过滤字段
     *
     * @param clazz
     * @param fields
     */
    public ColorsBeanPropertyFilter excludes(Class<?> clazz, String... fields) {
        return addToMap(excludeMap, clazz, fields);
    }

    private ColorsBeanPropertyFilter addToMap(Map<Class<?>, Set<String>> map, Class<?> clazz, String[] fields) {
        if (fields.length == 0) {
            return this;
        }
        clazzs.add(clazz);
        Set<String> fieldSet = map.getOrDefault(clazz, Sets.newHashSet());
        fieldSet.addAll(Lists.newArrayList(fields));
        map.put(clazz, fieldSet);
        return this;
    }

    private boolean check(Class<?> clazz, String name) {
        Set<String> includeFields = includeMap.get(clazz);
        Set<String> excludeFields = excludeMap.get(clazz);
        if (!CollectionUtils.isEmpty(excludeFields) && excludeFields.contains(name)) {
            return false;
        } else if (!CollectionUtils.isEmpty(includeFields) && includeFields.contains(name)) {
            return true;
        } else if (CollectionUtils.isEmpty(includeFields) && !CollectionUtils.isEmpty(excludeFields) && !excludeFields.contains(name)) {
            return true;
        } else {
            return includeFields == null && excludeFields == null;
        }
    }

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer) throws Exception {
        if (check(pojo.getClass(), writer.getName())) {
            writer.serializeAsField(pojo, jgen, prov);
        } else if (!jgen.canOmitFields()) {
            writer.serializeAsOmittedField(pojo, jgen, prov);
        }
    }

    public Builder mixin(Class<?> type) {
        return new Builder(this, type);
    }

    public static class Builder {

        private ColorsBeanPropertyFilter filter;

        private Class<?> currentClazz;

        public Builder(Class clazz) {
            this(new ColorsBeanPropertyFilter(), clazz);
        }

        public Builder(ColorsBeanPropertyFilter colorsBeanPropertyFilter, Class clazz) {
            this.filter = colorsBeanPropertyFilter;
            this.currentClazz = JsonUtils.minix(clazz);
        }

        public ColorsBeanPropertyFilter build() {
            return filter;
        }

        public Builder excludes(String... names) {
            if (names.length == 0) {
                return this;
            }
            filter.excludes(this.currentClazz, names);
            return this;
        }

        public Builder includes(String... names) {
            if (names.length == 0) {
                return this;
            }
            filter.includes(this.currentClazz, names);
            return this;
        }
    }

}