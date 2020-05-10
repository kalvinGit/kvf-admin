package com.kalvin.kvf.modules.workflow.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.workflow.dto.ProcessQuery;
import com.kalvin.kvf.modules.workflow.vo.*;
import org.activiti.engine.history.HistoricProcessInstance;

import java.io.InputStream;
import java.util.List;

/**
 * Create by Kalvin on 2020/5/10.
 */
public interface IWorkFlowService {

    /**
     * 创建流程模型
     * @return 流程模型ID
     */
    String create(ModelVO modelVO);

    /**
     * 发布/部署流程
     * @param modelId 模型ID
     * @return 流程定义ID
     */
    String deploy(String modelId);

    IPage<ProcessModelVO> getProcesses(ProcessQueryVO processQueryVO);

    /**
     * 删除流程。未发布只删除流程模型、已发布需要多删除流程定义和流程发布表
     * @param modelId 模型ID
     */
    void delete(String modelId);

    void deleteBatch(List<String> modelIds);

    ProcessQuery<JSONObject> getProcessesJson(List<String> modelIds);

    void importProcesses(InputStream in);

    IPage<ProcessModelVO> getDeployProcesses(ProcessQueryVO processQueryVO);

    ProcessQuery<List<MyTodoVO>> getTodoTasks(ProcessQueryVO processQueryVO);

    ProcessQuery<List<MyDoneVO>> getDoneTasks(ProcessQueryVO processQueryVO);

    ProcessQuery<List<MyApplyVO>> getApplyTasks(ProcessQueryVO processQueryVO);

    HistoricProcessInstance getHistoricProcessInstanceByProcessInstanceId(String processInstanceId);

    /**
     * 获取流程实例流转意见
     * @param processInstanceId 流程实例ID
     * @return ProcessQuery
     */
    ProcessQuery<List<CommentVO>> getProcessInstanceComments(String processInstanceId);

}
