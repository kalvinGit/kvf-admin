package com.kalvin.kvf.modules.dynamic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 多数据源测试接口层。可删除
 * 说明：
 * 1.此处使用的数据源为：slave_1（注意：此处实际跟主数据源是同一个数据库，因为没有新建数据库，只是为了测试）
 * 2.若需要更换其它数据库测试，请先修改application-dev.yml多数据源配置，更改为其它数据库
 * 3.@DS注解可作用在类上，也可作用在方法上，两者同时存在，方法上的注解优先级更高
 * Create by Kalvin on 2021/01/25.
 */
@DS("slave_1")
@Service
public class TestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List selectAll() {
        return jdbcTemplate.queryForList("select * from sys_user");
    }

    @DS("slave_1")
    public List selectByCondition() {
        return jdbcTemplate.queryForList("select * from sys_user where id > 1");
    }
}
