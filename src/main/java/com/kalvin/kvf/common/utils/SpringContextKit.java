package com.kalvin.kvf.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 作用】Spring上下文工具<br>
 * 说明：(无)
 *
 * @author kalvin
 * @Date 2019年04月08日 9:48
 */
@Component
public class SpringContextKit implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 可以用来获得：系统参数、环境变量参数 、properties/yml配置参数
     */
    public static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
        env = applicationContext.getEnvironment();
    }

    public static ApplicationContext applicationContext() {
        return applicationContext;
    }

    /**
     * 通过bean名称获取窗口bean
     * @param beanName
     * @param <BEAN>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <BEAN> BEAN getBean(String beanName) {
        return (BEAN) applicationContext.getBean(beanName);
    }

    /**
     * 通过class获取容器中的bean
     * @param beanClass
     * @param <BEAN>
     * @return
     */
    public static <BEAN> BEAN getBean(Class<BEAN> beanClass){
        return applicationContext.getBean(beanClass);
    }

}
