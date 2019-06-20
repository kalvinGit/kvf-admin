package com.kalvin.kvf.modules.sys.vo;

import com.kalvin.kvf.common.entity.BasePageEntity;
import com.kalvin.kvf.modules.sys.entity.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户查询参数实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserQueryVO extends BasePageEntity {
    private String username;
    private String realname;
    private Integer status;
    private Long deptId;
    private List<Dept> depts;
}
