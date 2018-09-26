package com.github.hicolors.leisure.common.example.a.repository;

import com.github.hicolors.leisure.common.example.a.bean.Person;
import com.github.hicolors.leisure.common.jpa.customiz.repository.ColorsRepository;
import org.springframework.stereotype.Repository;

/**
 * PersonRepository
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
@Repository
public interface PersonRepository extends ColorsRepository<Person, Long> {
}
