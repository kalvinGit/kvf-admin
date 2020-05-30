package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.sys.entity.Role;
import com.kalvin.kvf.modules.sys.vo.RoleMenuVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface IRoleService extends IService<Role> {

    /**
     * 获取菜单列表
     * @param role 参数实体
     * @return
     */
    Page<Role> listRolePage(Role role);

    /**
     * 保存/修改权限
     * @param roleMenuVO 角色菜单
     */
    void saveOrUpdatePermission(RoleMenuVO roleMenuVO);

    List<Role> listRoleByParentId(Long parentId);

    void deleteWithChildren(Long id);

    void deleteWithRoleMenu(List<Long> ids);

}
