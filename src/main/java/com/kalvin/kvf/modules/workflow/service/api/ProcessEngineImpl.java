package com.kalvin.kvf.modules.workflow.service.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.modules.workflow.dto.FlowData;
import com.kalvin.kvf.modules.workflow.dto.NodeAssignee;
import com.kalvin.kvf.modules.workflow.dto.ProcessNode;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;
import com.kalvin.kvf.modules.workflow.service.FormService;
import com.kalvin.kvf.modules.workflow.service.ProcessFormService;
import com.kalvin.kvf.modules.workflow.utils.ProcessKit;
import com.kalvin.kvf.modules.workflow.vo.FormConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Kalvin on 2020/4/20.
 */
@Slf4j
@Service("myProcessEngine")
public class ProcessEngineImpl implements IProcessEngine {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private FormService formService;

    @Resource
    private ProcessFormService processFormService;

    @Override
    public String start(String deploymentId) {
        return this.start(deploymentId, null, null);
    }

    @Override
    public String start(String deploymentId, String businessId) {
        return this.start(deploymentId, businessId, null);
    }

    @Override
    public String start(String deploymentId, String businessId, String startUser) {
        // 流程流转任务变量集合
        final HashMap<String, Object> taskVariables = new HashMap<>();
        final FlowData flowData = new FlowData();

        if (StrUtil.isBlank(deploymentId)) {
            throw new KvfException("流程发布ID不允许为空");
        }
        if (StrUtil.isBlank(startUser)) {
            // 获取当前登录用户
            startUser = ShiroKit.getUser().getUsername();
        }

        ProcessForm processForm = processFormService.getByModelId(ProcessKit.getModel(deploymentId).getId());
        if (processForm == null) {
            throw new KvfException("该流程未关联绑定流程表单，无法使用");
        }

        flowData.setMainFormKey(processForm.getFormCode());
        flowData.setStartUser(startUser);
        flowData.setCurrentUser(startUser);
        flowData.setFirstNode(true);
        flowData.setFirstSubmit(true);
        flowData.setNextUser(startUser);
        taskVariables.put(ProcessKit.FLOW_DATA_KEY, flowData);

        // 设置当前任务的办理人  // TODO: 2020/4/22 不知是否有用？
        Authentication.setAuthenticatedUserId(startUser);

        ProcessDefinition processDefinition = ProcessKit.getProcessDefinition(deploymentId);
        if (processDefinition.isSuspended()) {
            throw new KvfException("该流程已被挂起，无法使用，请激活后使用");
        }
        String processDefinitionId = processDefinition.getId();

        ProcessInstance processInstance;
        if (StrUtil.isBlank(businessId)) {
            processInstance =  runtimeService.startProcessInstanceById(processDefinitionId, taskVariables);
        } else {
            // TODO: 2020/4/22 使用业务表启动流程
            processInstance =  runtimeService.startProcessInstanceById(processDefinitionId, businessId, taskVariables);
        }

        // 设置流程启动后的相关核心变量
        String processName = processDefinition.getName();
        String processInstanceId = processInstance.getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String taskId = task.getId();

        // 设置流程实例名称
        runtimeService.setProcessInstanceName(processInstanceId, processName);

        flowData.setProcessName(processName);
        flowData.setProcessDefinitionId(processDefinitionId);
        flowData.setProcessInstanceId(processInstanceId);
        flowData.setFirstNodeId(task.getTaskDefinitionKey());
        flowData.setCurrentNodeId(task.getTaskDefinitionKey());
        flowData.setCurrentNodeName(task.getName());
        flowData.setTaskId(taskId);
        flowData.setExecutionId(task.getExecutionId());
        // 储存流程核心流转变量
        taskVariables.put(ProcessKit.FLOW_DATA_KEY, flowData);
        // 储存流程表单流转数据
        taskVariables.put(ProcessKit.FLOW_VARIABLES_KEY, new HashMap<>());
        taskService.setVariables(taskId, taskVariables);
        log.debug("用户【{}】启动流程【{}】实例【{}】", startUser, processName, processInstance.getId());
        return taskId;
    }

