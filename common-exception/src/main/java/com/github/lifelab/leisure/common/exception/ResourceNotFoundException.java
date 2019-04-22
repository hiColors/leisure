package com.github.lifelab.leisure.common.exception;

import com.github.lifelab.leisure.common.exception.consts.HttpStatus;

/**
 * ResourceNotFoundException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public class ResourceNotFoundException extends ExtensionException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), (long) HttpStatus.NOT_FOUND.value(), message, null, null);
    }
}