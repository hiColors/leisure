package com.github.life.lab.leisure.common.utils;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Thread local map
 *
 * @author 李伟超
 * @date 2017/11/02
 */
public class ThreadLocalUtils {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_MAP =
            ThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * 设置一个值到ThreadLocal
     *
     * @param key   键
     * @param value 值
     * @param <T>   值的类型
     * @return 被放入的值
     * @see Map#put(Object, Object)
     */
    public static <T> T put(String key, T value) {
        THREAD_LOCAL_MAP.get().put(key, value);
        return value;
    }

    /**
     * 删除参数对应的值
     *
     * @param key
     * @see Map#remove(Object)
     */
    public static void remove(String key) {
        THREAD_LOCAL_MAP.get().remove(key);
    }

    /**
     * 清空ThreadLocal
     *
     * @see Map#clear()
     */
    public static void clear() {
        THREAD_LOCAL_MAP.remove();
    }

    /**
     * 从ThreadLocal中获取值
     *
     * @param key 键
     * @param <T> 值泛型
     * @return 值, 不存在则返回null, 如果类型与泛型不一致, 可能抛出{@link ClassCastException}
     * @see Map#get(Object)
     * @see ClassCastException
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return ((T) THREAD_LOCAL_MAP.get().get(key));
    }

    /**
     * 从ThreadLocal中获取值,并指定一个当值不存在的提供者
     *
     * @see Supplier
     * @since 3.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Supplier<T> supplierOnNull) {
        return ((T) THREAD_LOCAL_MAP.get().computeIfAbsent(key, k -> supplierOnNull.get()));
    }

    /**
     * 获取一个值后然后删除掉
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值, 不存在则返回null
     * @see this#get(String)
     * @see this#remove(String)
     */
    public static <T> T getAndRemove(String key) {
        try {
            return get(key);
        } finally {
            remove(key);
        }
    }

    public static ThreadLocal<Map<String, Object>> getThreadLocalMap() {
        return THREAD_LOCAL_MAP;
    }
}
