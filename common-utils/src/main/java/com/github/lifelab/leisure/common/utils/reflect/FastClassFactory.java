package com.github.lifelab.leisure.common.utils.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FastClassFactory
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class FastClassFactory implements ColorsClassFactory {
    private Map<String, ColorsClass<?>> classes = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> ColorsClass<T> getClass(Class<T> cla) {
        if (!this.classes.containsKey(cla.getName())) {
            this.classes.put(cla.getName(), new FastClasses(cla));
        }
        return (ColorsClass<T>) this.classes.get(cla.getName());
    }
}