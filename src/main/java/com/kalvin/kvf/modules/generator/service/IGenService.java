package com.kalvin.kvf.modules.generator.service;

import com.kalvin.kvf.modules.generator.vo.GenConfigVO;

public interface IGenService {

    /**
     * 初始化生成配置数据
     * @param tableName 表名
     * @param tableType 表格类型
     * @param tableComment 表格备注
     * @return genConfig
     */
    GenConfigVO init(String tableName, String tableType, String tableComment);

}
