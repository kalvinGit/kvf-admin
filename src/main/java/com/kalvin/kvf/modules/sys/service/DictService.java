package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.sys.entity.Dict;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 * @since 2019-08-10 15:52:56
 */
public interface DictService extends IService<Dict> {

    /**
     * 获取列表。分页
     * @param dict 查询参数
     * @return page
     */
    Page<Dict> listDictPage(Dict dict);

    Dict getByCode(String code);

    /**
     * 根据字典码获取下面所有的字典项条目
     * @param code 字典码
     * @return
     */
    List<Dict> listAllDictItemByCode(String code);

    List<Dict> listByParentId(Long parentId);

    void deleteWithChildren(Long id);

}
