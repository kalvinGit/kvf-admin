package com.kalvin.kvf.modules.sys.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.sys.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2019-05-10
 */
public interface LogMapper extends BaseMapper<Log> {

    List<Log> selectLogList(@Param("log") Log log, Page page);

}
