package com.github.hicolors.leisure.common.framework.springmvc.advice;

import brave.Tracer;
import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.framework.logger.LoggerConst;
import com.github.hicolors.leisure.common.framework.springmvc.advice.enhance.event.ErrorEvent;
import com.github.hicolors.leisure.common.framework.utils.EnvHelper;
import com.github.hicolors.leisure.common.model.response.ErrorResponse;
import com.github.hicolors.leisure.common.utils.JsonUtils;
import com.github.hicolors.leisure.common.utils.Warning;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常处理器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/7/5
 */

@Slf4j
public abstract class AbstractExceptionHandlerAdvice implements ApplicationEventPublisherAware {

    private static final long UNEXPECT_EXCEPTION_CODE = 88888888L;
    private static final long PARAM_VALIDATED_UN_PASS = 66666666L;

    private ApplicationEventPublisher publisher;

    @Autowired
    private Tracer tracer;

    @Autowired
    private EnvHelper envHelper;

    @Value("${aliyun.sls.projectName:}")
    private String projectName;

    @Value("${aliyun.sls.logStoreName:}")
    private String logStoreName;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @ExceptionHandler(value = Exception.class)
    public ErrorResponse errorAttributes(Exception exception, HttpServletRequest request, HttpServletResponse response) {

        // 异常错误返回时 添加 trace_id 到 response header 中
        response.setHeader("trace-id", tracer.currentSpan().context().traceIdString());

        //特定异常处理所需要的参数
        Object data;
        Map paramMap = getParam(request);
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();

        //拼装返回结果
        ErrorResponse errorResponse = new ErrorResponse(new Date(), uri);

        if (exception instanceof ExtensionException) {
            log.warn(MessageFormat.format("当前程序进入到异常捕获器，出错的 url 为：[ {0} ]，出错的参数为：[ {1} ]", url, JsonUtils.serialize(paramMap)), exception);
            ExtensionException expectException = (ExtensionException) exception;
            errorResponse.setCode(expectException.getCode());
            errorResponse.setMessage(expectException.getMessage());
            errorResponse.setStatus(expectException.getStatus());
            data = expectException.getData();
            if (Objects.nonNull(expectException.getCause())) {
                errorResponse.setException(expectException.getCause().getMessage());
            }
        } else {
            log.error(MessageFormat.format("当前程序进入到异常捕获器，出错的 url 为：[ {0} ]，出错的参数为：[ {1} ]", url, JsonUtils.serialize(paramMap)), exception);
            errorResponse.setCode(UNEXPECT_EXCEPTION_CODE);
            errorResponse.setMessage("服务器发生了点小故障，请联系客服人员！");
            errorResponse.setException(exception.getMessage());
            errorResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            /**
             * 参数校验异常特殊处理
             */
            if (exception instanceof BindException) {
                errorResponse.setCode(PARAM_VALIDATED_UN_PASS);
                errorResponse.setMessage("输入的数据不合法,详情见 errors 字段");
                for (FieldError fieldError : ((BindException) exception).getBindingResult().getFieldErrors()) {
                    errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
                }
                errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            } else if (exception instanceof MethodArgumentNotValidException) {
                errorResponse.setCode(PARAM_VALIDATED_UN_PASS);
                errorResponse.setMessage("输入的数据不合法,详情见 errors 字段");
                for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
                    errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
                }
                errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            } else if (exception instanceof ConstraintViolationException) {
                errorResponse.setCode(PARAM_VALIDATED_UN_PASS);
                errorResponse.setMessage("输入的数据不合法,详情见 errors 字段");
                for (ConstraintViolation cv : ((ConstraintViolationException) exception).getConstraintViolations()) {
                    errorResponse.addError(cv.getPropertyPath().toString(), cv.getMessage());
                }
                errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            } else if (exception instanceof NoHandlerFoundException) {
                errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            } else if (exception instanceof HttpRequestMethodNotSupportedException) {
                errorResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            } else if (exception instanceof HttpMediaTypeNotSupportedException) {
                errorResponse.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
            }
            data = new Warning(envHelper.getEnv(),
                    "服务发生非预期异常",
                    tracer.currentSpan().context().traceIdString(),
                    uri,
                    request.getMethod(),
                    JsonUtils.serialize(paramMap),
                    null,
                    new Date(),
                    exceptionMsg(exception));
        }
        response.setStatus(errorResponse.getStatus());
        publisher.publishEvent(new ErrorEvent(errorResponse, data));
        return errorResponse;
    }


    @SuppressWarnings("unchecked")
    private Map getParam(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>(2);
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            String requestBody = "";
            try {
                requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
                if (StringUtils.isNotBlank(requestBody)) {
                    requestBody = org.springframework.util.StringUtils.trimAllWhitespace(requestBody);
                }
            } catch (IOException ignored) {
            }
            params.put(LoggerConst.REQUEST_KEY_FORM_PARAM, JsonUtils.serialize(request.getParameterMap()));
            params.put(LoggerConst.REQUEST_KEY_BODY_PARAM, requestBody);
        }
        return params;
    }

    protected String exceptionMsg(Exception exception) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(logStoreName)) {
            return exception.getMessage();
        }
        String traceId = tracer.currentSpan().context().traceIdString();
        StringBuilder url = new StringBuilder();
        url.append("https://sls.console.aliyun.com/next/project/")
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
        return exception.getMessage() + "\r\n" + "[查看阿里云日志](" + logUrl + ")";
    }
}