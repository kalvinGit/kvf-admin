package com.kalvin.kvf.modules.workflow.config;

/**
 * Create by Kalvin on 2020/4/20.
 */
public class ActivityConstant {

    /** activity 相关常量 */
    public final static String FLOW_VARIABLE_KEY = "flowVariable";  // 流程变量key值

    public final static String TASK_DATA_KEY = "taskData"; // 流程任务流转数据key值

    public final static String FORM_DATA_KEY = "formData";    // 存放表单数据的key值

    public final static String NEXT_USER_ID_KEY = "nextUser";   // 下一审批人key值

    public final static String DEFAULT_AGREE_COMMENT = "同意";
    public final static String DEFAULT_DISAGREE_COMMENT = "不同意";

    public final static String FORM_TEMP = "activiti/comm/form_temp"; // 业务表单模板
    public final static String HIS_FORM_TEMP = "activiti/comm/hisTask_temp"; // 历史任务业务表单模板
    public final static String DEFAULT_FORM_NAME = "default";  // activiti默认表单
    public final static String NO_PREMISSIONS_NAME = "activiti/no_premissions";  // activiti无权限页面
    public final static String FORM_PRFIX = "activiti/form/";   // actviti业务表单前缀
    public final static String FLOW_CHART = "activiti/comm/flow_chart";   // 流程图
    public final static String COMMENT_ADVICE = "activiti/comm/comment_advice";   // 查看流转意见


    public final static int NODE_JUMP_TYPE_ROLL = 0;    // 节点跳转类型：回退
    public final static int NODE_JUMP_TYPE_GO = 1;      // 节点跳转类型：前进

}
