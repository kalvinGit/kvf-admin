package com.kalvin.kvf.modules.workflow.service.api;

import java.util.List;
import java.util.Map;

/**
 * Create by Kalvin on 2020/4/20.
 */
public interface IProcessEngine {

    /**
     * 启动流程
     * @param deploymentId 流程发布ID
     * @return 活动任务ID
     */
    String start(String deploymentId);

    /**
     * 启动流程（带业务ID）
     * @param deploymentId 流程发布ID
     * @param businessId 业务ID
     * @return 活动任务ID
     */
    String start(String deploymentId, String businessId);

    /**
     * 启动流程（带业务ID）
     * @param deploymentId 流程发布ID
     * @param businessId 业务ID
     * @param startUser 流程发起人
     * @return 活动任务ID
     */
    String start(String deploymentId, String businessId, String startUser);

    /**
     * 提交任务
     * @param flowVariables 流程表单流转数据
     */
    void submitTask(Map<String, Object> flowVariables);

    /**
     * 根据一个流程实例的id挂起流程实例
     * @param processInstanceId 流程实例id
     */
    void suspendProcessInstance(String processInstanceId);

    /**
     * 根据一个流程实例的id激活流程实例
     *
     * @param processInstanceId 流程实例id
     */
    void activateProcessInstance(String processInstanceId);

    /**
     * 根据发布id批量挂起流程
     * @param deploymentIds 发布ID
     */
    void suspendProcessDefinitionByIds(List<String> deploymentIds);

    /**
     * 根据发布id批量激活流程
     *
     * @param deploymentIds 发布ID
     */
    void activateProcessDefinitionByIds(List<String> deploymentIds);

    /**
     * 撤回审批
     * @param oldTaskId 已审批的任务ID
     */
    void withdrawApproval(String oldTaskId);

    /**
     * 获取当前用户任务流转数据
     * @param taskId 当前任务ID
     * @return map
     */
    Map<String, Object> getCurrentUserTaskVariables(String taskId);

    /**
     * 获取历史用户任务流转数据
     * @param processInstanceId 流程实例ID
     * @return map
     */
    Map<String, Object> getHisUserTaskVariables(String processInstanceId);

    /**
     * 回退首环节
     * @param taskId 任务ID
     */
    void backToFirstNode(String taskId);

    /**
     * 回退上一环节
     * @param taskId 任务ID
     */
    void backToPreNode(String taskId);

    /**
     * 回退某环节
     * @param taskId 任务ID
     * @param targetNodeId 目标节点ID
     */
    void back2Node(String taskId, String targetNodeId);
}
