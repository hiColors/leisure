package com.github.hicolors.leisure.common.model;

import java.util.Date;

/**
 * BaseModel
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public interface BaseModel {

    Long getCreator();

    BaseModel setCreator(Long creator);

    Date getCreateTime();

    BaseModel setCreateTime(Date createTime);

    Long getModifier();

    BaseModel setModifier(Long modifier);

    Date getModifyTime();

    BaseModel setModifyTime(Date modifyTime);
}
