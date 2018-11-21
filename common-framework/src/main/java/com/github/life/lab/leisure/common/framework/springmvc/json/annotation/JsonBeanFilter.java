package com.github.life.lab.leisure.common.framework.springmvc.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * json 结果过滤，只对controller中方法有效
 * 拒绝优先原则，当 includes 和 excludes 都存在某一字段时，以 excludes 中为准。
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/13
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface JsonBeanFilter {

    Class<?> clazz();

    String[] includes() default {};

    String[] excludes() default {};
}
