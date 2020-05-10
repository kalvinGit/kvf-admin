package com.kalvin.kvf.modules.workflow.vo;

import com.kalvin.kvf.common.entity.BasePageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by Kalvin on 2020/4/21.
 */
@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ProcessQueryVO extends BasePageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String modelName;
    private String processName;
    private String taskName;
    private String titleName;
    private String key;
    private Integer status;
    private Date createTime;

}