    @Override
    public void submitTask(Map<String, Object> flowVariables) {
        final Map<String, Object> variables = new HashMap<>();
        final String currentUser = ShiroKit.getUser().getUsername();
        final FlowData flowData = new FlowData();
        BeanUtil.copyProperties(flowVariables, flowData);

        // 下一节点审批人
        final String nextUser = flowData.getNextUser();
        final String comment = StrUtil.isBlank(flowData.getComment()) ? ProcessKit.DEFAULT_AGREE_COMMENT : flowData.getComment();
        final String taskId = flowData.getTaskId();
        final String processInstanceId = flowData.getProcessInstanceId();
        final int nextNodeNum = flowData.getNextNodeNum();
        final Task currentTask = ProcessKit.getCurrentTask(taskId);
        final String currentNodeId = currentTask.getTaskDefinitionKey();

        if (StrUtil.isBlank(processInstanceId)) {
            throw new KvfException("流程实例ID不允许为空");
        }
        if (StrUtil.isBlank(nextUser) && nextNodeNum != 0) {
            throw new KvfException("下一环节审批人不允许为空");
        }

        if (flowData.isFirstSubmit()) {
            flowData.setFirstSubmitTime(new Date());
        }
        flowData.setFirstNode(false);
        flowData.setFirstSubmit(false);

        // 记录每个实例任务节点审批人
        HashMap<String, NodeAssignee> nodeAssignee = flowData.getNodeAssignee();
        if (nodeAssignee == null) {
            nodeAssignee = new HashMap<>();
        }
        nodeAssignee.put(currentNodeId + "_" + taskId, new NodeAssignee(taskId, currentNodeId, currentUser));
        flowData.setNodeAssignee(nodeAssignee);

        variables.put(ProcessKit.FLOW_VARIABLES_KEY, flowVariables);
        variables.put(ProcessKit.FLOW_DATA_KEY, flowData);

        if (nextNodeNum == 0 || nextNodeNum == 1) {
            // 正常提交任务
            // 添加审批意见
            taskService.addComment(taskId, processInstanceId, comment);

            // TODO: owner不为空说明可能存在委托任务
            /*if (StrUtil.isNotBlank(currentTask.getOwner())) {
                DelegationState delegationState = currentTask.getDelegationState();
                // 把被委托人代理处理后的任务交回给委托人
                if (DelegationState.PENDING == delegationState) {
                    taskService.resolveTask(currentTask.getId());
                }
            }*/

             Authentication.setAuthenticatedUserId(nextUser);

            // 先置空，再设置。否则提交完任务后，历史任务表不会保存审批人。可能是activiti6的bug的吧？
            taskService.setAssignee(taskId, "");
            taskService.setAssignee(taskId, currentUser);

            // 正式提交任务
            taskService.complete(taskId, variables);
        } else if (nextNodeNum > 1) {
            ProcessKit.nodeJumpTo(taskId, flowData.getTargetNodeId(), currentUser, variables, comment);
        } else {
            throw new KvfException("提交任务异常");
        }

    }

    @Override
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public void suspendProcessDefinitionByIds(List<String> deploymentIds) {
        deploymentIds.forEach(deploymentId -> {
            ProcessDefinition processDefinition = ProcessKit.getProcessDefinition(deploymentId);
            if (!processDefinition.isSuspended()) {
                repositoryService.suspendProcessDefinitionById(processDefinition.getId());
            }
        });
    }

    @Override
    public void activateProcessDefinitionByIds(List<String> deploymentIds) {
        deploymentIds.forEach(deploymentId -> {
            ProcessDefinition processDefinition = ProcessKit.getProcessDefinition(deploymentId);
            if (processDefinition.isSuspended()) {
                repositoryService.activateProcessDefinitionById(processDefinition.getId());
            }
        });
    }

