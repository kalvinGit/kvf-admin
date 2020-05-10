package com.kalvin.kvf.modules.workflow.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by Kalvin on 2020/4/22.
 */
@Data
@ToString
@Accessors(chain = true)
public class ProcessModelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;  // 模型ID
    private String name;    // 流程模型名称
    private String key;
    private String version;
    private String deploymentId;
    private Date createTime;
    private Date lastUpdateTime;

    private String processName;
    private String processDefinitionId;
    private Boolean processSuspended;   // 流程是否被挂起


}
