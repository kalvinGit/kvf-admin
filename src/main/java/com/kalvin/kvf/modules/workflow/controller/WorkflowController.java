package com.kalvin.kvf.modules.workflow.controller;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.utils.HttpServletContextKit;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.modules.workflow.dto.ProcessQuery;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;
import com.kalvin.kvf.modules.workflow.service.FormService;
import com.kalvin.kvf.modules.workflow.service.IWorkFlowService;
import com.kalvin.kvf.modules.workflow.service.ProcessFormService;
import com.kalvin.kvf.modules.workflow.service.api.IProcessEngine;
import com.kalvin.kvf.modules.workflow.service.api.IProcessImage;
import com.kalvin.kvf.modules.workflow.utils.ProcessKit;
import com.kalvin.kvf.modules.workflow.vo.CommentVO;
import com.kalvin.kvf.modules.workflow.vo.ModelVO;
import com.kalvin.kvf.modules.workflow.vo.ProcessQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 工作流程控制层
 * Create by Kalvin on 2020/4/20.
 */
@Slf4j
@RestController
@RequestMapping(value = "workflow")
public class WorkflowController {

    @Resource
    private RepositoryService repositoryService;

    @Autowired
    private IProcessEngine processEngine;

    @Resource
    private IProcessImage processImage;

    @Resource
    private FormService formService;

    @Resource
    private ProcessFormService processFormService;

    @Resource
    private IWorkFlowService workFlowService;

    @GetMapping(value = "create/index")
    public ModelAndView create() {
        return new ModelAndView("workflow/model_create");
    }

    @GetMapping(value = "editor")
    public ModelAndView editor() {
        return new ModelAndView("workflow/modeler");
    }

    @GetMapping(value = "process/index")
    public ModelAndView process() {
        return new ModelAndView("workflow/process");
    }

    @GetMapping(value = "formDesigner/index")
    public ModelAndView formDesigner() {
        return new ModelAndView("workflow/form_designer");
    }

    @GetMapping(value = "myprocess/index")
    public ModelAndView myProcess() {
        return new ModelAndView("workflow/my_process");
    }

    @GetMapping(value = "mytodo/index")
    public ModelAndView myTodo() {
        return new ModelAndView("workflow/my_todo");
    }

    @GetMapping(value = "mydone/index")
    public ModelAndView myDone() {
        return new ModelAndView("workflow/my_done");
    }

    @GetMapping(value = "myapply/index")
    public ModelAndView myApply() {
        return new ModelAndView("workflow/my_apply");
    }

    @GetMapping(value = "task/{taskId}/approve/index")
    public ModelAndView approve(@PathVariable String taskId) {
        ModelAndView mv = new ModelAndView("workflow/approve");
        Map<String, Object> currentUserTaskVariables = processEngine.getCurrentUserTaskVariables(taskId);
        if (currentUserTaskVariables != null) {
            mv.addObject("formConfig", ProcessKit.getFormConfig(currentUserTaskVariables));
            mv.addObject("flowData", ProcessKit.getFlowData(currentUserTaskVariables));
        } else {
            // TODO: 跳转其它页面
        }
        return mv;
    }

    @GetMapping(value = "hisComment/index")
    public ModelAndView hisComment(String processInstanceId) {
        ModelAndView mv = new ModelAndView("workflow/his_comment");
        List<CommentVO> commentVOS = workFlowService.getProcessInstanceComments(processInstanceId).getData();
        mv.addObject("comments", commentVOS);
        mv.addObject("processInstanceId", processInstanceId);
        return mv;
    }

    @GetMapping(value = "task/{taskId}/form")
    public ModelAndView taskForm(@PathVariable String taskId) {
        ModelAndView mv = new ModelAndView("workflow/common/base_wfform");
        Map<String, Object> currentUserTaskVariables = processEngine.getCurrentUserTaskVariables(taskId);
        if (currentUserTaskVariables != null) {
            mv.addObject("formConfig", ProcessKit.getFormConfig(currentUserTaskVariables));
            mv.addObject("flowData", ProcessKit.getFlowData(currentUserTaskVariables));
        } else {
            // TODO: 跳转其它页面
        }
        return mv;
    }

    @GetMapping(value = "process/{processInstanceId}/form")
    public ModelAndView processForm(@PathVariable String processInstanceId) {
        ModelAndView mv = new ModelAndView("workflow/common/base_wfform");
        Map<String, Object> hisUserTaskVariables = processEngine.getHisUserTaskVariables(processInstanceId);
        if (hisUserTaskVariables != null) {
            mv.addObject("formConfig", ProcessKit.getFormConfig(hisUserTaskVariables));
            mv.addObject("flowData", ProcessKit.getFlowData(hisUserTaskVariables));
        } else {
            // TODO: 跳转其它页面
        }
        return mv;
    }

