package com.github.life.lab.leisure.common.rocketmq.consumer;

/**
 * 阿里云 ons 消费者
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/9
 */
public abstract class AbstractRocketMqConsumer implements RocketMqConsumer {

    private final String consumerId;

    private final String topic;

    private String tag = "*";

    private Integer threadNums = 1;

    private Integer triggerNotificationTimes = 16;


    protected AbstractRocketMqConsumer(String consumerId, String topic) {
        this.consumerId = consumerId;
        this.topic = topic;
    }

    protected AbstractRocketMqConsumer(String consumerId, String topic, String tag) {
        this.consumerId = consumerId;
        this.topic = topic;
        this.tag = tag;
    }

    protected AbstractRocketMqConsumer(String consumerId, String topic, String tag, Integer threadNums, Integer triggerNotificationTimes) {
        this.consumerId = consumerId;
        this.topic = topic;
        this.tag = tag;
        this.threadNums = threadNums;
        this.triggerNotificationTimes = triggerNotificationTimes;
    }


    @Override
    public String getConsumerId() {
        return consumerId;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Integer getThreadNums() {
        return threadNums;
    }

    @Override
    public Integer getTriggerNotificationTimes() {
        return triggerNotificationTimes;
    }
}