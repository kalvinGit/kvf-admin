package com.kalvin.kvf.modules.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.form.entity.Test;

/**
 * <p>
 *  服务类
 * </p>
 * @since 2022-02-08 15:27:25
 */
public interface TestService extends IService<Test> {

    /**
     * 获取列表。分页
     * @param test 查询参数
     * @return page
     */
    Page<Test> listTestPage(Test test);

}