    @GetMapping(value = "setting")
    public ModelAndView setting(String modelId) {
        ModelAndView mv = new ModelAndView("workflow/process_setting");
        ProcessForm processForm = processFormService.getByModelId(modelId);
        if (processForm == null) {
            processForm = new ProcessForm();
        }
        mv.addObject("processForm", processForm);
        mv.addObject("forms", formService.list());
        return mv;
    }

    @PostMapping(value = "save/model")
    public R saveModel(ModelVO modelVO) {
        final String modelId = workFlowService.create(modelVO);
        return R.ok(modelId);
    }

    @GetMapping(value = "process/list")
    public R processList(ProcessQueryVO processQueryVO) {
        return R.ok(workFlowService.getProcesses(processQueryVO));
    }

    @PostMapping(value = "deploy/{modelId}")
    public R deploy(@PathVariable String modelId) {
        return R.ok(workFlowService.deploy(modelId));
    }

    /**
     * 当前用户启动流程
     * @param deploymentId 流程发布ID
     */
    @GetMapping(value = "process/{deploymentId}/start")
    public R start(@PathVariable String deploymentId) {
        final String taskId = processEngine.start(deploymentId);
        log.debug("taskId={}", taskId);
//        response.sendRedirect("/workflow/task/{taskId}/form".replace("{taskId}", taskId));
        return R.ok(taskId);
    }

    @PostMapping(value = "submit/task/{processInstanceId}/{taskId}")
    public R submit(@PathVariable String processInstanceId, @PathVariable String taskId) {
//        processEngine.submitTask(variables);
        return R.ok();
    }

    @PostMapping(value = "submit/task")
    public R submit(@RequestParam Map<String, Object> variables) {
        processEngine.submitTask(variables);
        return R.ok();
    }

    @PostMapping(value = "put/back/task/{taskId}")
    public R putBackTask(@PathVariable String taskId) {
        processEngine.backToPreNode(taskId);
        return R.ok();
    }

    @PostMapping(value = "put/back2/task/{taskId}/{targetNodeId}")
    public R putBack2Task(@PathVariable String taskId, @PathVariable String targetNodeId) {
        processEngine.back2Node(taskId, targetNodeId);
        return R.ok();
    }

    @PostMapping(value = "put/back/first/task/{taskId}")
    public R putBackFirstTask(@PathVariable String taskId) {
        processEngine.backToFirstNode(taskId);
        return R.ok();
    }

    @PostMapping(value = "delete/{modelId}")
    public R delete(@PathVariable String modelId) {
        workFlowService.delete(modelId);
        return R.ok();
    }

    @PostMapping(value = "delete/batch")
    public R deleteBatch(@RequestParam("modelIds") List<String> modelIds) {
        workFlowService.deleteBatch(modelIds);
        return R.ok();
    }

