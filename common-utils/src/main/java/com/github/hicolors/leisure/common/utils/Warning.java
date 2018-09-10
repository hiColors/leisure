package com.github.hicolors.leisure.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 预警消息
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/30
 */
@Data
@AllArgsConstructor
public class Warning {

    /**
     * 标题
     */
    private String title;

    /**
     * trace id
     */

    private String traceId;

    /**
     * 业务
     */
    private String business;

    /**
     * 方法
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String methodParams;

    /**
     * 额外参数
     */
    private Map<String, Object> extraInfo;

    /**
     * 发生时间
     */
    private Date date;

    /**
     * 异常信息
     */
    private String exceptionMsg;
}