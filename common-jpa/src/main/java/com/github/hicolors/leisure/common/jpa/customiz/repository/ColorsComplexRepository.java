package com.github.hicolors.leisure.common.jpa.customiz.repository;

import com.github.hicolors.leisure.common.jpa.customiz.expression.ColorsSpecification;
import com.github.hicolors.leisure.common.jpa.customiz.model.Pager;
import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Colors 自定义 Jpa Repository
 *
 * @author 李伟超
 * @date 2018/01/10
 */
public class ColorsComplexRepository<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements ColorsRepository<T, ID> {

    @Autowired(required = false)
    public ColorsComplexRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pager<T> findPage(Pager<T> pager, List<ColorsExpression> filters) {
        Page<T> page = this.findAll(new ColorsSpecification(filters), pager.trans2PageRequest());
        pager.reset(Long.valueOf(page.getTotalElements()).intValue(), page.getContent());
        return pager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findPage(Pageable pageable, List<ColorsExpression> filters) {
        return this.findAll(new ColorsSpecification(filters), pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(List<ColorsExpression> filters) {
        return this.findAll(new ColorsSpecification(filters));
    }
}