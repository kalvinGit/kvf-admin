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
public class MyDoneVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;  // 任务ID
    private String name;    // 任务名称
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String businessKey;
    private String owner;
    private String assignee;
    private Date startTime;
    private Date endTime;
    private Long durationInMillis;

    private String processName;
    private String startUser;
    private Integer processStatus; // 流程状态。0:未提交、1:审批中、2:结束（审批通过、拒绝）
    private String approveAction;   // 审批操作。通过、驳回、拒绝
}
