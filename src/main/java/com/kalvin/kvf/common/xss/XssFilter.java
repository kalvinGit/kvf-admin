package com.kalvin.kvf.common.xss;

import com.kalvin.kvf.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 * Create by Kalvin on 2019/6/25.
 */
@Slf4j
public class XssFilter implements Filter {

    private String[] excludedUris;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init XssFilter...");
        excludedUris = filterConfig.getInitParameter(Constants.XSS_NOTICE_KEY).split(",");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpRequestWrapper xssRequest = new XssHttpRequestWrapper((HttpServletRequest) request);
        String url = xssRequest.getServletPath();
        if (isExcludedUri(url)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(xssRequest, response);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isExcludedUri(String uri) {
        if (excludedUris == null || excludedUris.length <= 0) {
            return false;
        }
        for (String ex : excludedUris) {
            uri = uri.trim();
            ex = ex.trim();
            if (uri.toLowerCase().matches(ex.toLowerCase().replace("*", ".*")))
                return true;
        }
        return false;
    }

}
