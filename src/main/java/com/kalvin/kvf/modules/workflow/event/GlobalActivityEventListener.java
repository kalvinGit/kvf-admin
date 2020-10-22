package com.kalvin.kvf.modules.workflow.event;

import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.modules.workflow.dto.FlowData;
import com.kalvin.kvf.modules.workflow.utils.ProcessKit;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;

/**
 * activiti全局事件监听器
 * Create by Kalvin on 2020/4/20.
 */
@Slf4j
@Configuration
public class GlobalActivityEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        if (ActivitiEventType.TASK_CREATED.equals(event.getType())) {
            log.debug("ExecutionId = {}, ProcessDefinitionId = {}, ProcessInstanceId = {}", event.getExecutionId(), event.getProcessDefinitionId(), event.getProcessInstanceId());
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
            Object entity = entityEvent.getEntity();
            if(entity instanceof TaskEntity) {
                TaskEntity task = (TaskEntity) entity;
                log.debug("TASK_CREATED task={}", task.getTaskDefinitionKey());
                String nextUser = task.getAssignee();   // 节点已配置的审批人
                Set<IdentityLink> candidates = task.getCandidates();    // 节点已配置的审批人候选组

                FlowData flowData = ProcessKit.getFlowData(task.getId());
                log.debug("flowData = {}", flowData.toString());

                // TODO:  节点已配置的审批人
                log.debug("流程节点已配置的审批人：{}", nextUser);
                // 优先使用前端传过来的审批人
                if (StrUtil.isNotBlank(flowData.getNextUser())) {
                    nextUser = flowData.getNextUser();
                    log.info("nextUser = {}", nextUser);
                }

                // 设置审批人
                if (StrUtil.isNotBlank(nextUser)) {
                    if (nextUser.split(",").length > 1) {   // 多用户任务分配
                        task.setAssignee(null);
                        task.addCandidateUsers(Arrays.asList(nextUser.split(",")));
                    } else {
                        task.setAssignee(nextUser);
                    }
                }

                // 添加候选组用户
                /*candidates.forEach(candidate -> {
                    task.addCandidateUser(candidate.getUserId());
                    log.debug("节点候选组审批人：{}", candidate.getUserId());
                });*/
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        log.info("------------------workflow isFailOnException ----------------------");
        return true;
    }
}
