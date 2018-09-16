package com.github.hicolors.leisure.common.utils;

/**
 * Class Factory
 *
 * @author 李伟超
 * @email liweichao0102@gmail.com
 * @date 2018/1/10
 */
public class ClassFactory {

    private ClassFactory() {
    }

    static IClassFactory getFastClassFactory() {
        return new FastClassFactory();
    }

}
