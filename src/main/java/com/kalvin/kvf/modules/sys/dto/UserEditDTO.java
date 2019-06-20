package com.kalvin.kvf.modules.sys.dto;

import com.kalvin.kvf.modules.sys.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserEditDTO extends User {
    private String deptName;
    private UserRoleGroupDTO userRole;
}
