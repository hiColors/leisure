package com.github.lifelab.leisure.common.jpa.customiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页对象
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/7/5
 */
public class Pager<T> {

    /**
     * 排序 - 升序
     */
    public static final String SORT_ASC = "asc";
    /**
     * 排序 - 降序
     */
    public static final String SORT_DESC = "desc";
    /**
     * 最大数据条数
     */
    private int count = 0;
    /**
     * 每页显示的数据条数
     */
    private int size = 10;
    /**
     * 总页数
     */
    private int total = 1;
    /**
     * 当前页码
     */
    private int current = 1;
    /**
     * 开始数据索引
     */
    private int first = 0;
    /**
     * 排序字段
     */
    @JsonProperty("sort")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String order;

    private transient List<T> content;


    public Pager() {
    }

    public Pager(Pager pager) {
        this.current = pager.current;
        this.size = pager.size;
        this.count = pager.count;
        this.total = pager.total;
        this.orderBy = pager.orderBy;
        this.order = pager.order;
    }

    public Pager(Pager pager, List<T> content) {
        this.current = pager.current;
        this.size = pager.size;
        this.count = pager.count;
        this.total = pager.total;
        this.orderBy = pager.orderBy;
        this.order = pager.order;
        this.content = content;
    }

    /**
     * 获取总页码
     *
     * @return 总页数
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取每页显示的条数
     *
     * @return 每页显示条数
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置每页显示数据的条数
     *
     * @param size 每页显示数据条数
     */
    public void setSize(int size) {
        this.size = size;
    }

    public int getFirst() {
        return first;
    }

    /**
     * 返回翻页开始位置
     *
     * @param first 数据开始位置
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * 获取当前显示的页码
     *
     * @return current
     */
    public int getCurrent() {
        return current <= 0 ? 1 : current;
    }

    /**
     * 设置显示的页码 注意是页码
     *
     * @param current 当前页码
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * 获取数据总条数
     *
     * @return totalCount
     */
    public int getCount() {
        return count;
    }

    public List<T> getContent() {
        return content;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        if (StringUtils.isNotBlank(this.getOrderBy()) && StringUtils.isBlank(this.order)) {
            this.setOrder(SORT_ASC);
        }
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 是否启用排序
     *
     * @return boolean
     */
    @JsonIgnore
    public boolean isOrderBySetted() {
        return StringUtils.isNotBlank(this.getOrderBy()) && StringUtils.isNotBlank(this.getOrder());
    }

    /**
     * 设置总数据条数
     *
     * @param count 总数据条数
     */
    public void reset(int count) {
        this.count = count;
        this.total = count % size == 0 ? count / size : count / size + 1;
        if (current >= total) {
            setCurrent(total);
            setFirst((total - 1) * size);
        } else if (current <= 0) {
            setCurrent(1);
            setFirst(first);
        } else {
            setFirst((current - 1) * size);
        }
    }

    public void sort(String orderBy, String order) {
        this.orderBy = orderBy;
        this.order = order;
    }

    public void reset(List<T> content) {
        this.content = content;
    }

    public void reset(int totalCount, List<T> items) {
        this.reset(totalCount);
        this.reset(items);
    }

    public PageRequest trans2PageRequest() {
        if (StringUtils.isNotBlank(this.getOrder()) && StringUtils.isNotBlank(this.getOrderBy())) {
            Sort.Direction direction = Sort.Direction.DESC;
            if (SORT_ASC.equals(this.getOrder())) {
                direction = Sort.Direction.ASC;
            }
            return PageRequest.of(this.getCurrent() - 1, this.getSize(), new Sort(direction, getOrderBy()));
        }
        return PageRequest.of(this.getCurrent() - 1, this.getSize());
    }
}
