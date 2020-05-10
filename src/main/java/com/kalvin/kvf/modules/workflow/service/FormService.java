package com.kalvin.kvf.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.workflow.entity.Form;
import com.kalvin.kvf.modules.workflow.vo.FormConfigVO;

import java.util.Map;

/**
 * <p>
 * 表单设计表 服务类
 * </p>
 * @since 2020-05-02 19:36:28
 */
public interface FormService extends IService<Form> {

    /**
     * 获取列表。分页
     * @param form 查询参数
     * @return page
     */
    Page<Form> listFormPage(Form form);

    void saveForm(Form form);

    FormConfigVO getFormConfigById(Long id);

    FormConfigVO getFormConfigByCode(String code);

    FormConfigVO getFormConfig(String code, Map<String, Object> variables);

    FormConfigVO getFormConfig(Form form, Map<String, Object> variables);

    Form getByCode(String code);

}
