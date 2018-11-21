package com.github.life.lab.leisure.common.exception;

/**
 * ResourceNotFoundException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public class ResourceNotFoundException extends ExtensionException {

    private static final Long CODE = 404L;


    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), CODE, message, null, null);
    }
}