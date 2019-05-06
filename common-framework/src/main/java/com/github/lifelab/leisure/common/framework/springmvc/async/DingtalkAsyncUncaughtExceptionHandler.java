package com.github.lifelab.leisure.common.framework.springmvc.async;

import com.github.lifelab.leisure.common.framework.warning.WarningService;
import com.github.lifelab.leisure.common.framework.warning.impl.DingTalkWarningService;
import com.github.lifelab.leisure.common.utils.JsonUtils;
import com.github.lifelab.leisure.common.utils.Warning;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * DingtalkAsyncUncaughtExceptionHandler
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @since 2019-05-06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DingtalkAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private WarningService warningService;

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        Warning warning = new Warning(null,
                "异步处理任务时发生异常",
                null,
                method.getDeclaringClass().getName(),
                method.getName(),
                JsonUtils.serialize(objects),
                null,
                new Date(),
                throwable.getMessage());
        warningService.warning(warning);
    }


}
