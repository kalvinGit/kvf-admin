package com.kalvin.kvf.modules.generator.controller;

import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;
import com.kalvin.kvf.modules.generator.service.ITableService;
import com.kalvin.kvf.modules.generator.utils.AuxiliaryKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "table")
public class TableController {

    @Autowired
    private ITableService tableService;

    @GetMapping(value = "/{tableName}/columns")
    public R getColumns(@PathVariable String tableName) {
        List<TableColumnDTO> tableColumnDTOS = this.tableService.listTableColumn(tableName);
        return R.ok(AuxiliaryKit.handleTableColumns(tableColumnDTOS));
    }
}
