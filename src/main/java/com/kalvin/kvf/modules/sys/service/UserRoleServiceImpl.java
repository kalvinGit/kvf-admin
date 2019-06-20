package com.kalvin.kvf.modules.sys.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.modules.sys.dto.UserRoleGroupDTO;
import com.kalvin.kvf.modules.sys.entity.UserRole;
import com.kalvin.kvf.modules.sys.mapper.UserRoleMapper;
import com.kalvin.kvf.modules.sys.vo.UserRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public Page<UserRole> listUserRolePage(UserRole userRole) {
        Page<UserRole> page = new Page<>(userRole.getCurrent(), userRole.getSize());
        List<UserRole> userRoles = baseMapper.selectUserRoleList(userRole, page);
        return page.setRecords(userRoles);
    }

    @Override
    public List<UserRole> getUserRoleByRoleId(Long roleId) {
        return super.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
    }

    @Override
    public void saveOrUpdateBatchUserRole(UserRoleVO userRoleVO) {
        Long roleId = userRoleVO.getRoleId();
        List<Long> userIds = userRoleVO.getUserIds();
        List<UserRole> userRoles = new ArrayList<>();

        userIds.forEach(userId -> {
            UserRole userRole = super.getOne(new LambdaQueryWrapper<UserRole>()
                    .eq(UserRole::getRoleId, roleId).eq(UserRole::getUserId, userId));
            if (userRole == null) {
                userRole = new UserRole();
                userRole.setRoleId(roleId).setUserId(userId);
            }
            userRoles.add(userRole);
        });
        super.saveOrUpdateBatch(userRoles);
    }

    @Transactional
    @Override
    public void saveOrUpdateBatchUserRole(List<Long> roleIds, Long userId) {
        if (CollectionUtil.isEmpty(roleIds)) {
            super.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        } else {
            super.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId).notIn(UserRole::getRoleId, roleIds));
            List<UserRole> userRoles = new ArrayList<>();

            roleIds.forEach(roleId -> {
                UserRole userRole = super.getOne(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getRoleId, roleId).eq(UserRole::getUserId, userId));
                if (userRole == null) {
                    userRole = new UserRole();
                }
                userRole.setUserId(userId).setRoleId(roleId);
                userRoles.add(userRole);
            });
            super.saveOrUpdateBatch(userRoles);
        }
    }

    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return super.count(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
    }

    @Override
    public UserRoleGroupDTO getUserRoleGroupDTOByUserId(Long userId) {
        return baseMapper.selectUserRoleGroupByUserId(userId);
    }

    @Override
    public String getRoleIdsByUserId(Long userId) {
        return this.getUserRoleGroupDTOByUserId(userId).getRoleIds();
    }

    @Override
    public String getRoleNamesByUserId(Long userId) {
        UserRoleGroupDTO userRoleGroupDTOByUserId = this.getUserRoleGroupDTOByUserId(userId);
        return userRoleGroupDTOByUserId == null ? "" : userRoleGroupDTOByUserId.getRoleNames();
    }
}
