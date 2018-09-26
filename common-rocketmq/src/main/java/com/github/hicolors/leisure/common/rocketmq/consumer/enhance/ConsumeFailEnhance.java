package com.github.hicolors.leisure.common.rocketmq.consumer.enhance;

import com.aliyun.openservices.ons.api.Message;

/**
 * ConsumeFailEnhance
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/26
 */
public interface ConsumeFailEnhance {

    /**
     * 当前 handler 是否支持当前 消息
     *
     * @param consumerId
     * @param msg
     * @return
     */
    boolean support(String consumerId, Message msg);


    /**
     * 处理逻辑
     *
     * @param consumerId
     * @param msg
     * @param e
     */
    void enhance(String consumerId, Message msg, Exception e);

}