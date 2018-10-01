package com.github.hicolors.leisure.common.example.b.controller;

import com.github.hicolors.leisure.common.example.b.bean.Person;
import com.github.hicolors.leisure.common.example.b.bean.XXX;
import com.github.hicolors.leisure.common.example.b.repository.PersonRepository;
import com.github.hicolors.leisure.common.exception.BusinessException;
import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TestController
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private PersonRepository repository;

    @GetMapping
    public List<Person> testExpression(List<ColorsExpression> filters) {
        throw new BusinessException(1L, "xxx", "xxx");
//        log.info(filters.toString());
//        return repository.findAll(filters);
    }

    @GetMapping("/test")
    public XXX testObject(XXX person) {
        return person;
    }

    @PostMapping("/json")
    public String json(@RequestBody Person person) {
        return JsonUtils.serializeExcludes(person);
    }

    @GetMapping("/error")
    public Person error() {
        throw new RuntimeException("xxxxx");
    }
//
//    @GetMapping
//    @JsonResultFilter(
//            values = @JsonBeanFilter(clazz = Person.class, excludes = {"id"})
//    )
//    public Person test() {
//        return new Person(new Class(1L, "一班"), 1L, "liweichao");
//    }
}