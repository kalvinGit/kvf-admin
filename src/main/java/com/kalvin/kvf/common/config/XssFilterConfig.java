package com.kalvin.kvf.common.config;

import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * XssFilter配置
 * Create by Kalvin on 2019/6/25.
 */
@Configuration
public class XssFilterConfig {

    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(Integer.MAX_VALUE - 1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        // excludes用于配置不需要参数过滤的请求url
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        // isIncludeRichText主要用于设置富文本内容是否需要过滤  TODO: 好像无效
        initParameters.put("isIncludeRichText", "true");
        // 配置过滤URL白名单
        initParameters.put(Constants.XSS_NOTICE_KEY, "");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
}
