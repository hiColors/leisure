package com.github.hicolors.leisure.common.framework.springmvc.advice;

import brave.Tracer;
import com.github.hicolors.leisure.common.exception.ExtensionException;
import com.github.hicolors.leisure.common.framework.logger.LoggerConst;
import com.github.hicolors.leisure.common.framework.springmvc.enhance.ErrorEvent;
import com.github.hicolors.leisure.common.framework.springmvc.response.ErrorResponse;
import com.github.hicolors.leisure.common.utils.JSONUtils;
import com.github.hicolors.leisure.common.utils.Warning;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/7/5
 */

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Autowired
    private Tracer tracer;

    private static final long UNEXPECT_EXCEPTION_CODE = 88888888L;

    private static final long PARAM_VALIDATED_UN_PASS = 66666666L;


    @ExceptionHandler(value = Exception.class)
    public ErrorResponse errorAttributes(Exception exception, HttpServletRequest request, HttpServletResponse response) {

        //拼装返回结果
        ErrorResponse errorResponse = new ErrorResponse(request);

        //特定异常处理所需要的参数
        Object data;
        Map paramMap = getParam(request);
        String url = request.getRequestURL().toString();
        if (exception instanceof ExtensionException) {
            log.warn(MessageFormat.format("当前程序进入到异常捕获器，出错的 url 为：[ {0} ]，出错的参数为：[ {1} ]", url, JSONUtils.serialize(paramMap)), exception);
            ExtensionException expectException = (ExtensionException) exception;
            errorResponse.setCode(expectException.getCode());
            errorResponse.setMessage(expectException.getMessage());
            errorResponse.setStatus(expectException.getStatus());
            data = expectException.getData();
        } else {
            log.error(MessageFormat.format("当前程序进入到异常捕获器，出错的 url 为：[ {0} ]，出错的参数为：[ {1} ]", url, JSONUtils.serialize(paramMap)), exception);
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
            } else if (exception instanceof NoHandlerFoundException) {
                errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            } else if (exception instanceof HttpRequestMethodNotSupportedException) {
                errorResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            } else if (exception instanceof HttpMediaTypeNotSupportedException) {
                errorResponse.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
            }
            data = new Warning("服务发生非预期异常",
                    tracer.currentSpan().context().traceIdString(),
                    request.getRequestURI(),
                    request.getMethod(),
                    JSONUtils.serialize(paramMap),
                    null,
                    new Date(),
                    exception.getMessage());
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
            params.put(LoggerConst.REQUEST_KEY_FORM_PARAM, JSONUtils.serialize(request.getParameterMap()));
            params.put(LoggerConst.REQUEST_KEY_BODY_PARAM, requestBody);
        }
        return params;
    }

}