package com.kalvin.kvf.modules.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;
import com.kalvin.kvf.modules.workflow.mapper.ProcessFormMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程表单关联表 服务实现类
 * </p>
 * @since 2020-05-05 12:14:20
 */
@Service
public class ProcessFormServiceImpl extends ServiceImpl<ProcessFormMapper, ProcessForm> implements ProcessFormService {

    @Override
    public Page<ProcessForm> listProcessFormPage(ProcessForm processForm) {
        Page<ProcessForm> page = new Page<>(processForm.getCurrent(), processForm.getSize());
        List<ProcessForm> processForms = baseMapper.selectProcessFormList(processForm, page);
        return page.setRecords(processForms);
    }

    @Override
    public ProcessForm getByModelId(String modelId) {
        return super.getOne(new LambdaQueryWrapper<ProcessForm>().eq(ProcessForm::getModelId, modelId));
    }

}
