package com.github.life.lab.leisure.common.jpa.customiz.model;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页对象 request
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/02/06
 */
public class ColorsPageRequest extends AbstractPageRequest {

    private final Sort sort;


    public ColorsPageRequest() {
        this(1, 10, null);
    }

    public ColorsPageRequest(int page, int size) {
        this(page, size, null);
    }

    public ColorsPageRequest(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, new Sort(direction, properties));
    }


    public ColorsPageRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return PageRequest.of(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    public ColorsPageRequest previous() {
        return getPageNumber() == 0 ? this : new ColorsPageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override
    public Pageable first() {
        return PageRequest.of(0, getPageSize(), getSort());
    }

    @Override
    public String toString() {
        return String.format(
                "Page request [number: %d, size %d, sort: %s]",
                getPageNumber(),
                getPageSize(),
                sort == null ? null : sort.toString());
    }
}
