package com.kalvin.kvf.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * zTree 数据实体类
 */

@Data
@Accessors(chain = true)
public class ZTreeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;
    /**
     * 父ID
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否是父节点
     */
    private boolean parent;
    /**
     * 是否选中
     */
    private boolean checked;
    /**
     * 是否打开
     */
    private boolean open;
}
