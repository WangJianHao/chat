package com.sen.chat.common.interceptor;

import com.sen.chat.common.domain.RequestInfo;

/**
 * 当前请求上下文信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/30 16:25
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
