package com.kalvin.kvf.modules.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.sys.entity.Role;
import com.kalvin.kvf.modules.sys.service.IRoleService;
import com.kalvin.kvf.modules.sys.vo.RoleMenuVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@RestController
@RequestMapping("sys/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @RequiresPermissions("sys:role:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/role");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id, boolean isRole) {
        ModelAndView mv = new ModelAndView("sys/role_edit");
        Role role;
        if (id == null) {
            role = new Role();
        } else {
            role = roleService.getById(id);
        }
        mv.addObject("editInfo", role);
        return mv;
    }

    @GetMapping(value = "permission")
    public ModelAndView permission() {
        return new ModelAndView("sys/role_permission");
    }

    @GetMapping(value = "list/data")
    public R listData(Role role) {
        Page<Role> page = roleService.listRolePage(role);
        return R.ok(page);
    }

    @GetMapping(value = "list/tree")
    public R listTree(Long parentId) {
        return R.ok(roleService.listRoleByParentId(parentId));
    }

    @RequiresPermissions("sys:role:add")
    @PostMapping(value = "add")
    public R add(Role role) {
        roleService.save(role);
        return R.ok();
    }

    @RequiresPermissions("sys:role:edit")
    @PostMapping(value = "edit")
    public R edit(Role role) {
        roleService.updateById(role);
        return R.ok();
    }

    @RequiresPermissions("sys:role:permission")
    @PostMapping(value = "set/permission")
    public R setPermission(RoleMenuVO roleMenuVO) {
        roleService.saveOrUpdatePermission(roleMenuVO);
        return R.ok();
    }

    @RequiresPermissions("sys:role:del")
    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        roleService.deleteWithChildren(id);
        return R.ok();
    }

    @RequiresPermissions("sys:role:del")
    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        roleService.deleteWithRoleMenu(ids);
        return R.ok();
    }

}

