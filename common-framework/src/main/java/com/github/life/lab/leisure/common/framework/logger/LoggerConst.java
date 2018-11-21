package com.github.life.lab.leisure.common.framework.logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志中的常量信息
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/8/17
 */
public class LoggerConst {

    public static final String REQUEST_KEY_BODY_PARAM = "body-param";
    public static final String REQUEST_KEY_FORM_PARAM = "form-param";
    static final String REQUEST_PREFIX = "> ";
    static final String KEY_VALUE_SEPERATOR = ": ";
    static final String RESPONSE_PREFIX = "< ";
    static final String REQUEST_IDENTITY = "request";
    static final String RESPONSE_IDENTITY = "response";
    static final String REQUEST_KEY_TRACE_ID = "trace-id";
    /**
     * request 记录的key
     */
    static final String REQUEST_KEY_REQUEST_TIME = "request-time";
    static final String REQUEST_KEY_EXTRA_PARAM = "extra-param";
    static final String REQUEST_KEY_URL = "url";
    static final String REQUEST_KEY_HTTP_METHOD = "http-method";
    static final String REQUEST_KEY_HEADER = "header";

    /**
     * response 记录的key
     */
    static final String RESPONSE_KEY_HTTP_STATUS = "http-status";
    static final String RESPONSE_KEY_RESPONSE_TIME = "response-time";
    static final String RESPONSE_KEY_TAKE_TIME = "take-time";
    static final String RESPONSE_KEY_RESPONSE_DATA = "response-data";

    static final String RESPONSE_KEY_CONTENT_TYPE = "content-type";
    static final String RESPONSE_KEY_CONTENT_LENGTH = "content-length";


    static final String VALUE_DEFAULT = "-";
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    static final List<String> REQUEST_KEY_LIST = new ArrayList<>();
    static final List<String> RESPONSE_KEY_LIST = new ArrayList<>();


    static {
        //初始化request中的key add的顺序即为日志显示的顺序
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_REQUEST_TIME);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_URL);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_HTTP_METHOD);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_TRACE_ID);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_HEADER);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_EXTRA_PARAM);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_FORM_PARAM);
        REQUEST_KEY_LIST.add(LoggerConst.REQUEST_KEY_BODY_PARAM);


        //初始化response中的key add的顺序即为日志显示的顺序
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_RESPONSE_TIME);
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_HTTP_STATUS);
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_TAKE_TIME);
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_CONTENT_TYPE);
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_CONTENT_LENGTH);
        RESPONSE_KEY_LIST.add(LoggerConst.RESPONSE_KEY_RESPONSE_DATA);
    }

}
