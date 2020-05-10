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
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;  // comment ID
    private String userId;
    private Date time;
    private String taskId;
    private String processInstanceId;
    private String type;
    private String fullMessage;

    private String taskName;    // 任务名称
    private String username;    // 用户名称

}
