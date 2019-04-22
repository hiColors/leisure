package com.github.lifelab.leisure.common.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 类 包装器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public interface ColorsClass<T> {

    /**
     * 获取属性相关信息
     *
     * @param paramName
     * @return
     */
    ObjectProperty getProperty(String paramName);

    /**
     * 获取所有属性相关信息
     *
     * @return
     */
    ObjectProperty[] getPropertys();

    /**
     * 创建新的实例
     *
     * @return
     */
    T newInstance();

    /**
     * 根据参数创建新的实例
     *
     * @param paramObject
     * @return
     */
    T newInstance(Object paramObject);

    /**
     * 根据参数创建新的实例
     *
     * @param paramClass
     * @param paramObject
     * @return
     */
    T newInstance(Class<?> paramClass, Object paramObject);

    /**
     * 获取方法
     *
     * @param paramString
     * @return
     */
    MethodProxy getMethod(String paramString);

    /**
     * 获取方法
     *
     * @param paramString
     * @param paramArrayOfClass
     * @return
     */
    MethodProxy getMethod(String paramString, Class<?>[] paramArrayOfClass);

    /**
     * 设置值
     *
     * @param paramObject1
     * @param paramString
     * @param paramObject2
     */
    void setValue(Object paramObject1, String paramString, Object paramObject2);

    /**
     * 获取值
     *
     * @param paramObject
     * @param paramString
     * @param <V>
     * @return
     */
    <V> V getValue(Object paramObject, String paramString);

    /**
     * 创建新实例
     *
     * @param paramArrayOfClass
     * @param paramArrayOfObject
     * @return
     */
    T newInstance(Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject);

    /**
     * 获取属性
     *
     * @return
     */
    Field[] getDeclaredFields();

    /**
     * 获取属性
     *
     * @param fieldName
     * @return
     */
    Field getDeclaredField(String fieldName);

    /**
     * 获取属性
     *
     * @param annotClass
     * @return
     */
    Field[] getDeclaredFields(Class<? extends Annotation> annotClass);

    /**
     * 获取值
     *
     * @param name
     * @param <V>
     * @return
     */
    <V> V getValue(String name);
}