package com.kalvin.kvf.modules.form.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.form.entity.Test;
import com.kalvin.kvf.modules.form.service.TestService;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 * @since 2022-02-08 15:27:25
 */
@RestController
@RequestMapping("t/test")
public class TestController2 extends BaseController {

    @Autowired
    private TestService testService;

    @RequiresPermissions("t:test:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("t/test");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("t/test_edit");
        Test test;
        if (id == null) {
            test = new Test();
        } else {
            test = testService.getById(id);
        }
        mv.addObject("editInfo", test);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(Test test) {
        Page<Test> page = testService.listTestPage(test);
        return R.ok(page);
    }

    @RequiresPermissions("t:test:add")
    @PostMapping(value = "add")
    public R add(Test test) {
        testService.save(test);
        return R.ok();
    }

    @RequiresPermissions("t:test:del")
    @PostMapping(value = "batchdel")
    public R batchdel(@RequestParam("ids") List<Long> ids) {
        testService.removeByIds(ids);
        return R.ok();
    }

    @RequiresPermissions("t:test:edit")
    @PostMapping(value = "edit")
    public R edit(Test test) {
        testService.updateById(test);
        return R.ok();
    }

    @RequiresPermissions("t:test:del")
    @PostMapping(value = "del/{id}")
    public R del(@PathVariable Long id) {
        testService.removeById(id);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R get(@PathVariable Long id) {
        return R.ok(testService.getById(id));
    }

}

