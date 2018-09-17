package com.github.hicolors.leisure.common.rocketmq.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RocketMqProperties
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
@Data
@ConfigurationProperties(prefix = "rocket")
public class RocketMqProperties {

    private String accessKey;

    private String secretKey;

    private String nameServerAddress;

    private String dingtalk;

    private RocketMqProducerProperties producer;

}