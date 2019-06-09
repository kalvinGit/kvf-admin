package com.kalvin.kvf.vo.sys;

import com.kalvin.kvf.entity.sys.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色带菜单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleMenuVO extends Role {
    private Long[] menuIds;
}
