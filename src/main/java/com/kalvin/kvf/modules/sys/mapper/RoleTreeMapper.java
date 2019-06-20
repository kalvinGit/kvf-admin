package com.kalvin.kvf.modules.sys.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.sys.entity.RoleTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色树表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface RoleTreeMapper extends BaseMapper<RoleTree> {

    List<RoleTree> selectRoleTreeList(RoleTree roleTree, Page page);

}
