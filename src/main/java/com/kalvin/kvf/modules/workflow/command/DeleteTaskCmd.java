package com.kalvin.kvf.modules.workflow.command;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;

/**
 * Create by Kalvin on 2020/5/1.
 */
public class DeleteTaskCmd extends NeedsActiveTaskCmd<String> {

    public DeleteTaskCmd(String taskId){
        super(taskId);
    }

    @Override
    public String execute(CommandContext commandContext, TaskEntity currentTask){
        //获取所需服务
        TaskEntityManagerImpl taskEntityManager = (TaskEntityManagerImpl)commandContext.getTaskEntityManager();
        //获取当前任务的来源任务及来源节点信息
        ExecutionEntity executionEntity = currentTask.getExecution();
        //删除当前任务,来源任务
        taskEntityManager.deleteTask(currentTask, "撤回审批", false, false);
        return executionEntity.getId();
    }

    public String getSuspendedTaskException() {
        return "挂起的任务不能跳转";
    }

}
