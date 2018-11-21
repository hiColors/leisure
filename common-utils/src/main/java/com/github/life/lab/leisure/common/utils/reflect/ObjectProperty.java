package com.github.life.lab.leisure.common.utils.reflect;


import com.github.life.lab.leisure.common.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * 属性信息
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class ObjectProperty {

    private String name;
    private MethodProxy readMethodProxy;
    private MethodProxy writeMethodProxy;
    private Class propertyType;
    private boolean write;
    private boolean read;
    private Map<Class, Annotation> annotationCache = new HashMap<>();

    ObjectProperty(String name, MethodProxy readMethodProxy, MethodProxy writeMethodProxy, Class<?> propertyType) {
        this.read = readMethodProxy != null;
        this.write = writeMethodProxy != null;
        this.name = name;
        this.readMethodProxy = readMethodProxy;
        this.writeMethodProxy = writeMethodProxy;
        this.propertyType = propertyType;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isRead() {
        return this.read;
    }

    public Object getValue(Object target) {
        if (!this.read) {
            return null;
        }
        return this.readMethodProxy.invoke(target);
    }

    public void setValue(Object target, Object value) {
        if (!this.write) {
            return;
        }
        this.writeMethodProxy.invoke(target, value);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getPropertyType() {
        return this.propertyType;
    }

    public String getName() {
        return this.name;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        if (annotationCache.containsKey(tClass)) {
            return (T) annotationCache.get(tClass);
        }
        Annotation annotation = null;
        Class<?> declaringClass = null;
        if (this.isRead()) {
            annotation = this.getReadMethod().getAnnotation(tClass);
            if (annotation == null) {
                declaringClass = this.getReadMethod().getDeclaringClass();
            }
        }
        if (annotation == null && this.isWrite()) {
            annotation = this.getWriteMethod().getAnnotation(tClass);
            if (annotation == null) {
                declaringClass = this.getWriteMethod().getDeclaringClass();
            }
        }
        if (annotation == null) {
            Field field = ClassUtils.getDeclaredField(declaringClass, this.name);
            if (field != null) {
                annotation = field.getAnnotation(tClass);
            }
        }
        annotationCache.put(tClass, annotation);
        return (T) annotation;
    }

    public MethodProxy getReadMethod() {
        return this.readMethodProxy;
    }

    public MethodProxy getWriteMethod() {
        return this.writeMethodProxy;
    }

    public ParameterizedType getGenericType() {
        return (ParameterizedType) this.getReadMethod().getMethod().getGenericReturnType();
    }
}
