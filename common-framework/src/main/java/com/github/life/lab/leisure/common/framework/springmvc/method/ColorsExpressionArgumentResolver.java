package com.github.life.lab.leisure.common.framework.springmvc.method;

import com.github.life.lab.leisure.common.model.expression.ColorsExpression;
import com.github.life.lab.leisure.common.model.expression.MatchType;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义表达式语言 参数解析器
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/16
 */
public class ColorsExpressionArgumentResolver extends AbstractMethodArgumentResolver {

    private static boolean isColorsExpressionParameter(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
            return actualTypes.length == 1 && actualTypes[0] == ColorsExpression.class;
        }
        return false;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "filters".equals(parameter.getParameterName())
                && List.class.isAssignableFrom(parameter.getParameterType())
                && isColorsExpressionParameter(parameter.getGenericParameterType());
    }

    @Override
    protected Object createAttribute(String attributeName,
                                     MethodParameter parameter,
                                     WebDataBinderFactory binderFactory,
                                     NativeWebRequest request) throws Exception {
        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
            if (attribute != null) {
                return attribute;
            }
        }
        Class<?> parameterType = parameter.getParameterType();
        if (parameterType.isArray() || List.class.isAssignableFrom(parameterType)) {
            return ArrayList.class.newInstance();
        }
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void bindRequestParameters(ModelAndViewContainer mavContainer,
                                         WebDataBinderFactory binderFactory,
                                         WebDataBinder binder,
                                         NativeWebRequest request,
                                         MethodParameter parameter) {
        ServletRequest servletRequest = prepareServletRequest(request);

        List<Object> target = (List<Object>) binder.getTarget();
        for (String paramName : servletRequest.getParameterMap().keySet()) {
            String[] values = request.getParameterValues(paramName);
            MatchType matchType = MatchType.get(paramName);
            if (Objects.nonNull(matchType)) {
                target.add(new ColorsExpression(paramName, values));
            }
        }
    }

    @Override
    protected boolean isXxxModelAttribute(String parameterName) {
        return MatchType.exits(parameterName);
    }

}
