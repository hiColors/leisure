package com.github.lifelab.leisure.common.exception;

import com.github.lifelab.leisure.common.exception.consts.HttpStatus;

/**
 * 业务异常
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/10
 */
public class BusinessException extends ExtensionException {

    public BusinessException(Long code, String message) {
        super(HttpStatus.BAD_REQUEST.value(), code, message, null, null);
    }

    public BusinessException(Long code, String message, Object data) {
        super(HttpStatus.BAD_REQUEST.value(), code, message, data, null);
    }

}