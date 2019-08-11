package com.kalvin.kvf.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.sys.entity.Dict;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 * @since 2019-08-10 14:58:21
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 查询列表(分页)
     * @param dict 查询参数
     * @param page 分页参数
     * @return list
     */
    List<Dict> selectDictList(Dict dict, IPage page);

    /**
     * 根据字典码查询下面所有的字典项条目
     * @param code 字典码
     * @return
     */
    List<Dict> selectAllDictItemByCode(String code);

}
