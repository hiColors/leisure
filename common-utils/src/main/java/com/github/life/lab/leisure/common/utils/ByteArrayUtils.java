package com.github.life.lab.leisure.common.utils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * ByteArrayUtils
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/26
 */
public class ByteArrayUtils {

    /**
     * 对象转换为字节数组
     *
     * @param obj
     * @return
     */
    public static byte[] objectToBytes(Object obj) {
        byte[] bytes;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            bytes = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return bytes;
    }

    /**
     * 字节数组 转换为指定的 对象
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        T t;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn;
        try {
            sIn = new ObjectInputStream(in);
            t = clazz.cast(sIn.readObject());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return t;
    }

    /**
     * 解析 指定对象 为 字节
     *
     * @param object
     * @return
     */
    public static byte[] convertObject2ByteArray(Object object) {
        return JsonUtils.serialize(object).getBytes();
    }

    public static <T> T convertByteArray2Object(byte[] data, Class<T> clazz) {
        return convertJsonString2Object(convertByteArray2JsonString(data), clazz);
    }

    /**
     * 解析 字节数据 为 json 字符串
     *
     * @param body
     * @return
     */
    public static String convertByteArray2JsonString(byte[] body) {
        return new String(body, Charset.forName("UTF-8"));
    }

    /**
     * 解析 json 数据 为 指定对象
     *
     * @param json
     * @param clzz
     * @param <T>
     * @return
     */
    public static <T> T convertJsonString2Object(String json, Class<T> clzz) {
        return JsonUtils.deserialize(json, clzz);
    }
}