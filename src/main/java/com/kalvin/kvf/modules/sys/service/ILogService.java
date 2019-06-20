package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.sys.entity.Log;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-05-10
 */
public interface ILogService extends IService<Log> {

    Page<Log> listLogPage(Log log);

}
