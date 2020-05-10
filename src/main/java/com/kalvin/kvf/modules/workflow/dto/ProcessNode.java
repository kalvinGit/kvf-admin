package com.kalvin.kvf.modules.workflow.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 流程节点数据
 * Create by Kalvin on 2020/4/20.
 */
@Data
@Accessors(chain = true)
public class ProcessNode implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nodeId;
    private String nodeName;
    private String assignee;
    private String candidateUserIds;
    private String candidateGroupIds;

    public ProcessNode(String nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }
}
