package com.github.lifelab.leisure.common.feign.exception;

import lombok.Getter;

/**
 * FeignMessageEunm
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-04-22
 */
@Getter
public enum EnumExceptionMessageFeign {

    // feign 公共异常
    REMOTE_INVOKE_ERROR(10006001L, "feign client execute error,reasons please check log!");

    private final Long code;

    private final String message;

    EnumExceptionMessageFeign(Long code, String message) {
        this.code = code;
        this.message = message;
    }


}