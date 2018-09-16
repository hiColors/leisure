package com.github.hicolors.leisure.common.framework.springmvc.advice.enhance.event;

import com.github.hicolors.leisure.common.framework.springmvc.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误事件源
 *
 * @author 李伟超
 * @date 2017/10/29
 */
@Getter
@AllArgsConstructor
public class ErrorSource {

    private ErrorResponse error;

    private Object data;

}