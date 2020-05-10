package com.kalvin.kvf.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kalvin.kvf.modules.workflow.vo.ProcessModelVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 自定义activiti mapper接口
 * Create by Kalvin on 2020/4/22.
 */
public interface ActivityMapper {

    @Update("update act_re_procdef set NAME_=#{name} where ID_=#{processDefinitionId}")
    void updateProcessDefinitionName(@Param("name") String name, @Param("processDefinitionId") String processDefinitionId);

    @Delete("delete act_hi_actinst where TASK_ID_ = #{taskId}")
    void deleteHisActivityInstanceByTaskId(@Param("taskId") String taskId);

    @Delete("delete act_hi_taskinst where ID_ = #{taskId}")
    void deleteHisTaskInstanceByTaskId(@Param("taskId") String taskId);

    @Select("SELECT " +
            "rm.ID_ as id," +
            "rm.NAME_ as name," +
            "rm.KEY_ as `key`," +
            "rm.VERSION_ as version," +
            "rm.DEPLOYMENT_ID_ as deploymentId," +
            "rm.CREATE_TIME_ as createTime," +
            "rm.LAST_UPDATE_TIME_ as lastUpdateTime," +
            "rp.NAME_ as processName," +
            "rp.ID_ as processDefinitionId," +
            "IF(rp.SUSPENSION_STATE_=1, false, IF(rp.SUSPENSION_STATE_ is null, null, true)) as processSuspended " +
            "FROM " +
            "act_re_model rm " +
            "LEFT JOIN act_re_procdef rp ON rm.DEPLOYMENT_ID_ = rp.DEPLOYMENT_ID_ ${ew.customSqlSegment}")
    IPage<ProcessModelVO> listProcessModels(@Param(Constants.WRAPPER) Wrapper wrapper, IPage page);
}
