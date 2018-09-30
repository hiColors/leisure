package com.github.hicolors.leisure.common.example.b.sdk;

import com.github.hicolors.leisure.common.example.b.bean.Person;
import com.github.hicolors.leisure.common.model.expression.ColorsExpression;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * ExampleAClient
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/30
 */
@FeignClient(name = "example-a", url = "${url.example-a}")
@RequestMapping("/test")
public interface ExampleAClient {
    /**
     * 测试
     *
     * @param filters
     * @return
     */
    @GetMapping
    List<Person> testExpression(List<ColorsExpression> filters);
}