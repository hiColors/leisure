package com.github.hicolors.leisure.common.example.a.sdk;

import com.github.hicolors.leisure.common.example.a.bean.Person;
import com.github.hicolors.leisure.common.example.a.bean.XXX;
import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * ExampleBClient
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/30
 */
@FeignClient(name = "example-b", url = "${url.example-b}")
@RequestMapping("/test")
public interface ExampleBClient {

    /**
     * 测试
     *
     * @param filters
     * @return
     */
    @GetMapping
    List<Person> testExpression(List<ColorsExpression> filters);

    /**
     * 测试
     *
     * @param xxx
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    XXX testObject(XXX xxx);
}