package com.github.hicolors.leisure.common.utils;

/**
 * IClassFactory
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/6/9
 */
public interface IClassFactory {

    <T> IClass<T> getClass(Class<T> paramClass);

}