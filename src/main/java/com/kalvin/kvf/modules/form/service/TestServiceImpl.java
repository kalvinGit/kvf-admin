package com.kalvin.kvf.modules.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.kalvin.kvf.modules.form.entity.Test;
import com.kalvin.kvf.modules.form.mapper.TestMapper;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 * @since 2022-02-08 15:27:25
 */
@Service("testService2")
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    @Override
    public Page<Test> listTestPage(Test test) {
        Page<Test> page = new Page<>(test.getCurrent(), test.getSize());
        List<Test> tests = baseMapper.selectTestList(test, page);
        return page.setRecords(tests);
    }

}
