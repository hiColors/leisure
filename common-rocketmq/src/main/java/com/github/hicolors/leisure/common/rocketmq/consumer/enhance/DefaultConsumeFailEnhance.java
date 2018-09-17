package com.github.hicolors.leisure.common.rocketmq.consumer.enhance;

import com.aliyun.openservices.ons.api.Message;
import com.github.hicolors.leisure.common.rocketmq.properties.RocketMqProperties;
import com.github.hicolors.leisure.common.utils.ByteArrayUtils;
import com.github.hicolors.leisure.common.utils.DingTalkUtils;
import com.github.hicolors.leisure.common.utils.Warning;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 默认 消费失败增强处理，发送钉钉消息
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/10
 */

@Slf4j
@Service
@ConditionalOnProperty(name = "rocket.dingtalk")
public class DefaultConsumeFailEnhance implements ConsumeFailEnhance {

    @Autowired
    private RocketMqProperties properties;

    @Override
    public boolean support(String consumerId, Message msg) {
        return true;
    }

    @Override
    @Async
    public void enhance(String consumerId, Message message, Exception e) {
        log.info("Consuming ons message failed, and a warning dingtalk message will be issued soon！this message detail --> producerId:[{}], message-body:[{}]",
                consumerId,
                ByteArrayUtils.convertByteArray2JsonString(message.getBody()));

        //钉钉通知
        Map<String, Object> map = new HashMap<>(2);
        map.put("consumerId", consumerId);

        DingTalkUtils.send(properties.getDingtalk(),
                new Warning(
                        "MQ 消费失败预警",
                        message.getMsgID(),
                        "消费消息",
                        "",
                        ByteArrayUtils.convertByteArray2JsonString(message.getBody()),
                        map,
                        new Date(),
                        e.getMessage()));
    }
}