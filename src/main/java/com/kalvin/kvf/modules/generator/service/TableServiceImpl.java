package com.kalvin.kvf.modules.generator.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;
import com.kalvin.kvf.modules.generator.dto.TableDTO;
import com.kalvin.kvf.modules.generator.mapper.TableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements ITableService {

    @Autowired
    private TableMapper tableMapper;

    @Override
    public IPage<TableDTO> listTablePage(String tableName, int current, int size) {
        Page<TableDTO> page = new Page<>(current, size);
        List<TableDTO> tableDTOS;
        if (StrUtil.isBlank(tableName)) {
            tableDTOS = tableMapper.listTable(page);
        } else {
            tableDTOS = tableMapper.listTableByName(tableName, page);
        }
        return page.setRecords(tableDTOS);
    }

    @Override
    public List<TableColumnDTO> listTableColumn(String tableName) {
        return tableMapper.listTableColumn(tableName);
    }
}
