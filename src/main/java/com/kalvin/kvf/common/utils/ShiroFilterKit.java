package com.kalvin.kvf.common.utils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShiroFilterKit {

    /**
     * 是否是Ajax请求,如果是ajax请求响应头会有，x-requested-with
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     * 设置超时
     * @param servletResponse
     */
    public static void out(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        // 在响应头设置session状态
        response.setHeader("session-status", "timeout");
        response.flushBuffer();
    }

}
