package com.github.lifelab.leisure.common.model.validator;

/**
 * 参数校验分组信息 常量
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/15
 */
public interface ValidatorGroup {

    /**
     * 查询
     */
    interface Get {
    }

    /**
     * 创建
     */
    interface Post {
    }

    /**
     * 全量修改
     */
    interface Put {
    }

    /**
     * 部分修改
     */
    interface Patch {
    }

    /**
     * 删除
     */
    interface Delete {
    }

}