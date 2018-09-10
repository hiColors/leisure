package com.github.hicolors.leisure.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉机器人 工具类
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/30
 */
@Slf4j
public class DingTalkUtils {

    private static HttpClient httpclient = HttpClients.createDefault();

    private static final String ENV = System.getProperty("env");

    private static final String STANDARD_TEMPLATE =
            "<font face=\"微软雅黑\" color=#ff0000 size=4> {0} </font>  \r\n" +
                    "> ENV : {1}  \r\n" +
                    "> TRACE_ID : {2}  \r\n" +
                    "> 业务环节 : {3}  \r\n" +
                    "> 方法名 : {4}  \r\n" +
                    "> 方法入参 : {5}  \r\n" +
                    "> 额外信息 : {6}  \r\n" +
                    "> 时间 : {7}  \r\n" +
                    "> 异常信息 : {8}  \r\n";


    /**
     * @param webhook 钉钉机器人地址
     */
    public static void send(String webhook, Warning warning) {
        String message = buildStandardTemplate(warning);
        try {
            HttpPost httppost = new HttpPost(webhook);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(message, "utf-8");
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("预警发送成功 -> " + message);
            }
        } catch (IOException e) {
            log.warn("预警发送失败 -> " + message);
        }
    }


    private static String buildStandardTemplate(Warning warning) {
        String message = MessageFormat.format(STANDARD_TEMPLATE,
                warning.getTitle(),
                ENV,
                warning.getTraceId(),
                warning.getBusiness(),
                warning.getMethodName(),
                warning.getMethodParams(),
                JSONUtils.serialize(warning.getExtraInfo()),
                DateFormatUtils.format(warning.getDate(), "yyyy-MM-dd HH:mm:ss.SSS"),
                warning.getExceptionMsg());
        return getMarkdownMsg(message);
    }


    private static String getMarkdownMsg(String text) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msgtype", "markdown");
        Map<String, Object> markdown = new HashMap<String, Object>();
        markdown.put("title", "钉钉机器人预警");
        markdown.put("text", text);
        result.put("markdown", markdown);
        return JSONUtils.serialize(result);
    }
}