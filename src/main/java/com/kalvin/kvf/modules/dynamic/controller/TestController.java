package com.kalvin.kvf.modules.dynamic.controller;

import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.dynamic.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多数据源测试接口层。可删除
 * 登录后访问:http://localhost/dynamic/test/get/all
 * 说明：
 * 1.此处使用的数据源为：slave_1（注意：此处实际跟主数据源是同一个数据库，因为没有新建数据库，只是为了测试）
 * 2.若需要更换其它数据库测试，请先修改application-dev.yml多数据源配置，更改为其它数据库
 * 3.@DS注解可作用在类上，也可作用在方法上，两者同时存在，方法上的注解优先级更高
 * Create by Kalvin on 2021/01/25.
 */
@RestController
@RequestMapping(value = "dynamic/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping(value = "get/all")
    public R getAll() {
        return R.ok(testService.selectAll());
    }

    @GetMapping(value = "get/conditions")
    public R getByConditions() {
        return R.ok(testService.selectByCondition());
    }
}
