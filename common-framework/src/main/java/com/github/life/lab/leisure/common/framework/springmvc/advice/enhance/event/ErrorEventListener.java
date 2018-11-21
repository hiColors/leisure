package com.github.life.lab.leisure.common.framework.springmvc.advice.enhance.event;

import com.github.life.lab.leisure.common.framework.springmvc.advice.enhance.handler.ErrorSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ErrorEventLis
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/5/25
 */
@Component
public class ErrorEventListener implements ApplicationListener<ErrorEvent> {

    private final List<ErrorSourceHandler> handlers;

    @Autowired
    public ErrorEventListener(List<ErrorSourceHandler> handlers) {
        this.handlers = handlers;
    }

    @Async
    @Override
    public void onApplicationEvent(ErrorEvent errorEvent) {
        if (errorEvent.getSource() instanceof ErrorSource) {
            for (ErrorSourceHandler handler : handlers) {
                if (handler.support((ErrorSource) errorEvent.getSource())) {
                    handler.dispose((ErrorSource) errorEvent.getSource());
                }
            }
        }
    }
}
