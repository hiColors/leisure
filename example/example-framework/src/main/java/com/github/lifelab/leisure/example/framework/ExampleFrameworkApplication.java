package com.github.lifelab.leisure.example.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FrameworkApplication
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-04-22
 */
@SpringBootApplication
@Slf4j
@RestController("/test")
public class ExampleFrameworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleFrameworkApplication.class, args);
    }

    @GetMapping("/test404")
    public String test404(){
        return null;
    }

    @GetMapping("/test500")
    public String test500(){
        throw new RuntimeException("500");
    }
}
