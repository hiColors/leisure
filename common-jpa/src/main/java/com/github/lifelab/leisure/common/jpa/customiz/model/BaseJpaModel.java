package com.github.lifelab.leisure.common.jpa.customiz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.lifelab.leisure.common.model.BaseModel;
import com.github.lifelab.leisure.common.model.validator.ValidatorGroup;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * model 抽象
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/15
 */
@Data
@MappedSuperclass
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "delete_flag"})
public abstract class BaseJpaModel implements BaseModel {

    /**
     * 创建人
     */
    @Null(message = "创建人必须为空",
            groups = {
                    ValidatorGroup.Put.class,
                    ValidatorGroup.Patch.class
            })
    @Column(updatable = false, name = "creator", length = 20)
    protected Long creator;

    /**
     * 创建时间
     */
    @Null(message = "创建时间必须为空",
            groups = {
                    ValidatorGroup.Post.class,
                    ValidatorGroup.Put.class,
                    ValidatorGroup.Patch.class
            })
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "create_time")
    protected Date createTime;

    /**
     * 最后修改人
     */
    @Null(message = "修改人必须为空",
            groups = {
                    ValidatorGroup.Post.class
            })
    @Column(name = "modifier", length = 20)
    protected Long modifier;

    /**
     * 最后修改时间
     */
    @Null(message = "修改时间必须为空",
            groups = {
                    ValidatorGroup.Post.class,
                    ValidatorGroup.Put.class,
                    ValidatorGroup.Patch.class
            })
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    protected Date modifyTime;

    @Null(message = "删除标志位 必须为空", groups = {
            ValidatorGroup.Get.class,
            ValidatorGroup.Post.class,
            ValidatorGroup.Put.class,
            ValidatorGroup.Patch.class,
            ValidatorGroup.Delete.class
    })
    @Column(name = "delete_flag")
    protected Boolean deleteFlag;

}