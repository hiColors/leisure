package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.jpa.model.BaseJpaModel;

import javax.persistence.*;

/**
 * Person
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Table(name = "test")
@Entity
public class Person extends BaseJpaModel {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }
}
