package com.github.lifelab.leisure.common.jpa.customiz.exception;

import com.github.lifelab.leisure.common.exception.BusinessException;
import com.github.lifelab.leisure.common.exception.ExtensionException;
import com.github.lifelab.leisure.common.exception.consts.HttpStatus;

/**
 * JPA 异常
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class CommonJpaException extends BusinessException {

    public CommonJpaException(EnumExceptionMessageJpa e) {
        super(e.getCode(), e.getMessage());
    }

    public CommonJpaException(EnumExceptionMessageJpa e, Object o) {
        super(e.getCode(), e.getMessage(), o);
    }
}