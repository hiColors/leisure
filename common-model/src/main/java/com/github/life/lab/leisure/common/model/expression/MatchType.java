package com.github.life.lab.leisure.common.model.expression;

import java.util.regex.Pattern;

/**
 * MatchType
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public enum MatchType {
    /**
     * 等于
     */
    EQ(false),
    /**
     * 模糊查询
     */
    LIKE(false),
    /**
     * 小于
     */
    LT(false),
    /**
     * 大于
     */
    GT(false),
    /**
     * 小于等于
     */
    LE(false),
    /**
     * 大于等于
     */
    GE(false),
    /**
     * in
     */
    IN(true),
    /**
     * not in
     */
    NOTIN(true),
    /**
     * 不等于
     */
    NE(false),
    /**
     * is null
     */
    NULL,
    /**
     * not null
     */
    NOTNULL,
    /**
     * empty
     */
    EMPTY,
    /**
     * not empty
     */
    NOTEMPTY,
    /**
     * between
     */
    BETWEEN(false),
    /**
     * where sql
     */
    SQL(false);
    /**
     * 是否存在参数
     */
    private boolean existParams;
    /**
     * 是否有多个参数
     */
    private boolean multiParams;

    MatchType() {
        this(true, false);
    }

    MatchType(boolean multiParams) {
        this(false, multiParams);
    }

    MatchType(boolean existParams, boolean multiParams) {
        this.existParams = existParams;
        this.multiParams = multiParams;
    }

    public static MatchType get(String param) {
        for (MatchType matchType : values()) {
            if (Pattern.compile("^" + matchType.name()).matcher(param).find()) {
                return matchType;
            }
        }
        return null;
    }

    public static boolean exits(String str) {
        return get(str) != null;
    }

    public boolean isExistParams() {
        return existParams;
    }

    public MatchType setExistParams(boolean existParams) {
        this.existParams = existParams;
        return this;
    }

    public boolean isMultiParams() {
        return multiParams;
    }

    public MatchType setMultiParams(boolean multiParams) {
        this.multiParams = multiParams;
        return this;
    }
}
