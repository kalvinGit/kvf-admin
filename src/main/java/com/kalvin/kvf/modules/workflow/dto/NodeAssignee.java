package com.kalvin.kvf.modules.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 流程实例每个实例任务节点审批人
 * Create by Kalvin on 2020/4/20.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class NodeAssignee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskId;
    private String nodeId;
    private String assignee;

}
