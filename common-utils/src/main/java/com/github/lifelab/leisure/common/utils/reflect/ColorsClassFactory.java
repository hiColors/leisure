package com.github.lifelab.leisure.common.utils.reflect;

/**
 * ColorsClass 工厂
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/6/9
 */
public interface ColorsClassFactory {

    /**
     * 获取包装类型
     *
     * @param paramClass
     * @param <T>
     * @return
     */
    <T> ColorsClass<T> getClass(Class<T> paramClass);

}