package com.github.hicolors.leisure.common.rocketmq.consumer.enhance;

import com.aliyun.openservices.ons.api.Message;

public interface ConsumeFailEnhance {

    /**
     * 当前 handler 是否支持当前 消息
     *
     * @param msg
     * @return
     */
    boolean support(String consumerId, Message msg);

    /**
     * 处理逻辑
     *
     * @param msg
     */
    void enhance(String consumerId, Message msg, Exception e);

}