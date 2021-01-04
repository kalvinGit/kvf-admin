package com.kalvin.kvf.modules.sys.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.common.constant.SysConstant;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.common.utils.CryptionKit;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.modules.sys.dto.UserEditDTO;
import com.kalvin.kvf.modules.sys.dto.UserRoleGroupDTO;
import com.kalvin.kvf.modules.sys.entity.Dept;
import com.kalvin.kvf.modules.sys.entity.User;
import com.kalvin.kvf.modules.sys.service.IDeptService;
import com.kalvin.kvf.modules.sys.service.IUserRoleService;
import com.kalvin.kvf.modules.sys.service.IUserService;
import com.kalvin.kvf.modules.sys.vo.UserQueryVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@RestController
@RequestMapping("sys/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUserRoleService userRoleService;

    @RequiresPermissions("sys:user:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/user");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("sys/user_edit");
        UserEditDTO userEditDTO = new UserEditDTO();
        UserRoleGroupDTO userRoleGroupDTO = new UserRoleGroupDTO();
        if (id != null) {
            User user = userService.getById(id);
            Dept dept = deptService.getById(user.getDeptId());
            userRoleGroupDTO = userRoleService.getUserRoleGroupDTOByUserId(id);
            BeanUtil.copyProperties(user, userEditDTO);
            userEditDTO.setDeptName(dept == null ? "" : dept.getName());
        }
        userEditDTO.setUserRole(userRoleGroupDTO);
        mv.addObject("editInfo", userEditDTO);
        return mv;
    }

    @GetMapping(value = "info")
    public ModelAndView info() {
        ModelAndView mv = new ModelAndView("sys/user_info");
        User user = userService.getById(ShiroKit.getUserId());
        mv.addObject("user", user);
        return mv;
    }

    @GetMapping(value = "password")
    public ModelAndView password() {
        return new ModelAndView("sys/user_pwd");
    }

    @GetMapping(value = "list/data")
    public R listData(UserQueryVO queryVO) {
        Page<User> page = userService.listUserPage(queryVO);
        return R.ok(page);
    }

    @RequiresPermissions("sys:user:add")
    @Transactional
    @PostMapping(value = "add")
    public R add(User user, @RequestParam("roleIds") List<Long> roleIds) {
        userService.addUser(user, roleIds);
        return R.ok();
    }

    @RequiresPermissions("sys:user:edit")
    @Transactional
    @PostMapping(value = "edit")
    public R edit(User user, @RequestParam("roleIds") List<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return R.ok();
    }

    @PostMapping(value = "updateInfo")
    public R updateInfo(User user) {
        userService.updateById(user);
        return R.ok();
    }

    @RequiresPermissions("sys:user:del")
    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user.getUsername().equals(SysConstant.ADMIN)) {
            return R.fail("不允许删除超级管理员【" + SysConstant.ADMIN + "】用户");
        }
        userService.removeById(id);
        return R.ok();
    }

    @RequiresPermissions("sys:user:del")
    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        userService.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 管理员重置某个用户密码
     * @param id 用户ID
     * @return
     */
    @RequiresPermissions("sys:user:reset")
    @PostMapping(value = "{id}/resetPwd")
    public R resetPwd(@PathVariable Long id) {
        userService.updateUserPassword(id, CryptionKit.genUserPwd());
        return R.ok();
    }

    /**
     * 用户修改密码
     * @param oldPassword 旧密码
     * @param password  新密码
     * @return
     */
    @PostMapping(value = "changePwd")
    public R changePwd(String oldPassword, String password) {
        if (StrUtil.isBlank(oldPassword) && StrUtil.isBlank(password)) {
            return R.fail("修改失败，非法的参数");
        }
        // 用户修改密码
        User user = userService.getById(ShiroKit.getUserId());
        oldPassword = CryptionKit.genUserPwd(oldPassword);
        if (user.getPassword().equals(oldPassword)) {
            password = CryptionKit.genUserPwd(password);
            if (user.getPassword().equals(password)) {
                return R.fail("新密码不能与旧密码相同");
            }
        } else {
            return R.fail("旧密码不正确");
        }
        userService.updateUserPassword(ShiroKit.getUserId(), password);
        return R.ok();
    }

}

