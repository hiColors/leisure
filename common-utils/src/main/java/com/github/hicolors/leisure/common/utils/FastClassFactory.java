package com.github.hicolors.leisure.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FastClassFactory implements IClassFactory {
    private Map<String, IClass<?>> classes = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> IClass<T> getClass(Class<T> cla) {
        if (!this.classes.containsKey(cla.getName())) {
            this.classes.put(cla.getName(), new FastClasses(cla));
        }
        return (IClass<T>) this.classes.get(cla.getName());
    }
}