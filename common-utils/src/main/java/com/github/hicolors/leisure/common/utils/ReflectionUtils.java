package com.github.hicolors.leisure.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * ReflectionUtils
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public final class ReflectionUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    private static ConvertUtilsBean convertUtils = BeanUtilsBean2.getInstance().getConvertUtils();

    static {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(new String[]{
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd",
                "yyyyMMdd",
                "yyyy-MM",
                "yyyyMMddHHmmss",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",

        });
        convertUtils.register(dc, Date.class);
    }

    public static Object invokeGetterMethod(Object target, String propertyName) throws InvocationTargetException, IllegalAccessException {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(target, getterMethodName, new Class[0], new Object[0]);
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value) throws InvocationTargetException, IllegalAccessException {
        invokeSetterMethod(target, propertyName, value, null);
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(target, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new RuntimeException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        T result = null;
        try {
            result = (T) field.get(object);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常{}", e.getMessage(), e);
        }
        return result;
    }

    public static Class getFieldDeclaringClass(Object object, String fieldName) {
        return Objects.requireNonNull(getDeclaredField(object, fieldName)).getDeclaringClass();
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new RuntimeException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常:{}", e.getMessage(), e);
        }
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws InvocationTargetException, IllegalAccessException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new RuntimeException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        return method.invoke(object, parameters);


    }

    protected static Field getDeclaredField(Object object, String fieldName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; ) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException localNoSuchFieldException) {
                LOGGER.debug(localNoSuchFieldException.getMessage());
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    protected static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers()))) {
            field.setAccessible(true);
        }
    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; ) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException localNoSuchMethodException) {
                LOGGER.error(localNoSuchMethodException.getMessage(), localNoSuchMethodException);
                superClass = superClass.getSuperclass();
            }

        }
        return null;
    }

//    @SuppressWarnings("unchecked")
//    public static <T> Class<T> getSuperClassGenricType(Class clazz) {
//        return (Class<T>) getSuperClassGenricType(clazz, 0);
//    }
//
//    public static Class getInterfaceGenricType(Class clazz, Class interfaceClazz) {
//        return getInterfaceGenricType(clazz, interfaceClazz, 0);
//    }

//    @SuppressWarnings("unchecked")
//    public static <T> Class<T> getInterfaceGenricType(Class clazz, Class interfaceClazz, int index) {
//        return ClassUtils.getInterfaceGenricType(clazz, interfaceClazz, index);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> Class<T> getSuperClassGenricType(Class clazz, int index) {
//        return ClassUtils.getSuperClassGenricType(clazz, index);
//    }

    public static List<Object> convertElementPropertyToList(Collection<Object> collection, String propertyName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Object> list = new ArrayList<Object>();
        for (Iterator<?> i = collection.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            list.add(PropertyUtils.getProperty(obj, propertyName));
        }
        return list;
    }

    public static String convertElementPropertyToString(Collection<Object> collection, String propertyName, String separator) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Object> list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static <T> T convertStringToObject(String value, Class<T> toType) {
        return toType.cast(convertUtils.convert(value, toType));
    }

    public static <T> T convert(Object value, Class<T> toType) {
        return toType.cast(convertUtils.convert(value, toType));
    }


}