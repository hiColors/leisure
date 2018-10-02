package com.github.hicolors.leisure.common.example.b.bean;

import com.github.hicolors.leisure.common.model.BaseJpaModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Person
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Table(name = "test")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person extends BaseJpaModel {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

}
