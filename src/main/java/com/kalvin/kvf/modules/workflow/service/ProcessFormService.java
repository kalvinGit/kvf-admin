package com.kalvin.kvf.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;

/**
 * <p>
 * 流程表单关联表 服务类
 * </p>
 * @since 2020-05-05 12:14:20
 */
public interface ProcessFormService extends IService<ProcessForm> {

    /**
     * 获取列表。分页
     * @param processForm 查询参数
     * @return page
     */
    Page<ProcessForm> listProcessFormPage(ProcessForm processForm);

    ProcessForm getByModelId(String modelId);

}
