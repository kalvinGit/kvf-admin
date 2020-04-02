package com.kalvin.kvf.common.config;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 自定义redis key生成策略
 * Create by Kalvin on 2020/4/1.
 */
public class RedisCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(o.getClass().getSimpleName());
        stringBuilder.append(".");
        stringBuilder.append(method.getName());
        stringBuilder.append("[");
        for (Object obj : objects) {
            stringBuilder.append(obj.toString());
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
