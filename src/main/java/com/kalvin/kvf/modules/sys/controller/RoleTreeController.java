package com.kalvin.kvf.modules.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.sys.entity.RoleTree;
import com.kalvin.kvf.modules.sys.service.IRoleTreeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kalvin.kvf.common.controller.BaseController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 角色树表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@RestController
@RequestMapping("sys/roleTree")
public class RoleTreeController extends BaseController {

    @Autowired
    private IRoleTreeService roleTreeService;

    @RequiresPermissions("sys:roleTree:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/role_tree");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("sys/role_tree_edit");
        RoleTree roleTree;
        if (id == null) {
            roleTree = new RoleTree();
        } else {
            roleTree = roleTreeService.getById(id);
        }
        mv.addObject("editInfo", roleTree);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(RoleTree roleTree) {
        Page<RoleTree> page = roleTreeService.listRoleTreePage(roleTree);
        return R.ok(page);
    }

    /**
     * 角色分类树数据接口
     * 暂时只允许一层树结构
     * @return
     */
    @GetMapping(value = "list/tree")
    public R listTree() {
        return R.ok(roleTreeService.list());
    }

    @RequiresPermissions("sys:roleTree:edit")
    @PostMapping(value = "save")
    public R save(RoleTree roleTree) {
        roleTreeService.saveOrUpdate(roleTree);
        return R.ok();
    }

    @RequiresPermissions("sys:roleTree:del")
    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        roleTreeService.removeById(id);
        return R.ok();
    }

    @RequiresPermissions("sys:roleTree:del")
    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        roleTreeService.removeByIds(ids);
        return R.ok();
    }

}

