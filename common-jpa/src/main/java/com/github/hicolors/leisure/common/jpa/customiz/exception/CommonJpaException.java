package com.github.hicolors.leisure.common.jpa.customiz.exception;

import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.exception.HttpStatus;

/**
 * JPA 异常
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class CommonJpaException extends ExtensionException {

    public CommonJpaException(Long code, String message, Object data, Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), code, message, data, cause);
    }
}