    @Override
    public void withdrawApproval(String oldTaskId) {
        final String currentUser = ShiroKit.getUser().getUsername();
        HistoricTaskInstance historicTaskInstance = ProcessKit.getHistoricTaskInstance(oldTaskId);
        // 目标跳转节点
        final String targetNodeId = historicTaskInstance.getTaskDefinitionKey();
        final String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
        final String processInstanceId = historicTaskInstance.getProcessInstanceId();

        if (ProcessKit.isFinished(processInstanceId)) {
            throw new KvfException("流程已结束，不能撤回");
        }

        // 判断节点是否已被提交，如已提交则不能撤回
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (tasks.size() == 1) {
            Task currentTask = tasks.get(0);
            String currentTaskId = currentTask.getId();
            String currentNodeId = currentTask.getTaskDefinitionKey();

            final Map<String, Object> variables = taskService.getVariables(currentTaskId);

            // 计算期望节点ID
            List<UserTask> nextNodes = ProcessKit.getNextNode(processDefinitionId, targetNodeId, variables);
            if (nextNodes.size() > 1) {
                throw new KvfException("撤回失败，任务已被提交");
            }
            UserTask userTask = nextNodes.get(0);
            String expectNodeId = userTask.getId();
            if (currentNodeId.equals(expectNodeId)) {
                // 撤回
                ProcessKit.setNextUser(currentUser, variables);
                ProcessKit.nodeJumpTo(currentTaskId, targetNodeId, variables, "撤回");
                // 删除
//                activityMapper.deleteHisTaskInstanceByTaskId(currentTaskId);
//                activityMapper.deleteHisActivityInstanceByTaskId(currentTaskId);
            } else {
                throw new KvfException("撤回失败，任务已被提交");
            }

        } else if (tasks.size() > 1) {
            // TODO: 2020/5/1 多任务节点撤回
            throw new KvfException("目前暂不支持多任务撤回");
        } else {
            throw new KvfException("撤回失败，任务已被提交或不存在");
        }

    }

    @Override
    public Map<String, Object> getCurrentUserTaskVariables(String taskId) {
        final Map<String, Object> hashMap = new HashMap<>();
        final String currentUser = ShiroKit.getUser().getUsername();
        Task currentTask = ProcessKit.getCurrentUserTask(taskId);
        if (currentTask == null) {
            // TODO: 流程完结或当前用户没有权限
            return null;
        }

        // 获取当前节点表单数据
        final Map<String, Object> flowVariables = ProcessKit.getFlowVariables(taskId);
        final FlowData flowData = ProcessKit.getFlowData(taskId);

        /*
         * 当前流程绑定的表单代号。
         * 如果在流转过程中把流程绑定的表单移除掉或者更换了表单，它不会使当前流程实例生效。依旧使用的是启动时绑定的表单
         */
        final String formKey = flowData.getMainFormKey();
        final String processDefinitionId = flowData.getProcessDefinitionId();
        final String currentNodeId = currentTask.getTaskDefinitionKey();
        final String currentExecutionId = currentTask.getExecutionId();
        final String currentNodeName = currentTask.getName();

        // 获取当前流程表单的配置数据
        FormConfigVO formConfig;
        try {
            formConfig = formService.getFormConfig(formKey, flowVariables);
        } catch (Exception e) {
            // TODO: 获取表单出错，表单配置数据被删除等
            return null;
        }

        /*
         * 更新任务流转核心数据变量【FlowData】
         */
        // 如果当前任务不是首节点
        if (!flowData.isFirstNode()) {
            flowData.setFirstNode(false);
        }
        flowData.setCurrentUser(currentUser);
        flowData.setCurrentNodeId(currentNodeId);
        flowData.setCurrentNodeName(currentNodeName);
        flowData.setTaskId(taskId);
        flowData.setExecutionId(currentExecutionId);

        hashMap.put(ProcessKit.FLOW_DATA_KEY, flowData);
        hashMap.put(ProcessKit.FORM_CONFIG_KEY, formConfig);

        // 设置下一步路由出口用户任务节点集
        List<UserTask> nextUserTask = ProcessKit.getNextNode(processDefinitionId, currentNodeId, flowVariables);
        List<ProcessNode> processNodes = ProcessKit.convertTo(nextUserTask);
        flowData.setNextNodes(processNodes);
        flowData.setNextNodeNum(processNodes.size());

        // 设置当前环节可驳回的所有任务节点集
        List<ProcessNode> canBackNodes = ProcessKit.getCanBackNodes(currentNodeId, processDefinitionId);
        flowData.setCanBackNodes(canBackNodes);

        log.debug("hashMap={}", hashMap);
        return hashMap;
    }

