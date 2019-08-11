package com.kalvin.kvf.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kalvin.kvf.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 字典表
 * </p>
 * @since 2019-08-10 14:58:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 字典码
     */
    private String code;

    /**
     * 字典值
     */
    private String value;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
