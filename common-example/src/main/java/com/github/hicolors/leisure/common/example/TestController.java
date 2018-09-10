package com.github.hicolors.leisure.common.example;

import com.github.hicolors.leisure.common.exception.BusinessException;
import com.github.hicolors.leisure.common.utils.JSONUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping
    public String test(@RequestBody Person person) {
        return JSONUtils.serializeExcludes(person);
    }

    @GetMapping
    public String test() {
        throw new BusinessException(40L, "错误", null);
    }
}