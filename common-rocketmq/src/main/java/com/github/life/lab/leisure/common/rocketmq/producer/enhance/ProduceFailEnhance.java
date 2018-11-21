package com.github.life.lab.leisure.common.rocketmq.producer.enhance;

import com.aliyun.openservices.ons.api.Message;

/**
 * 消息生产(发送)失败 增强处理
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/6
 */
public interface ProduceFailEnhance {

    /**
     * 当前 handler 是否支持当前 消息
     *
     * @param producerId
     * @param msg
     * @return
     */
    boolean support(String producerId, Message msg);

    /**
     * 处理逻辑
     *
     * @param producerId
     * @param msg
     * @param e
     */
    void enhance(String producerId, Message msg, Exception e);

}