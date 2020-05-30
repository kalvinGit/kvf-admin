package com.kalvin.kvf.modules.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.sys.entity.Menu;
import com.kalvin.kvf.modules.sys.service.IMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@RestController
@RequestMapping("sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @RequiresPermissions("sys:menu:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/menu");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("sys/menu_edit");
        Menu menu;
        if (id == null) {
            menu = new Menu();
        } else {
            menu = menuService.getById(id);
        }
        mv.addObject("editInfo", menu);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(Menu menu) {
        Page<Menu> page = menuService.listMenuPage(menu);
        return R.ok(page);
    }

    @GetMapping(value = "list/treeData")
    public R listTreeData(Menu menu) {
        return R.ok(menuService.listMenuTree(menu));
    }

    @GetMapping(value = "list/tree")
    public R listTree(Long id) {
        return R.ok(menuService.listMenuByParentId(id));
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping(value = "save")
    public R save(Menu menu) {
        menuService.saveOrUpdate(menu);
        return R.ok();
    }

    @RequiresPermissions("sys:menu:add")
    @PostMapping(value = "add")
    public R add(Menu menu) {
        menuService.save(menu);
        return R.ok();
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping(value = "edit")
    public R edit(Menu menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @RequiresPermissions("sys:menu:del")
    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        menuService.deleteWithChildren(id);
        return R.ok();
    }

    @RequiresPermissions("sys:menu:del")
    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        menuService.deleteWithRoleMenu(ids);
        return R.ok();
    }

    /**
     * 角色菜单列表接口
     * @param roleId 角色ID
     * @return
     */
    @GetMapping(value = "role/tree")
    public R roleTree(Long roleId) {
        return R.ok(menuService.listRoleMenu(roleId));
    }

}

