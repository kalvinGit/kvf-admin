package com.kalvin.kvf.modules.workflow.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 流程实例流转数据
 * Create by Kalvin on 2020/4/20.
 */
@Getter
@Setter
@ToString
public class FlowData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String theme;   // 表单主题
    private String processName; // 流程名称
    private String processDefinitionId; // 流程定义ID
    private String processInstanceId;   // 流程实例ID
    private String executionId;  // 任务执行ID；在并发情况下是唯一的
    private String startUser;   // 流程发起人/申请人
    private String currentUserId;  // 当前用户ID
    private String currentUser;    // 当前用户名
    private String nextUser;  // 下一审批人
    private String ccUser;  // 抄送人
    private String taskId;  // 任务ID
    private String mainFormKey; // 主表单key
    private String formKey; // 任务表单key
    private String firstNodeId;  // 首节点ID
    private String currentNodeId;  // 当前节点ID
    private String currentNodeName; // 当前节点名称
    private String targetNodeId; // 目标任务节点ID
    private String comment; // 审批意见
    private String execType;    // 执行类型：默认正常，test为测试方式启动
//    private List<NodeAssignee> nodeAssignees;   // 每个节点的审批人
    private HashMap<String, NodeAssignee> nodeAssignee; // 每个节点的审批人
    private List<ProcessNode> nextNodes;   // 当前节点所有可能的出口路由节点列表
    private int nextNodeNum;   // 当前节点所有可能的出口路由节点个数
    private boolean agency; // 是否是代理申请
    private boolean firstNode;  // 是否首节点
    private boolean firstSubmit;    // 是否第一次已提交，用于判断流程首环节是否回退过
    private Date firstSubmitTime;   // 申请时间
    private List<ProcessNode> canBackNodes; // 当前节点所有可驳回的节点列表
    private boolean readOnly = false;   // 只读

}
