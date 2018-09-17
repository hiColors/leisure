package com.github.hicolors.leisure.common.rocketmq.properties;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RocketMqProducerProperties
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
@Data
public class RocketMqProducerProperties {

    private String defaultProducer;

    private Map<String, String> topicProducer = new ConcurrentHashMap<>();
}