    @Override
    public Map<String, Object> getHisUserTaskVariables(String processInstanceId) {
        final Map<String, Object> hashMap = new HashMap<>();

        // 获取当前节点表单数据
        final Map<String, Object> flowVariables = ProcessKit.getHisFlowVariables(processInstanceId);
        final FlowData flowData = ProcessKit.getHisFlowData(processInstanceId);
        // 设置只读
        flowData.setReadOnly(true);

        /*
         * 当前流程绑定的表单代号。
         * 如果在流转过程中把流程绑定的表单移除掉或者更换了表单，它不会使当前流程实例生效。依旧使用的是启动时绑定的表单
         */
        final String formKey = flowData.getMainFormKey();

        // 获取当前流程表单的配置数据
        FormConfigVO formConfig;
        try {
            formConfig = formService.getFormConfig(formKey, flowVariables);
        } catch (Exception e) {
            // TODO: 获取表单出错，表单配置数据被删除等
            return null;
        }

        hashMap.put(ProcessKit.FLOW_DATA_KEY, flowData);
        hashMap.put(ProcessKit.FORM_CONFIG_KEY, formConfig);

        log.debug("his hashMap={}", hashMap);
        return hashMap;
    }

    @Override
    public void backToFirstNode(String taskId) {
        final String currentUser = ShiroKit.getUser().getUsername();
        Map<String, Object> variables = taskService.getVariables(taskId);
        FlowData flowData = ProcessKit.getFlowData(variables);
        flowData.setFirstNode(true);
        flowData.setNextUser(flowData.getStartUser());
        ProcessKit.nodeJumpTo(taskId, flowData.getFirstNodeId(), currentUser, variables, "回退首环节");
    }

    @Override
    public void backToPreNode(String taskId) {
        final String currentUser = ShiroKit.getUser().getUsername();
        Map<String, Object> variables = taskService.getVariables(taskId);
        FlowData flowData = ProcessKit.getFlowData(variables);
        Task currentTask = ProcessKit.getCurrentTask(taskId);
        // 获取上一环节
        ProcessNode preOneIncomeNode = ProcessKit.getPreOneIncomeNode(currentTask.getTaskDefinitionKey(), flowData.getProcessDefinitionId());
        if (preOneIncomeNode == null) {
            throw new KvfException("驳回失败，preOneIncomeNode空指针异常");
        }
        String preNodeId = preOneIncomeNode.getNodeId();
        // 如果是首环节
        if (preNodeId.equals(flowData.getFirstNodeId())) {
            flowData.setFirstNode(true);
            flowData.setNextUser(flowData.getStartUser());
        } else {
            // 获取目标节点审批人
            int taskType = ProcessKit.getTaskType(preNodeId, flowData.getProcessDefinitionId());
            if (taskType == ProcessKit.USER_TASK_TYPE_NORMAL) {
                HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(flowData.getProcessInstanceId()).activityId(preNodeId).finished().singleResult();
                String assignee = historicActivityInstance.getAssignee();
                flowData.setNextUser(assignee);
            } else {
                throw new KvfException("目前不支持多任务实例驳回");
            }
        }
        ProcessKit.nodeJumpTo(taskId, preNodeId, currentUser, variables, "驳回上环节");
    }

    @Override
    public void back2Node(String taskId, String targetNodeId) {
        final String currentUser = ShiroKit.getUser().getUsername();
        Map<String, Object> variables = taskService.getVariables(taskId);
        FlowData flowData = ProcessKit.getFlowData(variables);
        // 如果是首环节
        if (targetNodeId.equals(flowData.getFirstNodeId())) {
            flowData.setFirstNode(true);
            flowData.setNextUser(flowData.getStartUser());
        } else {
            // 获取目标节点审批人
            int taskType = ProcessKit.getTaskType(targetNodeId, flowData.getProcessDefinitionId());
            if (taskType == ProcessKit.USER_TASK_TYPE_NORMAL) {
                HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(flowData.getProcessInstanceId()).activityId(targetNodeId).finished().singleResult();
                String assignee = historicActivityInstance.getAssignee();
                flowData.setNextUser(assignee);
            } else {
                throw new KvfException("目前不支持多任务实例驳回");
            }
        }
        FlowElement flowElement = ProcessKit.getFlowElement(targetNodeId, flowData.getProcessDefinitionId());
        ProcessKit.nodeJumpTo(taskId, targetNodeId, currentUser, variables, "驳回【" + flowElement.getName() + "】环节");
    }
}
