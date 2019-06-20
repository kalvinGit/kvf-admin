package com.kalvin.kvf.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.kalvin.kvf.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author Kalvin
 * @since 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录名称
     */
    private String username;

    /**
     * 操作功能
     */
    private String operation;

    /**
     * 操作uri
     */
    private String forwardAction;

    /**
     * IP
     */
    private String ip;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 系统
     */
    private String os;

    /**
     * 执行时长(毫秒)
     */
    private Long time;

    /**
     * 创建时间
     */
    private Date createTime;


}
