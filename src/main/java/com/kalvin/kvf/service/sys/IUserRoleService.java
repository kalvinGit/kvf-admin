package com.kalvin.kvf.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.dto.sys.UserRoleGroupDTO;
import com.kalvin.kvf.entity.sys.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.vo.sys.UserRoleVO;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface IUserRoleService extends IService<UserRole> {

    Page<UserRole> listUserRolePage(UserRole userRole);

    List<UserRole> getUserRoleByRoleId(Long roleId);

    void saveOrUpdateBatchUserRole(UserRoleVO userRoleVO);

    void saveOrUpdateBatchUserRole(List<Long> roleIds, Long userId);

    int countUserRoleByRoleId(Long roleId);

    UserRoleGroupDTO getUserRoleGroupDTOByUserId(Long userId);

    String getRoleIdsByUserId(Long userId);

    String getRoleNamesByUserId(Long userId);

}
