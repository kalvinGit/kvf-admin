package com.kalvin.kvf.common.shiro;

import com.kalvin.kvf.common.utils.ShiroFilterKit;
import com.kalvin.kvf.common.utils.ShiroKit;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author kalvin
 */
@Component
public class LoginFilter extends AccessControlFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return ShiroKit.isLogin();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (ShiroFilterKit.isAjax(request)) {
            LOGGER.info("当前用户没有登录，并且是Ajax请求");
            ShiroFilterKit.out(response);
            return false;
        }
        // 保存Request和Response 到登录后的链接
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }
}
