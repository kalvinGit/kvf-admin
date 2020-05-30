package com.kalvin.kvf.modules.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.sys.entity.Dept;
import com.kalvin.kvf.modules.sys.service.IDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@RestController
@RequestMapping("sys/dept")
public class DeptController extends BaseController {

    @Autowired
    private IDeptService deptService;

    @RequiresPermissions("sys:dept:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/dept");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("sys/dept_edit");
        Dept dept;
        if (id == null) {
            dept = new Dept();
        } else {
            dept = deptService.getById(id);
        }
        mv.addObject("editInfo", dept);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(Dept dept) {
        Page<Dept> page = deptService.listDeptPage(dept);
        return R.ok(page);
    }

    @GetMapping(value = "list/tree")
    public R listTree(Long parentId) {
        return R.ok(deptService.listDeptByParentId(parentId));
    }

    @RequiresPermissions("sys:dept:add")
    @PostMapping(value = "add")
    public R add(Dept dept) {
        deptService.save(dept);
        return R.ok();
    }

    @RequiresPermissions("sys:dept:edit")
    @PostMapping(value = "edit")
    public R edit(Dept dept) {
        deptService.updateById(dept);
        return R.ok();
    }

    @RequiresPermissions("sys:dept:del")
    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        deptService.deleteWithChildren(id);
        return R.ok();
    }

    @RequiresPermissions("sys:dept:del")
    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        deptService.removeByIds(ids);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R get(@PathVariable Long id) {
        return R.ok(deptService.getById(id));
    }
}

