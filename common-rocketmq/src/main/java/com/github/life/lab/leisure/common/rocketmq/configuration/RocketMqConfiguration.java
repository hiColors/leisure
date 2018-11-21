package com.github.life.lab.leisure.common.rocketmq.configuration;

import com.github.life.lab.leisure.common.rocketmq.consumer.RocketMqConsumer;
import com.github.life.lab.leisure.common.rocketmq.consumer.RocketMqConsumerHolder;
import com.github.life.lab.leisure.common.rocketmq.consumer.enhance.ConsumeFailEnhance;
import com.github.life.lab.leisure.common.rocketmq.producer.DefaultRocketMqProducer;
import com.github.life.lab.leisure.common.rocketmq.producer.RocketMqProducer;
import com.github.life.lab.leisure.common.rocketmq.producer.enhance.ProduceFailEnhance;
import com.github.life.lab.leisure.common.rocketmq.properties.RocketMqProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RocketMqConfiguration
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/26
 */
@Configuration
@EnableConfigurationProperties({
        RocketMqProperties.class,
})
public class RocketMqConfiguration {

    @Autowired
    private RocketMqProperties rocketMqProperties;

    @Autowired(required = false)
    private List<RocketMqConsumer> consumers;

    @Autowired(required = false)
    private List<ConsumeFailEnhance> consumeFailEnhances;

    @Autowired(required = false)
    private List<ProduceFailEnhance> produceFailEnhances;

    @Bean
    @ConditionalOnProperty(name = "rocket.producer.defaultProducer")
    public RocketMqProducer defaultRocketMqProducer() {
        return new DefaultRocketMqProducer(rocketMqProperties, CollectionUtils.isNotEmpty(produceFailEnhances) ? produceFailEnhances : ListUtils.EMPTY_LIST);
    }

    @Bean
    @ConditionalOnBean(RocketMqConsumer.class)
    public RocketMqConsumerHolder rocketMqConsumerHolder() {
        return new RocketMqConsumerHolder(rocketMqProperties, consumers, CollectionUtils.isNotEmpty(consumeFailEnhances) ? consumeFailEnhances : ListUtils.EMPTY_LIST);
    }

}