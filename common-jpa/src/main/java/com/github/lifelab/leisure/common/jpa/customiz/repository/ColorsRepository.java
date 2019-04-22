package com.github.lifelab.leisure.common.jpa.customiz.repository;

import com.github.lifelab.leisure.common.jpa.customiz.model.Pager;
import com.github.lifelab.leisure.common.model.expression.ColorsExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Colors Repository
 *
 * @author 李伟超
 * @date 2018/01/10
 */
@NoRepositoryBean
public interface ColorsRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 分页查询
     *
     * @param pager
     * @param filters
     * @return
     */
    Pager<T> findPage(Pager<T> pager, List<ColorsExpression> filters);

    /**
     * 分页查询
     *
     * @param pageable
     * @param filters
     * @return
     */
    Page<T> findPage(Pageable pageable, List<ColorsExpression> filters);

    /**
     * 列表查询
     *
     * @param filters
     * @return
     */
    List<T> findAll(List<ColorsExpression> filters);

}
