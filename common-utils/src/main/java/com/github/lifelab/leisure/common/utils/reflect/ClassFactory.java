package com.github.lifelab.leisure.common.utils.reflect;

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

    public static ColorsClassFactory getFastClassFactory() {
        return new FastClassFactory();
    }

}
