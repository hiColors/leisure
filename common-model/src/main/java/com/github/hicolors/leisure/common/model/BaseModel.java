package com.github.hicolors.leisure.common.model;

import lombok.Data;

import java.util.Date;

/**
 * BaseModel
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Data
public class BaseModel {

    /**
     * 创建人
     */
    protected Long creator;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 最后修改人
     */
    protected Long modifier;

    /**
     * 最后修改时间
     */
    protected Date modifyTime;
}
