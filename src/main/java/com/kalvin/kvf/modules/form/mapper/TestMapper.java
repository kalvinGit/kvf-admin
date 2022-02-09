package com.kalvin.kvf.modules.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.kalvin.kvf.modules.form.entity.Test;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * @since 2022-02-08 15:27:25
 */
public interface TestMapper extends BaseMapper<Test> {

    /**
     * 查询列表(分页)
     * @param test 查询参数
     * @param page 分页参数
     * @return list
     */
    List<Test> selectTestList(@Param("test") Test test, IPage page);

}
