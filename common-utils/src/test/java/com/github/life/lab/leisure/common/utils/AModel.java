package com.github.life.lab.leisure.common.utils;

public class AModel {
    private Long id;

    private String name;

    private Integer age;

    public Long getId() {
        return id;
    }

    public AModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AModel setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AModel setAge(Integer age) {
        this.age = age;
        return this;
    }
}