    /**
     * 导出流程设计文件（.json）
     * @param modelIds 模型ID集合
     */
    @GetMapping(value = "export/batch")
    public void exportBatch(@RequestParam("modelIds") List<String> modelIds) {
        ProcessQuery<JSONObject> processesJson = workFlowService.getProcessesJson(modelIds);
        JSONObject data = processesJson.getData();
        String name = data.get("name").toString();
        String version = data.get("version").toString();
        HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8") + "_v" + version + ".json");
        } catch (UnsupportedEncodingException e) {
            throw new KvfException(e.getMessage());
        }
        ServletUtil.write(response, data.toString(), "application/json;charset=UTF-8");
    }

    /**
     * 导入流程设计
     * @param file 流程设计文件（.json）
     * @return r
     */
    @PostMapping(value = "import/batch")
    public R importBatch(@RequestParam(value = "file") MultipartFile file) {
        try {
            workFlowService.importProcesses(file.getInputStream());
        } catch (IOException e) {
            return R.fail(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 挂起流程
     * @param deploymentIds 发布ID集合
     * @return r
     */
    @PostMapping(value = "suspend/process")
    public R suspendProcess(@RequestParam("deploymentIds") List<String> deploymentIds) {
        processEngine.suspendProcessDefinitionByIds(deploymentIds);
        return R.ok();
    }

    /**
     * 激活流程
     * @param deploymentIds 发布ID集合
     * @return r
     */
    @PostMapping(value = "activate/process")
    public R activateProcess(@RequestParam("deploymentIds") List<String> deploymentIds) {
        processEngine.activateProcessDefinitionByIds(deploymentIds);
        return R.ok();
    }

    /**
     * 挂起流程实例
     * @param processInstanceId 流程实例ID
     * @return r
     */
    @PostMapping(value = "suspend/processInstance/{processInstanceId}")
    public R suspendProcessInstance(@PathVariable String processInstanceId) {
        processEngine.suspendProcessInstance(processInstanceId);
        return R.ok();
    }

    /**
     * 激活流程实例
     * @param processInstanceId 流程实例ID
     * @return r
     */
    @PostMapping(value = "activate/processInstance/{processInstanceId}")
    public R activateProcessInstance(@PathVariable String processInstanceId) {
        processEngine.activateProcessInstance(processInstanceId);
        return R.ok();
    }

    @GetMapping(value = "form/designer/list")
    public R formDesignerList() {
        return R.ok();
    }

    @GetMapping(value = "myprocess/list")
    public R myProcessList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return R.ok(workFlowService.getDeployProcesses(processQueryVO));
    }

    @GetMapping(value = "mytodo/list")
    public R myTodoList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return workFlowService.getTodoTasks(processQueryVO).toR();
    }

    @GetMapping(value = "mydone/list")
    public R myDoneList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return workFlowService.getDoneTasks(processQueryVO).toR();
    }

    @GetMapping(value = "myapply/list")
    public R myApplyList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return workFlowService.getApplyTasks(processQueryVO).toR();
    }

    @GetMapping(value = "processInstance/{processInstanceId}")
    public R processInstance(@PathVariable String processInstanceId) {
        return R.ok(workFlowService.getHistoricProcessInstanceByProcessInstanceId(processInstanceId));
    }

    /**
     * 流程实例流转意见
     * @param processInstanceId 实例ID
     * @return r
     */
    @GetMapping(value = "processInstance/{processInstanceId}/comments")
    public R processInstanceComments(@PathVariable String processInstanceId) {
        return workFlowService.getProcessInstanceComments(processInstanceId).toR();
    }

    /**
     * 流程流转图，已执行节点和流程线高亮显示
     * @param processInstanceId 流程实例ID
     * @param response res
     */
    @GetMapping(value = "process/{processInstanceId}/flowChart/image.png")
    public void processFlowChartImage(@PathVariable String processInstanceId, HttpServletResponse response) {
        try {
            byte[] bytes = processImage.getFlowImgByProcInstId(processInstanceId);
            response.setContentType("image/png");
            OutputStream os = response.getOutputStream();
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            throw new KvfException("生成流转流程图失败：" + e.getMessage());
        }
    }

    /**
     * 撤回任务
     * @param taskId 已办任务ID
     * @return r
     */
    @PostMapping(value = "withdraw/task/{taskId}")
    public R withdrawTask(@PathVariable String taskId) {
        processEngine.withdrawApproval(taskId);
        return R.ok();
    }

    @GetMapping(value = "test/{pid}")
    public R mytest(@PathVariable String pid) {
//        String startNodeId = ProcessKit.getStartNodeId("95001");
//        log.debug("startNodeId={}", startNodeId);
//        ProcessKit.getHisFlowData("95001");
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pid);
        String sourceSystemId = bpmnModel.getSourceSystemId();
        Map<String, List<ExtensionAttribute>> definitionsAttributes = bpmnModel.getDefinitionsAttributes();
        List<Process> processes = bpmnModel.getProcesses();
        Process process = processes.get(0);
        FlowElement flowElement = process.getFlowElement("sid-01BC7AFA-995A-4928-9B7D-1736E340E0E3");
        if (flowElement instanceof UserTask) {

            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
            // 审批人集合参数
            multiInstanceLoopCharacteristics.setInputDataItem("assigneeList");
            // 迭代集合
//            multiInstanceLoopCharacteristics.setElementVariable("assignee");
            // 完成条件 已完成数等于实例数
            multiInstanceLoopCharacteristics.setCompletionCondition("${nrOfActiveInstances == nrOfInstances}");
            // 并行
            multiInstanceLoopCharacteristics.setSequential(false);

            ((UserTask) flowElement).setLoopCharacteristics(multiInstanceLoopCharacteristics);
            Object behavior = ((UserTask) flowElement).getBehavior();
            MultiInstanceActivityBehavior multiInstanceActivityBehavior = ((UserTaskActivityBehavior) ((UserTask) flowElement).getBehavior()).getMultiInstanceActivityBehavior();
        }
        Map<String, List<ExtensionAttribute>> attributes = process.getAttributes();
        Map<String, List<ExtensionElement>> extensionElements = process.getExtensionElements();
        log.debug("processName={}", process.getName());
        log.debug("sourceSystemId={}", sourceSystemId);
        return R.ok(bpmnModel.toString());
    }
}
