package com.github.hicolors.leisure.common.example.a.controller;

import com.github.hicolors.leisure.common.example.a.bean.Person;
import com.github.hicolors.leisure.common.example.a.bean.XXX;
import com.github.hicolors.leisure.common.example.a.repository.PersonRepository;
import com.github.hicolors.leisure.common.example.a.sdk.ExampleBClient;
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

    @Autowired
    private ExampleBClient client;

    @GetMapping
    public List<Person> testExpression(List<ColorsExpression> filters) {
        XXX xx = client.testObject(new XXX(1L, "2"));
        log.info(JsonUtils.serialize(xx));
        return client.testExpression(filters);
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