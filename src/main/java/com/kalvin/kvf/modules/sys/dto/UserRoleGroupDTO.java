package com.kalvin.kvf.modules.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserRoleGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String roleIds;
    private String roleNames;
}
