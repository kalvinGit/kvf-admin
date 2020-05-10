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
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions("workflow:process:designer")
    @GetMapping(value = "editor")
    public ModelAndView editor() {
        return new ModelAndView("workflow/modeler");
    }

    @RequiresPermissions("workflow:process:index")
    @GetMapping(value = "process/index")
    public ModelAndView process() {
        return new ModelAndView("workflow/process");
    }

    @RequiresPermissions("workflow:myprocess:index")
    @GetMapping(value = "myprocess/index")
    public ModelAndView myProcess() {
        return new ModelAndView("workflow/my_process");
    }

    @RequiresPermissions("workflow:mytodo:index")
    @GetMapping(value = "mytodo/index")
    public ModelAndView myTodo() {
        return new ModelAndView("workflow/my_todo");
    }

    @RequiresPermissions("workflow:mydone:index")
    @GetMapping(value = "mydone/index")
    public ModelAndView myDone() {
        return new ModelAndView("workflow/my_done");
    }

    @RequiresPermissions("workflow:myapply:index")
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

    /**
     * 任务表单页面
     * @param taskId 任务ID
     * @return mv
     */
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

    /**
     * 流程表单页面
     * @param processInstanceId 流程实例ID
     * @return mv
     */
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

    /**
     * 保存模型
     * @param modelVO 模型VO实体
     * @return r
     */
    @RequiresPermissions("workflow:process:add")
    @PostMapping(value = "save/model")
    public R saveModel(ModelVO modelVO) {
        final String modelId = workFlowService.create(modelVO);
        return R.ok(modelId);
    }

    /**
     * 流程管理列表
     * @param processQueryVO 查询参数
     * @return r
     */
    @GetMapping(value = "process/list")
    public R processList(ProcessQueryVO processQueryVO) {
        return R.ok(workFlowService.getProcesses(processQueryVO));
    }

    /**
     * 发布/部署流程
     * @param modelId 模型ID
     * @return r
     */
    @RequiresPermissions("workflow:process:push")
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
        return R.ok(taskId);
    }

    /**
     * 当前用户启动流程（一般用于开发者开发测试流程）
     * @param deploymentId 流程发布ID
     */
    @RequiresPermissions("workflow:process:start")
    @GetMapping(value = "process/{deploymentId}/start/test")
    public R startT(@PathVariable String deploymentId) {
        final String taskId = processEngine.start(deploymentId);
        return R.ok(taskId);
    }

    /**
     * 提交任务
     * @param flowVariables 流程表单数据
     * @return r
     */
    @PostMapping(value = "submit/task")
    public R submit(@RequestParam Map<String, Object> flowVariables) {
        processEngine.submitTask(flowVariables);
        return R.ok();
    }

    /**
     * 驳回任务
     * @param taskId 任务ID
     * @return r
     */
    @PostMapping(value = "put/back/task/{taskId}")
    public R putBackTask(@PathVariable String taskId) {
        processEngine.backToPreNode(taskId);
        return R.ok();
    }

    /**
     * 驳回指定环节
     * @param taskId 任务ID
     * @param targetNodeId 目标节点ID
     * @return r
     */
    @PostMapping(value = "put/back2/task/{taskId}/{targetNodeId}")
    public R putBack2Task(@PathVariable String taskId, @PathVariable String targetNodeId) {
        processEngine.back2Node(taskId, targetNodeId);
        return R.ok();
    }

    /**
     * 驳回至首环节
     * @param taskId 任务ID
     * @return r
     */
    @PostMapping(value = "put/back/first/task/{taskId}")
    public R putBackFirstTask(@PathVariable String taskId) {
        processEngine.backToFirstNode(taskId);
        return R.ok();
    }

    /**
     * 删除模型
     * @param modelId 模型ID
     * @return r
     */
    @RequiresPermissions("workflow:process:delete")
    @PostMapping(value = "delete/{modelId}")
    public R delete(@PathVariable String modelId) {
        workFlowService.delete(modelId);
        return R.ok();
    }

    /**
     * 批量删除模型
     * @param modelIds 模型ID集合
     * @return r
     */
    @RequiresPermissions("workflow:process:delete")
    @PostMapping(value = "delete/batch")
    public R deleteBatch(@RequestParam("modelIds") List<String> modelIds) {
        workFlowService.deleteBatch(modelIds);
        return R.ok();
    }

    /**
     * 导出流程设计文件（.json）
     * @param modelIds 模型ID集合
     */
    @RequiresPermissions("workflow:process:export")
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
    @RequiresPermissions("workflow:process:import")
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
    @RequiresPermissions("workflow:process:suspend")
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
    @RequiresPermissions("workflow:process:activate")
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

    /**
     * 我的流程列表
     * @param processQueryVO 查询参数
     * @return r
     */
    @GetMapping(value = "myprocess/list")
    public R myProcessList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return R.ok(workFlowService.getDeployProcesses(processQueryVO));
    }

    /**
     * 我的待办列表
     * @param processQueryVO 查询参数
     * @return r
     */
    @GetMapping(value = "mytodo/list")
    public R myTodoList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return workFlowService.getTodoTasks(processQueryVO).toR();
    }

    /**
     * 我的已办列表
     * @param processQueryVO 查询参数
     * @return r
     */
    @GetMapping(value = "mydone/list")
    public R myDoneList(ProcessQueryVO processQueryVO) {
        processQueryVO.setUsername(ShiroKit.getUser().getUsername());
        return workFlowService.getDoneTasks(processQueryVO).toR();
    }

    /**
     * 我的申请列表
     * @param processQueryVO 查询参数
     * @return r
     */
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

}
