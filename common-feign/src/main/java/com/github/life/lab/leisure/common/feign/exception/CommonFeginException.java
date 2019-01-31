package com.github.life.lab.leisure.common.feign.exception;

import com.github.life.lab.leisure.common.exception.ExtensionException;
import com.github.life.lab.leisure.common.exception.HttpStatus;

/**
 * CommonFeginException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-01-31
 */
public class CommonFeginException extends ExtensionException {

    public CommonFeginException(Long code, String message) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), code, message, null, null);
    }
}
