package com.github.lifelab.leisure.common.framework.logger;

import com.github.lifelab.leisure.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * request 日志 ExtraParam 操作工具类
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
public class ExtraParamUtils {

    private static final String REQUEST_KEY_EXTRA_PARAM = "extra-param";

    /**
     * 设置 request 日志 ExtraParam 参数中的键值对
     *
     * @param key
     * @param obj
     */
    public static void put(String key, String obj) {
        put(key, obj, true);
    }


    /**
     * 设置 request 日志 ExtraParam 参数中的键值对
     *
     * @param key
     * @param obj
     * @param cover true覆盖；false不覆盖
     */
    public static void put(String key, String obj, boolean cover) {
        // 从 threadlocal 取值
        Map<String, String> extraMap = ObjectUtils.defaultIfNull(ThreadLocalUtils.getAndRemove(REQUEST_KEY_EXTRA_PARAM), new ConcurrentHashMap<>(16));
        // 设置值
        if (!cover) {
            extraMap.putIfAbsent(key, obj);
        } else {
            extraMap.put(key, obj);
        }
        // 放进 threadlocal
        ThreadLocalUtils.getThreadLocalMap().get().put(REQUEST_KEY_EXTRA_PARAM, extraMap);
    }


    /**
     * 获取所有额外参数
     *
     * @return
     */
    public static Map<String, String> getAll() {
        return ObjectUtils.defaultIfNull(ThreadLocalUtils.getAndRemove(REQUEST_KEY_EXTRA_PARAM), new ConcurrentHashMap<>(16));
    }
}
