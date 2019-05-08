package com.github.lifelab.leisure.common.framework.warning.impl;

import brave.Tracer;
import com.github.lifelab.leisure.common.framework.utils.EnvHelper;
import com.github.lifelab.leisure.common.framework.warning.WarningService;
import com.github.lifelab.leisure.common.utils.DingTalkUtils;
import com.github.lifelab.leisure.common.utils.Warning;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * DingTalkWarningService
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-05-06
 */
@Service
public class DingTalkWarningService implements WarningService {

    @Autowired
    private Tracer tracer;

    @Autowired
    private EnvHelper envHelper;

    /**
     * 服务中 配置的钉钉告警地址
     */
    @Value("${warning.dingtalk:}")
    private String dingTalkWebhook;

    @Value("${aliyun.sls.projectName:}")
    private String projectName;

    @Value("${aliyun.sls.logStoreName:}")
    private String logStoreName;


    @Override
    public void warning(Warning model) {
        model.setEnv(ObjectUtils.defaultIfNull(model.getEnv(), envHelper.getEnv()));
        model.setTraceId(ObjectUtils.defaultIfNull(model.getTraceId(), tracer.currentSpan().context().traceIdString()));
        model.setExceptionMsg(exceptionMsg(model.getExceptionMsg()));
        DingTalkUtils.send(dingTalkWebhook, model);
    }

    protected String exceptionMsg(String exceptionMsg) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(logStoreName)) {
            return exceptionMsg;
        }
        String traceId = tracer.currentSpan().context().traceIdString();
        StringBuilder url = new StringBuilder()
                .append("https://sls.console.aliyun.com/next/project/")
                .append(projectName)
                .append("/logsearch/")
                .append(logStoreName)
                .append("?queryString=%s")
                .append("&queryTimeType=99")
                .append("&startTime=%d")
                .append("&endTime=%d");
        long startTime = new Date().toInstant().minusSeconds(10 * 60).getEpochSecond();
        long endTime = new Date().toInstant().plusSeconds(5 * 60).getEpochSecond();
        String logUrl = String.format(url.toString(), traceId, startTime, endTime);
        return exceptionMsg + "\r\n" + "[ 更多详情请点击查看阿里云日志 ](" + logUrl + ")";
    }

}
