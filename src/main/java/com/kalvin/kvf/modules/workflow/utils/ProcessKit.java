package com.kalvin.kvf.modules.workflow.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.common.utils.SpringContextKit;
import com.kalvin.kvf.modules.workflow.command.JumpAnyWhereCmd;
import com.kalvin.kvf.modules.workflow.dto.FlowData;
import com.kalvin.kvf.modules.workflow.dto.ProcessNode;
import com.kalvin.kvf.modules.workflow.ext.FelSupport;
import com.kalvin.kvf.modules.workflow.vo.FormConfigVO;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 工作流工具类
 * Create by Kalvin on 2020/4/21.
 */
public class ProcessKit {

    private final static Logger log = LoggerFactory.getLogger(ProcessKit.class);

    private final static RepositoryService repositoryService = SpringContextKit.getBean(RepositoryService.class);
    private final static HistoryService historyService = SpringContextKit.getBean(HistoryService.class);
    private final static TaskService taskService = SpringContextKit.getBean(TaskService.class);

    public final static String FLOW_DATA_KEY = "FLOW_DATA_KEY";
    public final static String FORM_CONFIG_KEY = "FORM_CONFIG_KEY";
    public final static String FLOW_VARIABLES_KEY = "FLOW_VARIABLES_KEY";
    public final static Integer FLOW_STATUS_NOT_SUBMIT = 0;
    public final static Integer FLOW_STATUS_RUNNING = 1;
    public final static Integer FLOW_STATUS_END = 2;
    public final static String DEFAULT_AGREE_COMMENT = "同意";
    public final static Integer USER_TASK_TYPE_NORMAL = 0;
    public final static Integer USER_TASK_TYPE_SEQUENTIAL = 1;
    public final static Integer USER_TASK_TYPE_PARALLEL = 2;


    public static String jointLikeLeft(String value) {
        return "%" + value;
    }

    public static String jointLikeRight(String value) {
        return value + "%";
    }

    public static String jointLike(String value) {
        return "%" + value + "%";
    }

    public static ProcessDefinition getProcessDefinition(String deploymentId) {
        return repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    }

    /**
     * 获取流程启动者
     * @param processInstanceId 流程实例ID
     * @return str
     */
    public static String getStartUser(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = ProcessKit.getHistoricProcessInstance(processInstanceId);
        return historicProcessInstance.getStartUserId();
    }

    public static String getStartNodeId(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = ProcessKit.getHistoricProcessInstance(processInstanceId);
        return historicProcessInstance.getStartActivityId();
    }

    /**
     * 获取历史流程实例
     * @param processInstanceId 流程实例ID
     * @return HistoricProcessInstance
     */
    public static HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    /**
     * 获取流程核心流转数据
     * @param taskId 当前任务ID
     * @return flowData
     */
    public static FlowData getFlowData(String taskId) {
        Map<String, Object> variables = taskService.getVariables(taskId);
        return (FlowData) variables.get(ProcessKit.FLOW_DATA_KEY);
    }

    /**
     * 获取流程核心流转数据
     * @param variables 当前任务流转数据，即taskService.getVariables(taskId)
     * @return flowData
     */
    public static FlowData getFlowData(Map<String, Object> variables) {
        return (FlowData) variables.get(ProcessKit.FLOW_DATA_KEY);
    }

