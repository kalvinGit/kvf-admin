package com.kalvin.kvf.modules.workflow.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by Kalvin on 2020/4/22.
 */
@Data
@ToString
@Accessors(chain = true)
public class MyApplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;  // 流程实例ID
    private String name;    // 流程名称
    private String processInstanceId;
    private String processDefinitionId;
    private String businessKey;
    private String startUserId;
    private Date startTime;
    private Date endTime;

    private String theme;   // 表单主题
    private String currentTaskIds;
    private Date submitTime;    // 申请提交时间
    private String currentTaskNames;
    private Integer processStatus; // 流程状态。0:未提交、1:审批中、2:结束（审批通过、拒绝）
}
