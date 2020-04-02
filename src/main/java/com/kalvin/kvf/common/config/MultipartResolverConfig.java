package com.kalvin.kvf.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by Kalvin on 2019/7/12.
 */
@Configuration
public class MultipartResolverConfig extends CommonsMultipartResolver {

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        String url = request.getRequestURI();
        // 解决ueditor上传文件被清空multipart对象问题
        if (url != null && url.contains("ueditor/api")) {
            return false;
        } else {
            return super.isMultipart(request);
        }
    }

}
