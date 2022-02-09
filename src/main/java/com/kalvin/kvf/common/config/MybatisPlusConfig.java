package com.kalvin.kvf.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 作用：mybatisPlus配置<br>
 * 说明：(无)
 *
 * @author kalvin
 * @Date 2019年04月08日 16:47
 */
@Configuration
@MapperScan("com.kalvin.kvf.modules.*.mapper")
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Resource
    private DataSource dataSource;

    /**
     * 配置事物管理器
     * @return DataSourceTransactionManager
     */
    @Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 配置分页插件
     * @return page
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

}
