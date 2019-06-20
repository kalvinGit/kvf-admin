package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.sys.entity.RoleTree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色树表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface IRoleTreeService extends IService<RoleTree> {

    /**
     * 获取角色树列表
     * @param roleTree 参数实体
     * @return
     */
    Page<RoleTree> listRoleTreePage(RoleTree roleTree);

}
