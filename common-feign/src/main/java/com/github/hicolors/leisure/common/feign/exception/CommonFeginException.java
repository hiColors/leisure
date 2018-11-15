package com.github.hicolors.leisure.common.feign.exception;

import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.exception.HttpStatus;

public class CommonFeginException extends ExtensionException {

    public CommonFeginException(Long code, String message) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), code, message, null, null);
    }
}
