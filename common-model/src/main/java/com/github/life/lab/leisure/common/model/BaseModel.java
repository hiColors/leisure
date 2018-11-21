package com.github.life.lab.leisure.common.model;

import com.github.life.lab.leisure.common.utils.json.JsonResultFilterSupport;

import java.util.Date;

/**
 * BaseModel
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public interface BaseModel extends JsonResultFilterSupport {
    /**
     * 创建人
     *
     * @return
     */
    Long getCreator();

    /**
     * 创建人
     *
     * @param creator
     * @return
     */
    BaseModel setCreator(Long creator);

    /**
     * 创建时间
     *
     * @return
     */
    Date getCreateTime();

    /**
     * 创建时间
     *
     * @param createTime
     * @return
     */
    BaseModel setCreateTime(Date createTime);

    /**
     * 修改人
     *
     * @return
     */
    Long getModifier();

    /**
     * 修改人
     *
     * @param modifier
     * @return
     */
    BaseModel setModifier(Long modifier);

    /**
     * 修改时间
     *
     * @return
     */
    Date getModifyTime();

    /**
     * 修改时间
     *
     * @param modifyTime
     * @return
     */
    BaseModel setModifyTime(Date modifyTime);

    /**
     * 删除标志位
     *
     * @return
     */
    Boolean getDeleteFlag();

    /**
     * 删除标志位
     *
     * @param deleteFlag
     * @return
     */
    BaseModel setDeleteFlag(Boolean deleteFlag);
}
