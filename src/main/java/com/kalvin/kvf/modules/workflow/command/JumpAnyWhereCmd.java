package com.kalvin.kvf.modules.workflow.command;

import com.kalvin.kvf.common.utils.SpringContextKit;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.*;

import java.util.Date;
import java.util.Map;

/**
 * 跳转任意节点命令
 * Create by Kalvin on 2020/5/1.
 */
public class JumpAnyWhereCmd implements Command<Void> {

    private final RepositoryService repositoryService = SpringContextKit.getBean(RepositoryService.class);

    private final String taskId;

    private final String targetNodeId;

    private String assignee;

    private final Map<String, Object> variables;

    private final String comment;

    /**
     * @param taskId 当前任务ID
     * @param targetNodeId 目标节点定义ID
     * @param variables 流转数据
     */
    public JumpAnyWhereCmd(String taskId, String targetNodeId, Map<String, Object> variables, String comment) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
        this.variables = variables;
        this.comment = comment;
    }
    /**
     * @param taskId 当前任务ID
     * @param targetNodeId 目标节点定义ID
     * @param assignee 审批用户
     * @param variables 流转数据
     */
    public JumpAnyWhereCmd(String taskId, String targetNodeId, String assignee, Map<String, Object> variables, String comment) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
        this.assignee = assignee;
        this.variables = variables;
        this.comment = comment;
    }


    @Override
    public Void execute(CommandContext commandContext) {
        // 获取任务实例管理类
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        // 获取当前任务实例
        TaskEntity currentTask = taskEntityManager.findById(this.taskId);

        // 获取当前节点的执行实例
        ExecutionEntity execution = currentTask.getExecution();
        String executionId = execution.getId();

        // 获取流程定义id
        String processDefinitionId = execution.getProcessDefinitionId();
        // 获取目标节点
//        Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        BpmnModel bpmnModel = this.repositoryService.getBpmnModel(processDefinitionId);
        Process process = bpmnModel.getProcesses().get(0);
        FlowElement flowElement = process.getFlowElement(this.targetNodeId);

        // 获取历史管理
        HistoryManager historyManager = commandContext.getHistoryManager();

        // 设置审批意见
        CommentEntityManager commentEntityManager = commandContext.getCommentEntityManager();
        CommentEntity commentEntity = commentEntityManager.create();
        commentEntity.setTaskId(this.taskId);
        commentEntity.setProcessInstanceId(currentTask.getProcessInstanceId());
        commentEntity.setMessage(this.comment);
        commentEntity.setFullMessage(this.comment);
        commentEntity.setTime(new Date());
        commentEntity.setType("comment");
        commentEntity.setAction("AddComment");
        commentEntityManager.insert(commentEntity);

        // 设置流转数据
        execution.setVariables(this.variables);

        if (null != assignee) {
            historyManager.recordTaskAssigneeChange(this.taskId, this.assignee);
        }

        // 通知当前活动结束(更新act_hi_actinst)
        historyManager.recordActivityEnd(execution, this.comment);
        // 通知任务节点结束(更新act_hi_taskinst)
        historyManager.recordTaskEnd(this.taskId, this.comment);
        // 删除正在执行的当前任务
        taskEntityManager.delete(taskId);

        // 此时设置执行实例的当前活动节点为目标节点
        execution.setCurrentFlowElement(flowElement);

        // 向operations中压入继续流程的操作类
        commandContext.getAgenda().planContinueProcessOperation(execution);

        return null;
    }
}
