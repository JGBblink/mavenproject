package com.demo.main.config.interceptor;

import java.util.Objects;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestContext {

    public static <T> T getRequestAttr(String name, Class<T> clazz) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Object attribute = requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        return Objects.isNull(attribute) ? null : (T) attribute;
    }

    public static void setRequestAttr(String name, Object value) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
    }

    public static void restAttr() {
        RequestContextHolder.resetRequestAttributes();
    }
}