    /**
     * 获取流程表单流转数据
     * @param taskId 当前任务ID
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getFlowVariables(String taskId) {
        Map<String, Object> variables = taskService.getVariables(taskId);
        return (Map<String, Object>) variables.get(ProcessKit.FLOW_VARIABLES_KEY);
    }

    /**
     * 获取流程历史核心流转数据
     * @param processInstanceId 流程实例ID
     * @return flowData
     */
    public static FlowData getHisFlowData(String processInstanceId) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).variableName(ProcessKit.FLOW_DATA_KEY).singleResult();
        return (FlowData) historicVariableInstance.getValue();
    }

    /**
     * 获取流程历史表单流转数据
     * @param processInstanceId 流程实例ID
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getHisFlowVariables(String processInstanceId) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).variableName(ProcessKit.FLOW_VARIABLES_KEY).singleResult();
        return (Map<String, Object>) historicVariableInstance.getValue();
    }

    public static String getNextUser(Map<String, Object> variables) {
        return ProcessKit.getFlowData(variables).getNextUser();
    }

    public static void setNextUser(String nextUser, Map<String, Object> variables) {
        FlowData flowData = ProcessKit.getFlowData(variables);
        flowData.setNextUser(nextUser);
        variables.put(ProcessKit.FLOW_DATA_KEY, flowData);
    }

    public static FormConfigVO getFormConfig(Map<String, Object> variables) {
        return (FormConfigVO) variables.get(ProcessKit.FORM_CONFIG_KEY);
    }

    public static Model getModel(String deploymentId) {
        return repositoryService.createModelQuery().deploymentId(deploymentId).singleResult();
    }

    /**
     * 获取当前用户任务
     * @param taskId 当前任务ID
     * @return task
     */
    public static Task getCurrentUserTask(String taskId) {
        final String username = ShiroKit.getUser().getUsername();
        return taskService.createTaskQuery().taskId(taskId).taskCandidateOrAssigned(username).singleResult();
    }

    public static Task getCurrentTask(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    public static Process getProcess(String processDefId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        return bpmnModel.getProcesses().get(0);
    }


    public static List<ProcessNode> convertTo(List<UserTask> userTasks) {
        final List<ProcessNode> processNodes = new ArrayList<>();
        userTasks.forEach(userTask -> {
            ProcessNode processNode = new ProcessNode(userTask.getId(), userTask.getName());
            processNode.setAssignee(userTask.getAssignee());
            processNode.setCandidateUserIds(String.join(",", userTask.getCandidateUsers()));
            processNode.setCandidateGroupIds(String.join(",", userTask.getCandidateGroups()));
            processNodes.add(processNode);
        });
        return processNodes;
    }

    /**
     * 获取任务类型
     * @param taskNodeId 任务节点ID
     * @param processDefId 流程定义ID
     * @return int
     */
    public static int getTaskType(String taskNodeId, String processDefId) {
        Process process = ProcessKit.getProcess(processDefId);
        FlowElement flowElement = process.getFlowElement(taskNodeId);
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            if (userTask.getBehavior() instanceof SequentialMultiInstanceBehavior) {
                // 串行任务
                return ProcessKit.USER_TASK_TYPE_SEQUENTIAL;
            } else if (userTask.getBehavior() instanceof ParallelMultiInstanceBehavior) {
                // 并行任务（多实例）
                return ProcessKit.USER_TASK_TYPE_PARALLEL;

            }
        }
        return ProcessKit.USER_TASK_TYPE_NORMAL;
    }

    /**
     * 获取所有可驳回的节点列表
     * @param currentNodeId 当前节点ID
     * @param processDefId 流程定义ID
     * @return List<ProcessNode>
     */
    public static List<ProcessNode> getCanBackNodes(String currentNodeId, String processDefId) {
        final List<ProcessNode> canBackNodes = new ArrayList<>();
        ProcessKit.getIncomeNodesRecur(currentNodeId, processDefId, canBackNodes, true, currentNodeId);
        for (int i = 0; i < canBackNodes.size(); i++) {
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                    .processDefinitionId(processDefId).activityId(canBackNodes.get(i).getNodeId()).finished().list();
            if (CollectionUtil.isEmpty(historicActivityInstances)) {
                canBackNodes.remove(i);
                i--;
            }
        }
        return canBackNodes;
    }

    /**
     * 获取上一环节节点信息
     * @param currentNodeId 当前节点ID
     * @param processDefId 流程定义ID
     * @return ProcessNode
     */
    public static ProcessNode getPreOneIncomeNode(String currentNodeId, String processDefId) {
        final List<ProcessNode> preNodes = new ArrayList<>();
        ProcessKit.getIncomeNodesRecur(currentNodeId, processDefId, preNodes, false);
        for (int i = 0; i < preNodes.size(); i++) {
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                    .processDefinitionId(processDefId).activityId(preNodes.get(i).getNodeId()).finished().list();
            if (CollectionUtil.isEmpty(historicActivityInstances)) {
                preNodes.remove(i);
                i--;
            }
        }
        if (CollectionUtil.isEmpty(preNodes)) {
            return null;
        }
        return preNodes.get(0);
    }

    public static void getIncomeNodesRecur(String currentNodeId, String processDefId, List<ProcessNode> incomeNodes, boolean isAll) {
        ProcessKit.getIncomeNodesRecur(currentNodeId, processDefId, incomeNodes, isAll, true, null);
    }

    public static void getIncomeNodesRecur(String currentNodeId, String processDefId, List<ProcessNode> incomeNodes, boolean isAll, String originalCurrentNodeId) {
        ProcessKit.getIncomeNodesRecur(currentNodeId, processDefId, incomeNodes, isAll, true, originalCurrentNodeId);
    }

    public static void getIncomeNodesRecur(String currentNodeId, String processDefId, List<ProcessNode> incomeNodes, boolean isAll, boolean isFirstExec, String originalCurrentNodeId) {
        Process process = ProcessKit.getProcess(processDefId);
        if (!isFirstExec) {
            if (currentNodeId.equals(originalCurrentNodeId)) {
                return;
            }
        }
        FlowElement currentFlowElement = process.getFlowElement(currentNodeId);
        List<SequenceFlow> incomingFlows = null;
        if (currentFlowElement instanceof UserTask) {
            incomingFlows = ((UserTask) currentFlowElement).getIncomingFlows();
        } else if (currentFlowElement instanceof Gateway) {
            incomingFlows = ((Gateway) currentFlowElement).getIncomingFlows();
        } else if (currentFlowElement instanceof StartEvent) {
            incomingFlows = ((StartEvent) currentFlowElement).getIncomingFlows();
        }
        if (incomingFlows != null && incomingFlows.size() > 0) {
            incomingFlows.forEach(incomingFlow -> {
                String expression = incomingFlow.getConditionExpression();
                // 出线的上一节点
                String sourceFlowElementID = incomingFlow.getSourceRef();
                // 查询上一节点的信息
                FlowElement preFlowElement = process.getFlowElement(sourceFlowElementID);

                //用户任务
                if (preFlowElement instanceof UserTask) {
                    incomeNodes.add(new ProcessNode(preFlowElement.getId(), preFlowElement.getName()));
                    if (isAll) {
                        getIncomeNodesRecur(preFlowElement.getId(), processDefId, incomeNodes, true, false, originalCurrentNodeId);
                    }
                }
                //排他网关
                else if (preFlowElement instanceof ExclusiveGateway) {
                    getIncomeNodesRecur(preFlowElement.getId(), processDefId, incomeNodes, isAll, false, originalCurrentNodeId);
                }
                //并行网关
                else if (preFlowElement instanceof ParallelGateway) {
                    getIncomeNodesRecur(preFlowElement.getId(), processDefId, incomeNodes, isAll, false, originalCurrentNodeId);
                }
            });
        }
    }

    /**
     * 节点跳转
     * @param taskId 当前任务ID
     * @param targetNodeId 目标节点定义ID
     * @param variables 流转数据
     * @param comment 备注/意见
     */
    public static void nodeJumpTo(String taskId, String targetNodeId, Map<String, Object> variables, String comment) {
        CommandExecutor commandExecutor = ((TaskServiceImpl) taskService).getCommandExecutor();
        commandExecutor.execute(new JumpAnyWhereCmd(taskId, targetNodeId, variables, comment));
    }

    /**
     * 节点跳转
     * @param taskId 当前任务ID
     * @param targetNodeId 目标节点定义ID
     * @param assignee 审批用户
     * @param variables 流转数据
     * @param comment 备注/意见
     */
    public static void nodeJumpTo(String taskId, String targetNodeId, String assignee, Map<String, Object> variables, String comment) {
        CommandExecutor commandExecutor = ((TaskServiceImpl) taskService).getCommandExecutor();
        commandExecutor.execute(new JumpAnyWhereCmd(taskId, targetNodeId, assignee, variables, comment));
    }

    public static FlowElement getFlowElement(String nodeId, String processDefId) {
        return ProcessKit.getProcess(processDefId).getFlowElement(nodeId);
    }

    /**
     * 获取下一环节用户任务列表
     * @param procDefId 流程定义ID
     * @param targetNodeId 目标节点ID
     * @param flowVariables 流程表单流转数据
     * @return List<UserTask>
     */
    public static List<UserTask> getNextNode(String procDefId, String targetNodeId, Map<String, Object> flowVariables) {
        List<UserTask> userTasks = new ArrayList<>();
        // 获取Process对象
        Process process = ProcessKit.getProcess(procDefId);
        // 获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前节点信息
        FlowElement flowElement = ProcessKit.getFlowElementById(targetNodeId, flowElements);
        ProcessKit.getNextNodeRecur(flowElements, flowElement, flowVariables, userTasks);
        return userTasks;
    }

    public static boolean isFinished(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = ProcessKit.getHistoricProcessInstance(processInstanceId);
        return historicProcessInstance.getEndTime() != null;
    }

    public static HistoricTaskInstance getHistoricTaskInstance(String taskId) {
        return historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
    }

    /**
     * 查询一个节点的是否子任务中的节点，如果是，返回子任务
     *
     * @param flowElements 全流程的节点集合
     * @param flowElement  当前节点
     */
    private static FlowElement getSubProcess(Collection<FlowElement> flowElements, FlowElement flowElement) {
        for (FlowElement fel : flowElements) {
            if (fel instanceof SubProcess) {
                for (FlowElement flowElement2 : ((SubProcess) fel).getFlowElements()) {
                    if (flowElement.equals(flowElement2)) {
                        return fel;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 返回流程的开始节点
     *
     * @param flowElements 流程节点集合
     */
    private static FlowElement getStartFlowElement(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof StartEvent) {
                return flowElement;
            }
        }
        return null;
    }

    /**
     * 根据ID查询流程节点对象, 如果是子任务，则返回子任务的开始节点
     *
     * @param Id           节点ID
     * @param flowElements 流程节点集合
     */
    private static FlowElement getFlowElementById(String Id, Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement.getId().equals(Id)) {
                //如果是子任务，则查询出子任务的开始节点
                if (flowElement instanceof SubProcess) {
                    return getStartFlowElement(((SubProcess) flowElement).getFlowElements());
                }
                return flowElement;
            }
            if (flowElement instanceof SubProcess) {
                FlowElement flowElement1 = getFlowElementById(Id, ((SubProcess) flowElement).getFlowElements());
                if (flowElement1 != null) {
                    return flowElement1;
                }
            }
        }
        return null;
    }

    /**
     * 查询下一步节点 递归
     *
     * @param flowElements 全流程节点集合
     * @param flowElement  当前节点
     * @param map          业务数据
     * @param nextUser     下一步用户节点
     */
    private static void getNextNodeRecur(Collection<FlowElement> flowElements, FlowElement flowElement, Map<String, Object> map, List<UserTask> nextUser) {

        //如果是结束节点
        if (flowElement instanceof EndEvent) {
            //如果是子任务的结束节点
            if (getSubProcess(flowElements, flowElement) != null) {
                flowElement = getSubProcess(flowElements, flowElement);
            }
        }

        //获取Task的出线信息--可以拥有多个
        List<SequenceFlow> outGoingFlows = null;
        if (flowElement instanceof Task) {
            outGoingFlows = ((org.activiti.bpmn.model.Task) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof Gateway) {
            outGoingFlows = ((Gateway) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof StartEvent) {
            outGoingFlows = ((StartEvent) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof SubProcess) {
            outGoingFlows = ((SubProcess) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof UserTask) {
            outGoingFlows = ((UserTask) flowElement).getOutgoingFlows();
        }

        if (outGoingFlows != null && outGoingFlows.size() > 0) {
            //遍历所有的出线--找到可以正确执行的那一条
            for (SequenceFlow sequenceFlow : outGoingFlows) {

                //1.有表达式，且为true
                //2.无表达式
                String expression = sequenceFlow.getConditionExpression();
                if (StrUtil.isBlank(expression) ||
                        Boolean.parseBoolean(
                                String.valueOf(
                                        FelSupport.result(map, expression.substring(expression.lastIndexOf("{") + 1, expression.lastIndexOf("}")))))) {
                    //出线的下一节点
                    String nextFlowElementID = sequenceFlow.getTargetRef();
                    //查询下一节点的信息
                    FlowElement nextFlowElement = getFlowElementById(nextFlowElementID, flowElements);

                    //用户任务
                    if (nextFlowElement instanceof UserTask) {
                        nextUser.add((UserTask) nextFlowElement);
                    }
                    //排他网关
                    else if (nextFlowElement instanceof ExclusiveGateway) {
                        getNextNodeRecur(flowElements, nextFlowElement, map, nextUser);
                    }
                    //并行网关
                    else if (nextFlowElement instanceof ParallelGateway) {
                        getNextNodeRecur(flowElements, nextFlowElement, map, nextUser);
                    }
                    //接收任务
                    else if (nextFlowElement instanceof ReceiveTask) {
                        getNextNodeRecur(flowElements, nextFlowElement, map, nextUser);
                    }
                    //子任务的起点
                    else if (nextFlowElement instanceof StartEvent) {
                        getNextNodeRecur(flowElements, nextFlowElement, map, nextUser);
                    }
                    //结束节点
                    else if (nextFlowElement instanceof EndEvent) {
                        getNextNodeRecur(flowElements, nextFlowElement, map, nextUser);
                    }
                }
            }
        }
    }
}
