package com.github.hicolors.leisure.common.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Person
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;
    private String name;
}
