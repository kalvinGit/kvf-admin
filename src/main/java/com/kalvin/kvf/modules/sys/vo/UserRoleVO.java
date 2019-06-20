package com.kalvin.kvf.modules.sys.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【作用】用户角色参数<br>
 * 【说明】（无）
 * @author Kalvin
 * @Date 2019/5/9 20:50
 */
@Data
@Accessors(chain = true)
@ToString
public class UserRoleVO {
    private Long roleId;
    private List<Long> userIds;
}
