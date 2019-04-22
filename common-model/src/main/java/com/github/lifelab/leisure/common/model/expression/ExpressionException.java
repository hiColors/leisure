package com.github.lifelab.leisure.common.model.expression;

/**
 * 自定义 jpa 表达式异常
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class ExpressionException extends IllegalArgumentException {

    public ExpressionException(String msg) {
        super(msg);
    }

    public ExpressionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
