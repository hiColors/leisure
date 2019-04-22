package com.github.lifelab.leisure.common.framework.springmvc.method;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * 参数解析器 基类
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public abstract class AbstractMethodArgumentResolver implements HandlerMethodArgumentResolver {


    protected final static String SEPARATOR = ",; \t\n";

    /**
     * 是否是需要转换的属性
     *
     * @param parameterName
     * @return
     */
    abstract boolean isXxxModelAttribute(String parameterName);


    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        String name = getParameterName(parameter);
        Object target = mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);
        WebDataBinder binder = binderFactory.createBinder(request, target, name);
        target = binder.getTarget();
        if (target != null) {
            bindRequestParameters(mavContainer, binderFactory, binder, request, parameter);

            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }
        target = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(name)) {
            mavContainer.addAttribute(name, target);
        }
        return target;
    }

    /**
     * 预处理参数
     *
     * @param request
     * @return
     */
    protected ServletRequest prepareServletRequest(NativeWebRequest request) {
        return (HttpServletRequest) request.getNativeRequest();
    }

    /**
     * 获取参数名称
     *
     * @param parameter
     * @return
     */
    protected String getParameterName(MethodParameter parameter) {
        return parameter.getParameterName();
    }

    /**
     * 创建属性类型
     *
     * @param attributeName
     * @param parameter
     * @param binderFactory
     * @param request
     * @return
     * @throws Exception
     */
    protected abstract Object createAttribute(String attributeName,
                                              MethodParameter parameter,
                                              WebDataBinderFactory binderFactory,
                                              NativeWebRequest request) throws Exception;


    /**
     * 获取 URI 中的参数
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null) ? variables : Collections.emptyMap();
    }


    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        if (StringUtils.hasText(variables.get(attributeName))) {
            return variables.get(attributeName);
        } else if (StringUtils.hasText(request.getParameter(attributeName))) {
            return request.getParameter(attributeName);
        } else {
            return null;
        }
    }

    protected Object createAttributeFromRequestValue(String sourceValue,
                                                     String attributeName,
                                                     MethodParameter parameter,
                                                     WebDataBinderFactory binderFactory,
                                                     NativeWebRequest request) throws Exception {
        DataBinder binder = binderFactory.createBinder(request, null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(parameter);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
            }
        }
        return null;
    }


    /**
     * 绑定请求参数
     *
     * @param mavContainer
     * @param binderFactory
     * @param binder
     * @param request
     * @param parameter
     * @throws Exception
     */
    protected abstract void bindRequestParameters(ModelAndViewContainer mavContainer,
                                                  WebDataBinderFactory binderFactory,
                                                  WebDataBinder binder,
                                                  NativeWebRequest request,
                                                  MethodParameter parameter) throws Exception;

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]);
        return !hasBindingResult;
    }
}
