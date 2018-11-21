package com.github.life.lab.leisure.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.life.lab.leisure.common.model.validator.ValidatorGroup;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@MappedSuperclass
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Override
    public Long getCreator() {
        return creator;
    }

    @Override
    public BaseJpaModel setCreator(Long creator) {
        this.creator = creator;
        return this;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public BaseJpaModel setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public Long getModifier() {
        return modifier;
    }

    @Override
    public BaseJpaModel setModifier(Long modifier) {
        this.modifier = modifier;
        return this;
    }

    @Override
    public Date getModifyTime() {
        return modifyTime;
    }

    @Override
    public BaseJpaModel setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    @Override
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    @Override
    public BaseJpaModel setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
        return this;
    }
}