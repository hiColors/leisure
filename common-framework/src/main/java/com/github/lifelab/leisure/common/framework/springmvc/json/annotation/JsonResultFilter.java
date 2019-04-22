package com.github.lifelab.leisure.common.framework.springmvc.json.annotation;

import java.lang.annotation.*;

/**
 * json 过滤注解，当有这个注解时生效
 * 过滤规则详情请参看 JsonBeanFilter
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/13
 * @see JsonBeanFilter
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResultFilter {

    JsonBeanFilter[] values();

}
