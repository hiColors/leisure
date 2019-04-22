package com.github.lifelab.leisure.common.jpa.customiz.exception;

import lombok.Getter;

/**
 * FeignMessageEunm
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-04-22
 */
@Getter
public enum EnumExceptionMessageJpa {

    // 公共异常
    UNEXPECTED_ERROR(10006000L, "unexpected exception, please contact the administrator to resolve"),
    PARAM_VALIDATED_UN_PASS(10006001L, "the data entered is not legal. see the errors field for details.");

    private final Long code;

    private final String message;

    EnumExceptionMessageJpa(Long code, String message) {
        this.code = code;
        this.message = message;
    }


}