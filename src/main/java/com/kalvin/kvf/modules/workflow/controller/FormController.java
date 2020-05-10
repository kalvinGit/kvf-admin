package com.kalvin.kvf.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.workflow.entity.Form;
import com.kalvin.kvf.modules.workflow.service.FormService;
import com.kalvin.kvf.modules.workflow.vo.FormConfigVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * <p>
 * 表单设计表 前端控制器
 * </p>
 * @since 2020-05-02 19:36:28
 */
@RestController
@RequestMapping("workflow/form")
public class FormController extends BaseController {

    @Autowired
    private FormService formService;

    @RequiresPermissions("workflow:form:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("workflow/form");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("workflow/form_edit");
        Form form;
        if (id == null) {
            form = new Form();
        } else {
            form = formService.getById(id);
        }
        mv.addObject("editInfo", form);
        return mv;
    }

    @RequiresPermissions("workflow:form:prev")
    @GetMapping(value = "/{id}/prev")
    public ModelAndView prevForm(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("workflow/form_prev");
        FormConfigVO formConfigVO = formService.getFormConfigById(id);
        mv.addObject("formConfig", formConfigVO);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(Form form) {
        Page<Form> page = formService.listFormPage(form);
        return R.ok(page);
    }

    @RequiresPermissions("workflow:form:add")
    @PostMapping(value = "add")
    public R add(Form form) {
        formService.saveForm(form);
        return R.ok();
    }

    @RequiresPermissions("workflow:form:delete")
    @PostMapping(value = "batchdel")
    public R batchdel(@RequestParam("ids") List<Long> ids) {
        formService.removeByIds(ids);
        return R.ok();
    }

    @RequiresPermissions("workflow:form:edit")
    @PostMapping(value = "edit")
    public R edit(Form form) {
        formService.updateById(form);
        return R.ok();
    }

    @RequiresPermissions("workflow:form:delete")
    @PostMapping(value = "del/{id}")
    public R del(@PathVariable Long id) {
        formService.removeById(id);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R get(@PathVariable Long id) {
        return R.ok(formService.getById(id));
    }

}

