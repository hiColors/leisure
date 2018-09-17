package com.github.hicolors.leisure.common.rocketmq.consumer;


import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.github.hicolors.leisure.common.rocketmq.consumer.enhance.ConsumeFailEnhance;
import com.github.hicolors.leisure.common.rocketmq.properties.RocketMqProperties;
import com.github.hicolors.leisure.common.utils.ByteArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

/**
 * 阿里云 ONS 消费者管理器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/6
 */
@Slf4j
public class RocketMqConsumerHolder {

    private List<ConsumeFailEnhance> enhances;

    private List<RocketMqConsumer> consumerHandlers;

    public RocketMqConsumerHolder(RocketMqProperties aliyunOnsProperties,
                                  List<RocketMqConsumer> consumerHandlers,
                                  List<ConsumeFailEnhance> enhances) {
        log.info("rocket mq consumer is loading！");
        this.enhances = enhances;
        this.consumerHandlers = consumerHandlers;
        if (CollectionUtils.isNotEmpty(consumerHandlers)) {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.AccessKey, aliyunOnsProperties.getAccessKey());
            properties.put(PropertyKeyConst.SecretKey, aliyunOnsProperties.getSecretKey());
            if (StringUtils.isNotBlank(aliyunOnsProperties.getNameServerAddress())) {
                properties.put(PropertyKeyConst.NAMESRV_ADDR, aliyunOnsProperties.getNameServerAddress());
            }
            for (RocketMqConsumer consumerHandler : consumerHandlers) {
                properties.put(PropertyKeyConst.ConsumerId, consumerHandler.getConsumerId());
                properties.put(PropertyKeyConst.ConsumeThreadNums, consumerHandler.getThreadNums());
                Consumer consumer = ONSFactory.createConsumer(properties);
                consumer.start();
                consumer.subscribe(consumerHandler.getTopic(), consumerHandler.getTag(), (message, context) -> {
                    try {
                        consumerHandler.handle(message, context);
                        return Action.CommitMessage;
                    } catch (Exception e) {
                        if (message.getReconsumeTimes() >= consumerHandler.getTriggerNotificationTimes()) {
                            for (ConsumeFailEnhance enhance : enhances) {
                                //// 消息 消费 失败，进行增强处理。
                                String consumerId = consumerHandler.getConsumerId();
                                if (enhance.support(consumerId, message)) {
                                    enhance.enhance(consumerId, message, e);
                                }
                            }
                        }
                        log.error(MessageFormat.format("consume ons message fail - [message_id:{} -> message_body:{}]", message.getMsgID(), ByteArrayUtils.convertByteArray2JsonString(message.getBody())), e);
                        return Action.ReconsumeLater;
                    }
                });
            }
        }

    }

    public List<ConsumeFailEnhance> getEnhances() {
        return enhances;
    }

    public RocketMqConsumerHolder setEnhances(List<ConsumeFailEnhance> enhances) {
        this.enhances = enhances;
        return this;
    }

    public List<RocketMqConsumer> getConsumerHandlers() {
        return consumerHandlers;
    }

    public RocketMqConsumerHolder setConsumerHandlers(List<RocketMqConsumer> consumerHandlers) {
        this.consumerHandlers = consumerHandlers;
        return this;
    }
}