package com.kalvin.kvf.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;
import com.kalvin.kvf.modules.workflow.service.ProcessFormService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * <p>
 * 流程表单关联表 前端控制器
 * </p>
 * @since 2020-05-05 12:14:20
 */
@RestController
@RequestMapping("workflow/processForm")
public class ProcessFormController extends BaseController {

    @Autowired
    private ProcessFormService processFormService;

    @RequiresPermissions("workflow:processForm:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("workflow/processForm");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("workflow/processForm_edit");
        ProcessForm processForm;
        if (id == null) {
            processForm = new ProcessForm();
        } else {
            processForm = processFormService.getById(id);
        }
        mv.addObject("editInfo", processForm);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(ProcessForm processForm) {
        Page<ProcessForm> page = processFormService.listProcessFormPage(processForm);
        return R.ok(page);
    }

    @RequiresPermissions("workflow:process:setting")
    @PostMapping(value = "add")
    public R add(ProcessForm processForm) {
        processFormService.save(processForm);
        return R.ok();
    }

    /*@RequiresPermissions("workflow:process:setting")
    @PostMapping(value = "batchdel")
    public R batchdel(@RequestParam("ids") List<Long> ids) {
        processFormService.removeByIds(ids);
        return R.ok();
    }*/

    @RequiresPermissions("workflow:process:setting")
    @PostMapping(value = "edit")
    public R edit(ProcessForm processForm) {
        processFormService.updateById(processForm);
        return R.ok();
    }

    @RequiresPermissions("workflow:process:setting")
    @PostMapping(value = "del/{id}")
    public R del(@PathVariable Long id) {
        processFormService.removeById(id);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R get(@PathVariable Long id) {
        return R.ok(processFormService.getById(id));
    }

}

