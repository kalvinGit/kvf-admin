package com.kalvin.kvf.modules.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.kalvin.kvf.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

/**
 * <p>
 * 表单设计表
 * </p>
 * @since 2020-05-02 19:32:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("wf_form")
public class Form extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表单代号
     */
    private String code;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 表单类型。0：简单表单；1：复杂表单；
     */
    private Integer type;

    /**
     * 表单主题。不配置默认为表单名称
     */
    private String theme;

    /**
     * 表单设计数据。
     */
    private String designData;

    /**
     * 表单js代码。仅当复杂表单才有
     */
    private String jsCode;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
