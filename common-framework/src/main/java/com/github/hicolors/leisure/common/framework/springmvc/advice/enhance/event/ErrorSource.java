package com.github.hicolors.leisure.common.framework.springmvc.advice.enhance.event;

import com.github.hicolors.leisure.common.framework.springmvc.response.ErrorResponse;

/**
 * 错误事件源
 *
 * @author 李伟超
 * @date 2017/10/29
 */
public class ErrorSource {

    private ErrorResponse error;

    private Object data;

    public ErrorSource(ErrorResponse error, Object data) {
        this.error = error;
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public Object getData() {
        return data;
    }
}