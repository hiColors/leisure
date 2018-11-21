package com.github.life.lab.leisure.common.exception;

/**
 * 业务异常
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/10
 */
public class BusinessException extends ExtensionException {
    public BusinessException(Long code, String message, Object data) {
        super(HttpStatus.BAD_REQUEST.value(), code, message, data, null);
    }
}
