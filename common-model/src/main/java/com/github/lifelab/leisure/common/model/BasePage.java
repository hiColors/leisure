package com.github.lifelab.leisure.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Page
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-01-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页结果模型")
public class BasePage<T> {
    /**
     * 结果集
     */
    @ApiModelProperty("结果集")
    private List<T> content;

    /**
     * 总元素数量
     */
    @ApiModelProperty("总元素数量")
    private Long totalElements;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Long totalPages;

    /**
     * 每页大小
     */
    @ApiModelProperty("每页大小")
    private Long size;

    /**
     * 当前页数量
     */
    @ApiModelProperty("当前页数量")
    private Long numberOfElements;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Long number;

    /**
     * 是否最后一页
     */
    @ApiModelProperty("是否最后一页")
    private boolean last;

    /**
     * 是否首页
     */
    @ApiModelProperty("是否首页")
    private Boolean first;

}
