package com.github.lifelab.leisure.common.utils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 用户存储一些静态变量和内容
 */
public class LocalCacheUtils {

    private static final ReadWriteLock CACHE_LOCK = new ReentrantReadWriteLock();

    private static final Map<String, Object> LOCAL_CACHE = new ConcurrentHashMap<>();

    public static Object getValue(String key) {
        CACHE_LOCK.readLock().lock();
        try {
            return LOCAL_CACHE.get(key);
        } finally {
            CACHE_LOCK.readLock().unlock();
        }
    }

    public static void put(Map<String, Object> params) {
        CACHE_LOCK.writeLock().lock();
        try {
            LOCAL_CACHE.putAll(params);
        } finally {
            CACHE_LOCK.writeLock().unlock();
        }
    }

    public static void put(String key, Object value) {
        CACHE_LOCK.writeLock().lock();
        try {
            LOCAL_CACHE.put(key, value);
        } finally {
            CACHE_LOCK.writeLock().unlock();
        }
    }

    public static void put(Properties p) {
        CACHE_LOCK.writeLock().lock();
        try {
            Enumeration<Object> en = p.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                LOCAL_CACHE.put(key, p.get(key));
            }
        } finally {
            CACHE_LOCK.writeLock().unlock();
        }
    }

    public static Set<String> keys() {
        CACHE_LOCK.readLock().lock();
        try {
            return LOCAL_CACHE.keySet();
        } finally {
            CACHE_LOCK.readLock().unlock();
        }
    }
}
