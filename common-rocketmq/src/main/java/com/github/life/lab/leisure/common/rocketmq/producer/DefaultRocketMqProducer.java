package com.github.life.lab.leisure.common.rocketmq.producer;

import com.aliyun.openservices.ons.api.*;
import com.github.life.lab.leisure.common.rocketmq.producer.enhance.ProduceFailEnhance;
import com.github.life.lab.leisure.common.rocketmq.properties.RocketMqProperties;
import com.github.life.lab.leisure.common.utils.ByteArrayUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.*;


/**
 * 阿里 ONS 消息生产者
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/10
 */
@Slf4j
public class DefaultRocketMqProducer implements RocketMqProducer {

    private Producer producerImpl;

    private Map<String, Producer> producerMap;

    private List<ProduceFailEnhance> enhances;

    private RocketMqProperties rocketMqProperties;

    public DefaultRocketMqProducer(RocketMqProperties rocketMqProperties, List<ProduceFailEnhance> enhances) {
        log.info("rocket mq producer is loading！");
        this.enhances = enhances;
        this.rocketMqProperties = rocketMqProperties;

        //临时变量
        Map<String, Producer> tempMap = new HashMap<>();
        Producer producer;

        //密钥公用
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, rocketMqProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, rocketMqProperties.getSecretKey());
        if (StringUtils.isNotBlank(rocketMqProperties.getNameServerAddress())) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, rocketMqProperties.getNameServerAddress());
        }

        if (MapUtils.isNotEmpty(rocketMqProperties.getProducer().getTopicProducer())) {
            for (Map.Entry<String, String> one : rocketMqProperties.getProducer().getTopicProducer().entrySet()) {
                properties.put(PropertyKeyConst.ProducerId, one.getValue());
                producer = ONSFactory.createProducer(properties);
                producer.start();
                tempMap.put(one.getKey(), producer);
            }
            if (MapUtils.isNotEmpty(tempMap)) {
                //产生只读的 map
                producerMap = Collections.unmodifiableMap(tempMap);
            }
        }

        if (rocketMqProperties.getProducer().getDefaultProducer() != null) {
            properties.put(PropertyKeyConst.ProducerId, rocketMqProperties.getProducer().getDefaultProducer());
            producerImpl = ONSFactory.createProducer(properties);

            producerImpl.start();
        }
    }


    /**
     * 根据 topic 获取生产者
     *
     * @param topic
     * @return
     */
    private Producer getProducerByTopic(String topic) {
        return MapUtils.isNotEmpty(producerMap) ? producerMap.get(topic) : null;
    }

    private Producer getProducer(String topic) {
        Producer result = getProducerByTopic(topic);
        return Objects.nonNull(result) ? result : getProducerImpl();
    }

    /**
     * 获取当前的 producer id
     *
     * @param topic
     * @return
     */
    private String getProducerId(String topic) {
        return Objects.nonNull(getProducerByTopic(topic)) ? rocketMqProperties.getProducer().getTopicProducer().get(topic) : rocketMqProperties.getProducer().getDefaultProducer();
    }

    /**
     * 获取原生的 producer
     *
     * @return
     */
    protected Producer getProducerImpl() {
        return producerImpl;
    }


    @Override
    public void publish(String topic, Object body) {
        publish(topic, "", body);
    }

    @Override
    public void publish(String topic, String tag, Object body) {
        publish(topic, tag, "", body);
    }

    @Override
    public void publish(String topic, String tag, String key, Object body) {
        publish(topic, tag, key, body, 0L);
    }

    @Override
    public void publish(String topic, String tag, String key, Object body, long delayMillisecond) {
        Message message = new Message(topic, tag, key, ByteArrayUtils.convertObject2ByteArray(body));
        message.setStartDeliverTime(System.currentTimeMillis() + delayMillisecond);
        publish(message);
    }

    private void publish(@NonNull Message message) {
        SendResult result;
        Producer producer = getProducer(message.getTopic());
        try {
            result = producer.send(message);
            log.info("produce rocket mq message success - [message_id:{} -> message_body:{}]", result.getMessageId(), ByteArrayUtils.convertByteArray2JsonString(message.getBody()));
        } catch (Exception e) {
            log.error(MessageFormat.format("produce rocket mq message fail - [message_body:{0}] ", ByteArrayUtils.convertByteArray2JsonString(message.getBody())), e);
            for (ProduceFailEnhance enhance : enhances) {
                // 消息生产（发送）失败，进行增强处理。
                String producerId = getProducerId(message.getTopic());
                if (enhance.support(producerId, message)) {
                    enhance.enhance(producerId, message, e);
                }
            }
        }

    }
}