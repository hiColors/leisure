package com.github.life.lab.leisure.common.framework.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

/**
 * EnvUtils
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/11/3
 */
public class EnvHelper {

    @Autowired
    private Environment env;

    @Value("${env.property.name:spring.profiles.active}")
    private String envProperty;

    public String getEnv() {
        return env.getProperty(envProperty);
    }

}
