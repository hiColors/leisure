package com.github.life.lab.leisure.common.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring 上下文工具类
 *
 * @author 李伟超
 * @date 2017/10/15
 */
public class SpringContextUtils implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextUtils.class);

    /**
     * Spring 应用上下文环境
     */
    private static ApplicationContext applicationContext;

    private static BeanDefinitionRegistry registry;

    private static ConfigurableListableBeanFactory beanFactory;

    public static void publish(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    public static void setRegistry(BeanDefinitionRegistry registry) {
        SpringContextUtils.registry = registry;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (ObjectUtils.isEmpty(SpringContextUtils.applicationContext)) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取对象
     *
     * @param name beanId
     * @return Object 一个以所给名字注册的bean的实例
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (BeansException e) {
            LOGGER.error(" BeanName:{} not exist，Exception => {}", name, e.getMessage());
        }
        return null;
    }

    /**
     * 获取类型为requiredType的对象 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     */
    public static synchronized <T> T getBean(String name, Class<T> requiredType) {
        try {
            return applicationContext.getBean(name, requiredType);
        } catch (NullPointerException | BeansException e) {
            LOGGER.error(" BeanName:{} not exist，Exception => {}", name, e.getMessage());

        }
        return null;
    }

    public static synchronized <T> T autowireBean(T existingBean) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
        return existingBean;
    }

    /**
     * 由spring容器初始化该对象
     *
     * @param <T>       泛型
     * @param beanClass 泛型 class
     * @param autoType  自动注入方式
     * @return T 对象
     * @see AutoType
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T autowire(Class<T> beanClass, AutoType autoType) {
        return (T) applicationContext.getAutowireCapableBeanFactory().autowire(beanClass, autoType.getValue(), false);
    }

    /**
     * spring 创建该Bean
     *
     * @param <T>       泛型
     * @param beanClass 泛型 class
     * @param autoType  自动注入方式
     * @return T 对象
     * @see AutoType
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T createBean(Class<T> beanClass, AutoType autoType) {
        return (T) applicationContext.getAutowireCapableBeanFactory().createBean(beanClass, autoType.getValue(), false);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name beanname
     * @return boolean
     */
    public static synchronized boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name beanname
     * @return boolean
     */
    public static synchronized boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name beanname
     * @return Class 注册对象的类型
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> Class<T> getType(String name) {
        return (Class<T>) applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name beanname
     * @return name aliases
     */
    public static synchronized String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }

    public static synchronized <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static synchronized <T> String[] getBeanNamesForType(Class<T> clazz) {
        return applicationContext.getBeanNamesForType(clazz);
    }

    public static <T> boolean containsBean(Class<T> clazz) {
        return applicationContext != null && getBeanNamesForType(clazz).length > 0;
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static synchronized Resource[] getResources(String pattern) {
        try {
            return applicationContext.getResources(pattern);
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage());
        }
        return new Resource[0];
    }

    public static boolean startup() {
        return applicationContext != null;
    }

    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    public static synchronized <T> T registerBeanDefinition(String beanName, Class<?> clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionBuilder.setAutowireMode(AutoType.AUTOWIRE_BY_TYPE.getValue());
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return getBean(beanName);
    }

    public static <T> T registerBeanDefinition(String beanName, Class<?> clazz, Object[] argValues) {
        return registerBeanDefinition(beanName, clazz, argValues, new ConcurrentHashMap<>(16));
    }

    public static <T> T registerBeanDefinition(String beanName, Class<?> clazz, Map<String, Object> propertyValues) {
        return registerBeanDefinition(beanName, clazz, new Object[0], propertyValues);
    }

    public static synchronized void removeBeanDefinition(String beanName) {
        registry.removeBeanDefinition(beanName);
    }

    public static synchronized <T> T registerBeanDefinition(String beanName, Class<?> clazz, Object[] argValues, Map<String, Object> propertyValues) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionBuilder.setAutowireMode(AutoType.AUTOWIRE_BY_TYPE.getValue());

        for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
            beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }

        for (Object argValue : argValues) {
            beanDefinitionBuilder.addConstructorArgValue(argValue);
        }
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return getBean(beanName);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        SpringContextUtils.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextUtils.beanFactory = beanFactory;
    }

    public void destroy() {
        cleanApplicationContext();
    }

    public enum AutoType {

        /**
         *
         */
        AUTOWIRE_NO(0),
        /**
         *
         */
        AUTOWIRE_BY_NAME(1),
        /**
         *
         */
        AUTOWIRE_BY_TYPE(2),
        /**
         *
         */
        AUTOWIRE_CONSTRUCTOR(3),
        /**
         *
         */
        AUTOWIRE_AUTODETECT(4);

        public int value;

        AutoType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

}
