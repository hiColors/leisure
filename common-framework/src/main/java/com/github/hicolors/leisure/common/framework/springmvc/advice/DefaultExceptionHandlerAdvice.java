package com.github.hicolors.leisure.common.framework.springmvc.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * DefaultExceptionHandlerAdvice
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/11/14
 */
@RestControllerAdvice
public class DefaultExceptionHandlerAdvice extends AbstractExceptionHandlerAdvice {
}