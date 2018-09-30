package com.github.hicolors.leisure.common.example.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ExampleApplication
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.github.hicolors")
public class ExampleAApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleAApplication.class, args);
    }


}
