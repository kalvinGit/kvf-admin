package com.kalvin.kvf.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.workflow.entity.ProcessForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程表单关联表 Mapper 接口
 * </p>
 * @since 2020-05-05 12:14:20
 */
public interface ProcessFormMapper extends BaseMapper<ProcessForm> {

    /**
     * 查询列表(分页)
     * @param processForm 查询参数
     * @param page 分页参数
     * @return list
     */
    List<ProcessForm> selectProcessFormList(@Param("processForm") ProcessForm processForm, IPage page);

}
