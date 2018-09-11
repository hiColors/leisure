package com.github.hicolors.leisure.common.utils;

import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class DingTalkUtilsTest {

    @Test
    public void send() {
        DingTalkUtils.send(
                "https://oapi.dingtalk.com/robot/send?access_token=8b9e2b314f6363cca32e9a1d325a561d2a8a0079e689b2a40db33967f121cab4",
                new Warning("测试标题",
                        UUID.randomUUID().toString().replace("-", ""),
                        "测试环节",
                        "测试方法",
                        "测试入参",
                        null,
                        new Date(),
                        "测试异常"));
    }
}