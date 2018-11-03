package com.github.hicolors.leisure.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.env.Environment;

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

    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

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
     * @param warning 标准预警信息
     */
    public static void send(String webhook, Warning warning) {
        sendMarkdown(webhook, buildStandardTemplate(warning));
    }


    public static void sendText(String webhook, String text) {
        send(webhook, MsgType.TEXT, text);
    }

    public static void sendMarkdown(String webhook, String text) {
        send(webhook, MsgType.MARKDOWN, text);
    }

    public static synchronized void send(String webhook, MsgType msgType, String text) {
        String body = getDingTalkPostBody(msgType, text);
        HttpPost httppost = new HttpPost(webhook);
        try {
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(body, "utf-8");
            httppost.setEntity(se);
            HttpResponse response = HTTP_CLIENT.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("预警发送成功 -> " + body);
            }
        } catch (IOException e) {
            log.warn("预警发送失败 -> " + body);
        } finally {
            httppost.reset();
        }
    }

    private static String buildStandardTemplate(Warning warning) {
        return MessageFormat.format(STANDARD_TEMPLATE,
                warning.getTitle(),
                warning.getEnv(),
                warning.getTraceId(),
                warning.getBusiness(),
                warning.getMethodName(),
                warning.getMethodParams(),
                JsonUtils.serialize(warning.getExtraInfo()),
                DateFormatUtils.format(warning.getDate(), "yyyy-MM-dd HH:mm:ss.SSS"),
                warning.getExceptionMsg());
    }

    public static String getDingTalkPostBody(MsgType msgType, String msg) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("msgtype", msgType.getType());
        switch (msgType) {
            case MARKDOWN:
                Map<String, Object> markdown = new HashMap<>(2);
                markdown.put("title", "钉钉机器人预警");
                markdown.put("text", msg);
                result.put("markdown", markdown);
                break;
            case TEXT:
                Map<String, Object> text = new HashMap<>(2);
                text.put("title", "钉钉机器人预警");
                text.put("content", msg);
                result.put("text", text);
                break;
            default:
                break;
        }
        return JsonUtils.serialize(result);
    }

    public enum MsgType {
        /**
         * 钉钉发送消息text类型
         */
        TEXT("text"),
        /**
         * 钉钉发送消息markdown类型
         */
        MARKDOWN("markdown");

        private final String type;

        MsgType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}