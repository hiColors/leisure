package com.github.hicolors.leisure.common.example.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ExampleApplication
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients(basePackages = "com.github.hicolors.leisure.common.example")
public class ExampleZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleZuulApplication.class, args);
    }

}
