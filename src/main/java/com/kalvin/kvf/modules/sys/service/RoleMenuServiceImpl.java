package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.modules.sys.entity.RoleMenu;
import com.kalvin.kvf.modules.sys.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    public void deleteByMenuId(Long menuId) {
        super.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId));
    }

    @Override
    public void deleteByMenuIds(List<Long> menuIds) {
        super.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getMenuId, menuIds));
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        super.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
    }

    @Override
    public void deleteByRoleIds(List<Long> roleIds) {
        super.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, roleIds));
    }
}
