package com.github.hicolors.leisure.common.feign.customiz.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class PenetrateHeadersRequestInterceptor implements RequestInterceptor {

    private static final String SPLIT_CHARACTER = ",";

    @Value("feign.penetrate.headers:user-agent")
    private String penetrateHeaders;

    @Override
    public void apply(RequestTemplate template) {
        List<String> headerList = Arrays.asList(penetrateHeaders.split(SPLIT_CHARACTER));
        Set<String> headers = new HashSet<>();
        headerList.forEach(e -> headers.add(e.toLowerCase()));

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                for (String header : headers) {
                    if (StringUtils.equalsIgnoreCase(header, name)) {
                        template.header(name, values);
                        break;
                    }
                }
            }
        }
    }


}
