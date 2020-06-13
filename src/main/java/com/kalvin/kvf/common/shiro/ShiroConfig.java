package com.kalvin.kvf.common.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.codec.Base64;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 作用：Shiro配置<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 11:29
 */
@Configuration
public class ShiroConfig {

    /**
     * 集成缓存
     * @return sessionManager
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间(单位：毫秒)，默认为15分钟
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 15);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        // 自定义缓存实现
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager);

        // 设置cookie管理
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    /**
     * shiro缓存管理器。需要添加到securityManager中
     * @return cacheManager
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * Cookie对象
     * @return simpleCookie
     */
    private SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 记住时间，单位秒，默认15天
        simpleCookie.setMaxAge(60 * 60 * 24 * 15);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * @return cookieRememberMeManager
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        /*重要，设置自定义拦截器，当访问某些自定义url时，使用这个filter进行验证*/
        Map<String, Filter> filters = new LinkedHashMap<>();
        // 如果map里面key值为authc,表示所有名为authc的过滤条件使用这个自定义的filter
        // map里面key值为LoginFilter,表示所有名为LoginFilter的过滤条件使用这个自定义的filter，具体见下方
        filters.put("myFilter", new LoginFilter());
        shiroFilter.setFilters(filters);

        shiroFilter.setLoginUrl("/login");
//        shiroFilter.setSuccessUrl("/sys/index");
        shiroFilter.setUnauthorizedUrl("/403");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/static/**", "anon");
        filterMap.put("/login/**", "anon");
//        filterMap.put("/logout", "logout");   // 暂不使用Shiro自带的登出
        filterMap.put("/druid/**", "anon");
        filterMap.put("/captcha", "anon");
//        filterMap.put("/", "user");

        filterMap.put("/**", "myFilter,authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 开启shiro标签
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
