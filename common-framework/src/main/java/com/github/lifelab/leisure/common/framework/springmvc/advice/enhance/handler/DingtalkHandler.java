package com.github.lifelab.leisure.common.framework.springmvc.advice.enhance.handler;

import com.github.lifelab.leisure.common.framework.springmvc.advice.enhance.event.ErrorSource;
import com.github.lifelab.leisure.common.framework.warning.WarningService;
import com.github.lifelab.leisure.common.utils.Warning;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private WarningService warningService;

    @Override
    public boolean support(ErrorSource t) {
        return t.getData() instanceof Warning;

    }

    @Override
    @Async
    public void dispose(ErrorSource t) {
        Warning warning = (Warning) t.getData();
        warningService.warning(warning);
    }
}
