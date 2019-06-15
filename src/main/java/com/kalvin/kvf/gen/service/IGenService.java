package com.kalvin.kvf.gen.service;

import com.kalvin.kvf.gen.vo.GenConfigVO;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface IGenService {

    /**
     * 初始化生成配置数据
     * @param tableName 表名
     * @param tableType 表格类型
     * @return genConfig
     */
    GenConfigVO init(String tableName, String tableType);

}
