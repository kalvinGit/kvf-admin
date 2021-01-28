package com.kalvin.kvf.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.schedule.entity.Job;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 定时任务表 Mapper 接口
 * </p>
 * @since 2019-08-17 17:06:12
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 查询列表(分页)
     * @param job 查询参数
     * @param page 分页参数
     * @return list
     */
    List<Job> selectJobList(@Param("job") Job job, IPage page);

}
