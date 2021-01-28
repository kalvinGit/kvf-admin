package com.kalvin.kvf.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.workflow.entity.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 表单设计表 Mapper 接口
 * </p>
 * @since 2020-05-02 19:36:28
 */
public interface FormMapper extends BaseMapper<Form> {

    /**
     * 查询列表(分页)
     * @param form 查询参数
     * @param page 分页参数
     * @return list
     */
    List<Form> selectFormList(@Param("form") Form form, IPage page);

}
