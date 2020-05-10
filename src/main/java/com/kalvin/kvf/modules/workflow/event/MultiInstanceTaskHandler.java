package com.kalvin.kvf.modules.workflow.event;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 多实例事件监听
 * Create by Kalvin on 2020/05/08.
 */
@Slf4j
public class MultiInstanceTaskHandler implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateTask) {
        log.info("任务监听类开始执行");
        FlowElement currentFlowElement = delegateTask.getCurrentFlowElement();
        if(currentFlowElement instanceof UserTask){
            UserTask userTask = (UserTask) currentFlowElement;
            List<String> list = new ArrayList<>();
            list.add("admin");
            list.add("test");
            List<String> candidateUsers = userTask.getCandidateUsers();
            log.debug("candidateUsers={}", candidateUsers.size());
            // 设为本地变量(节点所有)
            delegateTask.setVariable("userList", list);
//            delegateTask.setVariableLocal("userList",candidateUsers);
        }
    }
}
