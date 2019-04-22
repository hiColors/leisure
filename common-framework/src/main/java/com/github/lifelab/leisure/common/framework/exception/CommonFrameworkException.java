package com.github.lifelab.leisure.common.framework.exception;

import com.github.lifelab.leisure.common.exception.BusinessException;

/**
 * CommonFeignException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-01-31
 */
public class CommonFrameworkException extends BusinessException {

    public CommonFrameworkException(EnumExceptionMessageFramework e) {
        super(e.getCode(), e.getMessage());
    }

    public CommonFrameworkException(EnumExceptionMessageFramework e, Object o) {
        super(e.getCode(), e.getMessage(), o);
    }
}