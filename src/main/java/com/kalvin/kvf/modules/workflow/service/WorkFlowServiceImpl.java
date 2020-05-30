package com.kalvin.kvf.modules.workflow.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.modules.workflow.dto.FlowData;
import com.kalvin.kvf.modules.workflow.dto.ProcessQuery;
import com.kalvin.kvf.modules.workflow.entity.Form;
import com.kalvin.kvf.modules.workflow.mapper.ActivityMapper;
import com.kalvin.kvf.modules.workflow.utils.ProcessKit;
import com.kalvin.kvf.modules.workflow.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Kalvin on 2020/5/10.
 */
@Slf4j
@Service
public class WorkFlowServiceImpl implements IWorkFlowService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private FormService formService;

    @Override
    public String create(ModelVO modelVO) {

        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model model = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelVO.getModelName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, modelVO.getDescription());
        model.setMetaInfo(modelObjectNode.toString());
        model.setName(modelVO.getModelName());
        model.setKey(modelVO.getModelKey());

        log.debug("editorNode.toString() = {}", editorNode.toString());

        // 保存模型
        repositoryService.saveModel(model);
        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return model.getId();
    }

    @Override
    public String deploy(String modelId) {
        String modelName = "", processName = "";
        try {
            // 获取模型
            Model modelData = repositoryService.getModel(modelId);
            modelName = modelData.getName();
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            if (bytes == null) {
                throw new KvfException("模型数据为空，请先设计流程并成功保存，再进行发布。");
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            log.debug("modelNode = {}", modelNode.toString());
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (model.getProcesses().size() == 0) {
                throw new KvfException("数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            //发布流程
            String processSourceName = modelName + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelName)
                    .addString(processSourceName, new String(bpmnBytes, StandardCharsets.UTF_8))
                    .deploy();

            // 发布版本+1
            Integer version = modelData.getVersion();
            modelData.setVersion(StrUtil.isBlank(modelData.getDeploymentId()) ? version : version + 1);
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);

            ProcessDefinition processDefinition = ProcessKit.getProcessDefinition(deployment.getId());
            processName = processDefinition.getName();
            if (StrUtil.isBlank(processName)) {
                processName = modelName;
                activityMapper.updateProcessDefinitionName(processName, processDefinition.getId());
                log.debug("流程模型【{}】没有配置流程名称，默认使用流程模型名称作为流程名称", modelName);
            }
            log.debug("流程【{}】成功发布", processName);

            return processDefinition.getId();
        } catch (Exception e) {
            log.error("流程【{}】发布失败：{}", processName, e.getMessage());
            throw new KvfException("流程发布失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<ProcessModelVO> getProcesses(ProcessQueryVO processQueryVO) {
        IPage<ProcessModelVO> page = new Page<>(processQueryVO.getCurrent(), processQueryVO.getSize());
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(processQueryVO.getModelName())) {
            queryWrapper.like("rm.NAME_", processQueryVO.getModelName());
        }
        if (StrUtil.isNotBlank(processQueryVO.getProcessName())) {
            queryWrapper.like("rp.NAME_", processQueryVO.getProcessName());
        }
        queryWrapper.orderBy(true, false, "rm.CREATE_TIME_", "rm.DEPLOYMENT_ID_");
        page = activityMapper.listProcessModels(queryWrapper, page);
        return page;
    }

    @Override
    public void delete(String modelId) {
        if (modelId == null) {
            throw new KvfException("modelId不允许为null");
        }
        ModelQuery modelQuery = repositoryService.createModelQuery().modelId(modelId);
        Model model = modelQuery.singleResult();
        final String deploymentId = model.getDeploymentId();

        // 已发布需要多删除流程定义和流程发布表
        if (StrUtil.isNotBlank(deploymentId)) {
            repositoryService.deleteDeployment(deploymentId);
        }
        // 删除流程模型表
        repositoryService.deleteModel(modelId);
        // todo 删除二进制表数据
    }

    @Transactional
    @Override
    public void deleteBatch(List<String> modelIds) {
        modelIds.forEach(this::delete);
    }

    @Override
    public ProcessQuery<JSONObject> getProcessesJson(List<String> modelIds) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("name", "流程模型JSON");
        jsonObject.put("version", "1.0");
        jsonObject.put("count", modelIds.size());
        JSONArray processes = JSONUtil.createArray();

        modelIds.forEach(modelId -> {
            ModelEntity model = (ModelEntity) repositoryService.getModel(modelId);
            if (model == null || !model.hasEditorSource()) {
                throw new KvfException("导出流程模型数据失败，模型数据不存在");
            }
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
            byte[] modelEditorSourceExt = repositoryService.getModelEditorSourceExtra(modelId);
            JSONObject obj = JSONUtil.createObj();
            obj.put("id", model.getId());
            obj.put("name", model.getName());
            obj.put("version", model.getVersion());
            obj.put("key", model.getKey());
            obj.put("metaInfo", model.getMetaInfo());
            obj.put("editorSourceValueId", model.getEditorSourceValueId());
            obj.put("editorSourceExtValueId", model.getEditorSourceExtraValueId());
            obj.put("editorSource", new String(modelEditorSource, StandardCharsets.UTF_8));
            obj.put("editorSourceExt", new String(modelEditorSourceExt, StandardCharsets.UTF_8));

            processes.add(obj);
        });
        if (modelIds.size() == 1) {
            jsonObject.put("name", ((JSONObject) processes.get(0)).get("name"));
            jsonObject.put("version", ((JSONObject) processes.get(0)).get("version"));
        }
        jsonObject.put("processes", processes);
        log.debug("json={}", jsonObject.toString());

        return new ProcessQuery<>(jsonObject, (long) modelIds.size());
    }

    @Transactional
    @Override
    public void importProcesses(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String json = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
            log.debug("json={}", json);
            JSONObject jsonObject = JSONUtil.parseObj(json);
            JSONArray processes = (JSONArray) jsonObject.get("processes");
            processes.forEach(process -> {
                JSONObject obj = (JSONObject) process;
                String modelId = obj.get("id").toString();
                Model modelEntity = repositoryService.getModel(modelId);
                if (modelEntity == null) {
                    modelEntity = repositoryService.newModel();
                }

                // 新增或更新模型表
                modelEntity.setName(obj.get("name").toString());
                modelEntity.setKey(obj.get("key").toString());
                modelEntity.setMetaInfo(obj.get("metaInfo").toString());
                modelEntity.setVersion((Integer) obj.get("version"));   // todo version 保存无效？
                repositoryService.saveModel(modelEntity);

                // 新增或更新二进制数据
                byte[] modelEditorSources = obj.get("editorSource").toString().getBytes(StandardCharsets.UTF_8);
                byte[] modelEditorSourceExts = obj.get("editorSourceExt").toString().getBytes(StandardCharsets.UTF_8);
                repositoryService.addModelEditorSource(modelEntity.getId(), modelEditorSources);
                repositoryService.addModelEditorSourceExtra(modelEntity.getId(), modelEditorSourceExts);
                log.debug("成功导入或更新流程【{}】", modelEntity.getName());
            });
        } catch (JSONException e) {
            throw new KvfException("流程导入失败，请确保流程json数据格式是否正确");
        }

    }

    @Override
    public IPage<ProcessModelVO> getDeployProcesses(ProcessQueryVO processQueryVO) {
        IPage<ProcessModelVO> page = new Page<>(processQueryVO.getCurrent(), processQueryVO.getSize());
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(processQueryVO.getUsername())) {
            // TODO: 2020/4/25 根据用户获取所属流程
        }
        if (StrUtil.isNotBlank(processQueryVO.getProcessName())) {
            queryWrapper.like("rp.NAME_", processQueryVO.getProcessName());
        }
        queryWrapper.isNotNull("rm.DEPLOYMENT_ID_");
        queryWrapper.orderBy(true, false, "rm.CREATE_TIME_", "rm.KEY_");
        page = activityMapper.listProcessModels(queryWrapper, page);
        return page;
    }

    @Override
    public ProcessQuery<List<MyTodoVO>> getTodoTasks(ProcessQueryVO processQueryVO) {
        TaskQuery taskQuery = taskService.createTaskQuery();

        if (StrUtil.isNotBlank(processQueryVO.getUsername())) {
            taskQuery.taskCandidateOrAssigned(processQueryVO.getUsername());
        }
        if (StrUtil.isNotBlank(processQueryVO.getTaskName())) {
            taskQuery.taskNameLike(ProcessKit.jointLike(processQueryVO.getTaskName()));
        }
        if (StrUtil.isNotBlank(processQueryVO.getProcessName())) {
            taskQuery.processDefinitionNameLike(ProcessKit.jointLike(processQueryVO.getProcessName()));
        }

        taskQuery.orderByTaskCreateTime().desc();

        ProcessQuery<List<Task>> tq = new ProcessQuery<>();
        List<MyTodoVO> myTodoVOS = new ArrayList<>();
        List<Task> tasks = tq.listPage(taskQuery, processQueryVO.getCurrent(), processQueryVO.getSize()).getData();
        tasks.forEach(task -> {
            MyTodoVO myTodoVO = new MyTodoVO();
            BeanUtil.copyProperties(task, myTodoVO);
            HistoricProcessInstance historicProcessInstance = this.getHistoricProcessInstanceByProcessInstanceId(task.getProcessInstanceId());
            myTodoVO.setProcessName(historicProcessInstance.getName());
            myTodoVO.setStartUser(historicProcessInstance.getStartUserId());
            myTodoVOS.add(myTodoVO);
        });

        return new ProcessQuery<>(myTodoVOS, tq.getCount());
    }

    @Override
    public ProcessQuery<List<MyDoneVO>> getDoneTasks(ProcessQueryVO processQueryVO) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();

        if (StrUtil.isNotBlank(processQueryVO.getUsername())) {
            historicTaskInstanceQuery.taskAssignee(processQueryVO.getUsername());
        }
        if (StrUtil.isNotBlank(processQueryVO.getTaskName())) {
            historicTaskInstanceQuery.taskNameLike(ProcessKit.jointLike(processQueryVO.getTaskName()));
        }
        if (StrUtil.isNotBlank(processQueryVO.getProcessName())) {
            historicTaskInstanceQuery.processDefinitionNameLike(ProcessKit.jointLike(processQueryVO.getProcessName()));
        }

        historicTaskInstanceQuery.finished().orderByHistoricTaskInstanceEndTime().desc();

        final ProcessQuery<List<HistoricTaskInstance>> pq = new ProcessQuery<>();
        List<HistoricTaskInstance> historicTaskInstances = pq.listPage(historicTaskInstanceQuery, processQueryVO.getCurrent(), processQueryVO.getSize()).getData();

        List<MyDoneVO> myDoneVOS = new ArrayList<>();
        historicTaskInstances.forEach(historicTaskInstance -> {
            MyDoneVO myDoneVO = new MyDoneVO();
            BeanUtil.copyProperties(historicTaskInstance, myDoneVO);

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();

            myDoneVO.setProcessName(historicProcessInstance.getName());
            myDoneVO.setStartUser(historicProcessInstance.getStartUserId());
            // TODO: 2020/4/25 审批操作
            String deleteReason = historicTaskInstance.getDeleteReason();
            if (deleteReason != null && (deleteReason.contains("驳回") || deleteReason.contains("回退"))) {
                myDoneVO.setApproveAction("驳回");
            } else {
                myDoneVO.setApproveAction("同意");
            }

            if (historicProcessInstance.getEndTime() == null) {
                myDoneVO.setProcessStatus(ProcessKit.FLOW_STATUS_RUNNING);
            } else {
                myDoneVO.setProcessStatus(ProcessKit.FLOW_STATUS_END);
            }
            myDoneVOS.add(myDoneVO);
        });

        return new ProcessQuery<>(myDoneVOS, pq.getCount());
    }

    @Override
    public ProcessQuery<List<MyApplyVO>> getApplyTasks(ProcessQueryVO processQueryVO) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        if (StrUtil.isNotBlank(processQueryVO.getUsername())) {
            historicProcessInstanceQuery.startedBy(processQueryVO.getUsername());
        }
        if (StrUtil.isNotBlank(processQueryVO.getProcessName())) {
            historicProcessInstanceQuery.processDefinitionName(ProcessKit.jointLike(processQueryVO.getProcessName()));
        }

        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc();
        final ProcessQuery<List<HistoricProcessInstance>> pq = new ProcessQuery<>();
        List<HistoricProcessInstance> historicProcessInstances = pq.listPage(historicProcessInstanceQuery, processQueryVO.getCurrent(), processQueryVO.getSize()).getData();

        List<MyApplyVO> myApplyVOS = new ArrayList<>();
        historicProcessInstances.forEach(historicProcessInstance -> {
            MyApplyVO myApplyVO = new MyApplyVO();
            BeanUtil.copyProperties(historicProcessInstance, myApplyVO);

            String processInstanceId = historicProcessInstance.getId();
            StringBuilder currentTaskNames = new StringBuilder();
            StringBuilder currentTaskIds = new StringBuilder();

            final FlowData flowData = ProcessKit.getHisFlowData(processInstanceId);
            myApplyVO.setTheme(flowData.getTheme());

            if (historicProcessInstance.getEndTime() == null) {
                // 未提交、审批中
                myApplyVO.setProcessStatus(ProcessKit.FLOW_STATUS_RUNNING); // 流程状态。未提交、审批中、审批通过、拒绝
                List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .unfinished().list();
                // 未提交
                if (flowData.isFirstNode()) {
                    myApplyVO.setProcessStatus(ProcessKit.FLOW_STATUS_NOT_SUBMIT);
                }
                // 获取当前处理任务节点名称
                list.forEach(taskInstance -> {
                    if (currentTaskIds.length() > 0) {
                        currentTaskIds.append(",");
                        currentTaskNames.append(",");
                    }
                    currentTaskIds.append(taskInstance.getId());
                    currentTaskNames.append(taskInstance.getName());
                });
            } else {
                // 审批通过、拒绝
                myApplyVO.setProcessStatus(ProcessKit.FLOW_STATUS_END); // 流程状态。未提交、审批中、审批通过、拒绝
            }

            myApplyVO.setCurrentTaskNames(currentTaskNames.toString());
            myApplyVO.setCurrentTaskIds(currentTaskIds.toString());
            myApplyVO.setSubmitTime(flowData.getFirstSubmitTime());

            myApplyVOS.add(myApplyVO);
        });

        return new ProcessQuery<>(myApplyVOS, pq.getCount());
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceByProcessInstanceId(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public ProcessQuery<List<CommentVO>> getProcessInstanceComments(String processInstanceId) {
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId, "comment");
        ArrayList<CommentVO> commentVOS = new ArrayList<>();
        for (int i = comments.size() - 1; i >= 0; i--) {
            CommentVO commentVO = new CommentVO();
            Comment comment = comments.get(i);
            BeanUtil.copyProperties(comment, commentVO);
            HistoricTaskInstance historicTaskInstance = ProcessKit.getHistoricTaskInstance(comment.getTaskId());
            commentVO.setUsername(historicTaskInstance.getAssignee());
            commentVO.setTaskName(historicTaskInstance.getName());
            commentVOS.add(commentVO);
        }
        return new ProcessQuery<>(commentVOS);
    }

}
