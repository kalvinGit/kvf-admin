package com.kalvin.kvf.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kalvin.kvf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色树表
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role_tree")
public class RoleTree extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父角色树ID，一级角色树为0
     */
    private Long parentId;

    /**
     * 角色树名称
     */
    private String name;

    /**
     * 排序值。越小越靠前
     */
    private Integer sort;


}
