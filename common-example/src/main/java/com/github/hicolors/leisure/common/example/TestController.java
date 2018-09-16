package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.framework.springmvc.json.annotation.JsonBeanFilter;
import com.github.hicolors.leisure.common.framework.springmvc.json.annotation.JsonResultFilter;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import org.springframework.web.bind.annotation.*;

/**
 * TestController
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@RestController
@RequestMapping("/test")
public class TestController {

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