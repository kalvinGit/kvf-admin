package com.kalvin.kvf.dto.sys;

import com.kalvin.kvf.entity.sys.User;
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
