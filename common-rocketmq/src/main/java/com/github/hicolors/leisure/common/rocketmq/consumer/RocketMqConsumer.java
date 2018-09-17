package com.github.hicolors.leisure.common.rocketmq.consumer;

import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;

/**
 * ONS 消费接口
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/6
 */
public interface RocketMqConsumer {

    /**
     * 获取消费者 id
     *
     * @return
     */
    String getConsumerId();

    /**
     * 获取 消息主题
     *
     * @return
     */
    String getTopic();

    /**
     * 获取 消息标签
     *
     * @return
     */
    String getTag();

    /**
     * 获取线程数量
     *
     * @return
     */
    Integer getThreadNums();

    /**
     * 触发通知的次数
     *
     * @return
     */
    Integer getTriggerNotificationTimes();

    /**
     * 处理逻辑
     *
     * @param message
     * @param context
     * @throws Exception
     */
    void handle(Message message, ConsumeContext context) throws Exception;

}