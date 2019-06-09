package com.kalvin.kvf.mapper.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.dto.sys.UserRoleGroupDTO;
import com.kalvin.kvf.entity.sys.UserRole;
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
