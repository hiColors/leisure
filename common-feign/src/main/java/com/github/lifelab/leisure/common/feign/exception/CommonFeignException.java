package com.github.lifelab.leisure.common.feign.exception;

import com.github.lifelab.leisure.common.exception.ExtensionException;
import com.github.lifelab.leisure.common.exception.consts.HttpStatus;

/**
 * CommonFeginException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-01-31
 */
public class CommonFeignException extends ExtensionException {

    public CommonFeignException(EnumExceptionMessageFeign e) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getCode(), e.getMessage(), null, null);
    }
}
