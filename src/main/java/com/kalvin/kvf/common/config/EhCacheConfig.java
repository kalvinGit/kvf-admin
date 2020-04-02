package com.kalvin.kvf.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 缓存配置
 */
@EnableCaching
@Configuration
@ConditionalOnProperty(name = "kvf.cache.enable", havingValue = "true")
public class EhCacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));     // 缓存信息配置文件
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

    /**
     * ehcache 主要的管理器
     *
     * @param cacheManagerFactoryBean cm
     * @return ec
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean cacheManagerFactoryBean) {
        return new EhCacheCacheManager(cacheManagerFactoryBean.getObject());
    }
}
