package com.github.lifelab.leisure.common.utils.reflect;

import com.github.lifelab.leisure.common.utils.ClassUtils;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FastClasses
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/6/9
 */
public class FastClasses<T> implements ColorsClass<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    private Class<T> clazz;

    private FastClass fastClass;

    private BeanInfo beanInfo;

    private Map<String, ObjectProperty> propertys = new ConcurrentHashMap<>();

    private Map<String, MethodProxy> methodProxys = new ConcurrentHashMap<>();

    private Map<Class<?>, Constructor<T>> constructors = new ConcurrentHashMap<>();

    private Map<String, Field> fields = new ConcurrentHashMap<>();

    private Map<String, Field> staticFields = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public FastClasses(Class<T> clazz) {
        this.clazz = clazz;
        this.fastClass = FastClass.create(clazz);
        if (!clazz.isInterface()) {
            this.beanInfo = ClassUtils.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = this.beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                MethodProxy readMethodProxy = descriptor.getReadMethod() == null ? null : new MethodProxy(descriptor.getReadMethod());
                MethodProxy writeMethodProxy = descriptor.getWriteMethod() == null ? null : new MethodProxy(descriptor.getWriteMethod(), descriptor.getPropertyType());
                this.propertys.put(descriptor.getName(), new ObjectProperty(descriptor.getName(), readMethodProxy, writeMethodProxy, descriptor.getPropertyType()));
            }
            for (Method method : this.clazz.getDeclaredMethods()) {
                Class<?>[] parameters = method.getParameterTypes();
                StringBuilder name = new StringBuilder(method.getName());
                if (parameters.length != 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> parameterType = parameters[i];
                        name.append(i == 0 ? "(" : "");
                        name.append(parameterType.getName());
                        name.append(i + 1 == parameters.length ? ")" : ",");
                    }
                } else {
                    name.append("()");
                }
                try {
                    if (method.isAccessible()) {
                        this.methodProxys.put(name.toString(), new MethodProxy(this.fastClass.getMethod(method), parameters));
                    } else {
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        this.methodProxys.put(name.toString(), new MethodProxy(method, parameters));
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            for (Constructor<?> constructor : clazz.getConstructors()) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 1) {
                    this.constructors.put(parameterTypes[0], (Constructor<T>) constructor);
                }
            }
        } else {
            for (Method method : clazz.getDeclaredMethods()) {
                StringBuilder name = new StringBuilder(method.getName());
                Class<?>[] parameters = method.getParameterTypes();
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> parameterType = parameters[i];
                    name.append(i == 0 ? "(" : "").append(parameterType.getName()).append(i + 1 == parameters.length ? ")" : ",");
                }
                this.methodProxys.put(name.toString(), new MethodProxy(this.fastClass.getMethod(method), parameters));
            }
        }
        for (Class<?> superClass = clazz; superClass != Object.class; ) {
            for (Field field : filterFields(superClass.getDeclaredFields())) {
                if (!this.fields.containsKey(field.getName())) {
                    this.fields.put(field.getName(), field);
                }
            }
            superClass = superClass.getSuperclass();
        }
    }

    private List<Field> filterFields(Field[] fields) {
        List<Field> result = new ArrayList<>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                result.add(field);
            } else {
                field.setAccessible(true);
                staticFields.put(field.getName(), field);
            }
        }
        return result;
    }

    @Override
    public T newInstance() {
        try {
            return this.clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public T newInstance(Object object) {
        try {
            if (object == null) {
                return newInstance();
            }
            return newInstance(object.getClass(), object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T newInstance(Class<?> type, Object object) {
        try {
            return this.constructors.get(type).newInstance(new Object[]{object});
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ObjectProperty getProperty(String name) {
        if (this.propertys.containsKey(name)) {
            return this.propertys.get(name);
        }
        return null;
    }

    @Override
    public ObjectProperty[] getPropertys() {
        return this.propertys.values().toArray(new ObjectProperty[this.propertys.size()]);
    }

    @Override
    public MethodProxy getMethod(String methodName) {
        MethodProxy methodProxy = this.methodProxys.get(methodName + "()");
        if (org.apache.commons.lang3.ObjectUtils.allNotNull(methodProxy)) {
            return methodProxy;
        }
        for (Map.Entry<String, MethodProxy> entry : this.methodProxys.entrySet()) {
            if (entry.getKey().equals(methodName) || entry.getKey().startsWith(methodName + "(")) {
                return entry.getValue();
            }
        }
        if (this.clazz.getSuperclass() != Object.class) {
            return ClassUtils.getMethodProxy(this.clazz.getSuperclass(), methodName);
        }
        return null;
    }

    @Override
    public MethodProxy getMethod(String methodName, Class<?>... parameterTypes) {
        StringBuilder methodname = new StringBuilder(methodName);
        if (parameterTypes.length != 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                methodname.append(i == 0 ? "(" : "").append(parameterTypes[i].getName()).append(i + 1 == parameterTypes.length ? ")" : ",");
            }
        } else {
            methodname.append("()");
        }
        return this.methodProxys.get(methodname.toString());
    }

    @Override
    public void setValue(Object target, String name, Object value) {
        Field field = this.fields.get(name);
        try {
            if (field != null) {
                field.set(target, value);
            } else {
                throw new RuntimeException(String.format("没有找到[%s.%s]对应的属性!", clazz.getName(), name));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public <V> V getValue(Object target, String name) {
        try {
            return getValue(target, this.fields.get(name));
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new RuntimeException(String.format("没有找到[%s.%s]对应的属性!", clazz.getName(), name));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <V> V getValue(Object target, Field field) throws IllegalAccessException, NoSuchFieldException {
        if (field == null) {
            throw new NoSuchFieldException("字段不存在");
        }
        return (V) field.get(target);
    }

    @Override
    public T newInstance(Class<?>[] parameterTypes, Object[] parameters) {
        if (parameterTypes.length == 0) {
            return newInstance();
        }
        if (parameterTypes.length == 1) {
            return newInstance(parameterTypes[0], parameters[0]);
        }
        throw new RuntimeException("还不支持多个参数的构造器");
    }

    @Override
    public Field[] getDeclaredFields() {
        return this.fields.values().toArray(new Field[this.fields.size()]);
    }

    @Override
    public Field getDeclaredField(String fieldName) {
        return this.fields.get(fieldName);
    }

    @Override
    public Field[] getDeclaredFields(Class<? extends Annotation> annotClass) {
        List<Field> retvalues = new ArrayList<>();
        for (Field field : this.fields.values()) {
            if (field.isAnnotationPresent(annotClass)) {
                retvalues.add(field);
            }
        }
        return retvalues.toArray(new Field[retvalues.size()]);
    }

    @Override
    public <V> V getValue(String name) {
        try {
            return this.getValue(null, this.staticFields.get(name));
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new RuntimeException(String.format("没有找到[%s.%s]对应的属性!", clazz.getName(), name));
        }

    }

}