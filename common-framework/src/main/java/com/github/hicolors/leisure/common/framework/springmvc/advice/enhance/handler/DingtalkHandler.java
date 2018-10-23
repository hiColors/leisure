package com.github.hicolors.leisure.common.framework.springmvc.advice.enhance.handler;

import com.github.hicolors.leisure.common.framework.springmvc.advice.enhance.event.ErrorSource;
import com.github.hicolors.leisure.common.utils.DingTalkUtils;
import com.github.hicolors.leisure.common.utils.Warning;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * DingtalkHandler
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/11
 */
@Component
@Slf4j
public class DingtalkHandler implements ErrorSourceHandler {

    /**
     * 服务中 配置的钉钉告警地址
     */
    @Value("${warning.dingtalk:}")
    private String webhook;

    @Override
    public boolean support(ErrorSource t) {
        return t.getData() instanceof Warning;

    }

    @Override
    @Async
    public void dispose(ErrorSource t)  {
        if(StringUtils.isNotBlank(webhook)){
            Warning warning = (Warning) t.getData();
            DingTalkUtils.send(webhook, warning);
        }
    }
}
