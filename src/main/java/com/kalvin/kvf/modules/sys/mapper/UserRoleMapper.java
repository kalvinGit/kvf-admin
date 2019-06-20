package com.kalvin.kvf.modules.sys.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.sys.dto.UserRoleGroupDTO;
import com.kalvin.kvf.modules.sys.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> selectUserRoleList(UserRole userRole, Page page);

    UserRoleGroupDTO selectUserRoleGroupByUserId(Long userId);

}
