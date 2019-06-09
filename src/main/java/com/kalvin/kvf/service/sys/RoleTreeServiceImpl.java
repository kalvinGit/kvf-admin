package com.kalvin.kvf.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.entity.sys.RoleTree;
import com.kalvin.kvf.mapper.sys.RoleTreeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色树表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Service
public class RoleTreeServiceImpl extends ServiceImpl<RoleTreeMapper, RoleTree> implements IRoleTreeService {

    @Override
    public Page<RoleTree> listRoleTreePage(RoleTree roleTree) {
        Page<RoleTree> page = new Page<>(roleTree.getCurrent(), roleTree.getSize());
        List<RoleTree> menus = baseMapper.selectRoleTreeList(roleTree, page);
        return page.setRecords(menus);
    }
}
