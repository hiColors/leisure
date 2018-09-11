package com.github.hicolors.leisure.common.exception;

import lombok.Getter;

/**
 * ExtensionException
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Getter
public class ExtensionException extends RuntimeException {
    /**
     * Http Response 状态码
     */
    private Integer status;

    /**
     * 业务异常码 ( 详情参加文档说明 )
     */
    private Long code;

    /**
     * 业务异常信息
     */
    private String message;

    /**
     * 业务异常数据
     */
    private Object data;

    /**
     * The throwable that caused this throwable to get thrown, or null if this
     * throwable was not caused by another throwable, or if the causative
     * throwable is unknown.  If this field is equal to this throwable itself,
     * it indicates that the cause of this throwable has not yet been
     * initialized.
     */
    private Throwable cause;

    public ExtensionException(Integer status, Long code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.cause = cause;
    }

}