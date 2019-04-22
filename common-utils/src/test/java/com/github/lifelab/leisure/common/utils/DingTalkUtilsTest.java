package com.github.lifelab.leisure.common.utils;

import com.github.lifelab.leisure.common.utils.DingTalkUtils;
import com.github.lifelab.leisure.common.utils.Warning;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class DingTalkUtilsTest {

    @Test
    public void send() {
        DingTalkUtils.send(
                "https://oapi.dingtalk.com/robot/send?access_token=099551a192492077a922a5d1aa7e1a060b0fd18890f021c0e4298013d4dcb5c2",
                new Warning("test",
                        "测试标题",
                        UUID.randomUUID().toString().replace("-", ""),
                        "测试环节",
                        "测试方法",
                        "测试入参",
                        null,
                        new Date(),
                        "测试异常"));
    }
}