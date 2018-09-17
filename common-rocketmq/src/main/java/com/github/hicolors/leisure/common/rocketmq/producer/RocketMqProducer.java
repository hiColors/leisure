package com.github.hicolors.leisure.common.rocketmq.producer;

/**
 * RocketMqProducer
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/17
 */
public interface RocketMqProducer {

    /**
     * 延迟投递: 延迟3s投递, 设置为 3000;
     *
     * @param topic            消息主题, 最长不超过255个字符; 由a-z, A-Z, 0-9, 以及中划线"-"和下划线"_"构成.
     * @param tag              消息标签, 请使用合法标识符, 尽量简短且见名知意
     * @param key              业务主键
     * @param body             消息体, 消息体长度默认不超过4M, 具体请参阅集群部署文档描述.
     * @param delayMillisecond 延迟时间
     */
    void publish(String topic, String tag, String key, Object body, long delayMillisecond);

    /**
     * 投递消息
     *
     * @param topic 消息主题, 最长不超过255个字符; 由a-z, A-Z, 0-9, 以及中划线"-"和下划线"_"构成.
     * @param tag   消息标签, 请使用合法标识符, 尽量简短且见名知意
     * @param key   业务主键
     * @param body  消息体, 消息体长度默认不超过4M, 具体请参阅集群部署文档描述.
     */
    void publish(String topic, String tag, String key, Object body);

    /**
     * 投递消息
     *
     * @param topic 消息主题, 最长不超过255个字符; 由a-z, A-Z, 0-9, 以及中划线"-"和下划线"_"构成.
     * @param tag   消息标签, 请使用合法标识符, 尽量简短且见名知意
     * @param body  消息体, 消息体长度默认不超过4M, 具体请参阅集群部署文档描述.
     */
    void publish(String topic, String tag, Object body);


    /**
     * 投递消息
     *
     * @param topic 消息主题, 最长不超过255个字符; 由a-z, A-Z, 0-9, 以及中划线"-"和下划线"_"构成.
     * @param body  消息体, 消息体长度默认不超过4M, 具体请参阅集群部署文档描述.
     */
    void publish(String topic, Object body);
}
