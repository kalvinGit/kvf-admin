package com.kalvin.kvf.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.kalvin.kvf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区域表
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_area")
public class Area extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 拼音首字母
     */
    private String nameSpell;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 排序值。越小越靠前
     */
    private Integer sort;

    /**
     * 类型。0：国家；1：省直辖市；2：市；3：区县
     */
    private Integer type;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
