package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.utils.JSONUtils;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @PostMapping("/test")
    public String test(@RequestBody A a) {
        return JSONUtils.serializeExcludes(a);
    }

    @GetMapping("/test")
    public String test() {
        throw new ExtensionException(400,40L,"错误",null,null);
    }
}

@Data
class A {

    private Long id;
    private String name;
}