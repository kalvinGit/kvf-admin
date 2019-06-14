package com.kalvin.kvf.gen.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.comm.utils.AuxiliaryKit;
import com.kalvin.kvf.controller.BaseController;
import com.kalvin.kvf.dto.R;
import com.kalvin.kvf.gen.dto.ColumnCommentValueRelationDTO;
import com.kalvin.kvf.gen.dto.ColumnConfigDTO;
import com.kalvin.kvf.gen.dto.ColumnsValueRelationDTO;
import com.kalvin.kvf.gen.dto.TableColumnDTO;
import com.kalvin.kvf.gen.service.ITableService;
import com.kalvin.kvf.gen.utils.VelocityKit;
import com.kalvin.kvf.gen.vo.GenConfigVO;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 代码生成 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-05-10
 */
@RestController
@RequestMapping("gen")
public class GenController extends BaseController {

    @Autowired
    private ITableService tableService;

    @GetMapping(value = "table/index")
    public ModelAndView table() {
        return new ModelAndView("gen/table");
    }

    @GetMapping(value = "setting/{tableName}")
    public ModelAndView setting(@PathVariable String tableName) {
        ModelAndView mv = new ModelAndView("gen/setting");
        List<TableColumnDTO> tableColumnDTOS = tableService.listTableColumn(tableName);
        List<TableColumnDTO> collect = tableColumnDTOS.stream()
                .peek(tc -> {
                    tc.setComment(tc.getColumnComment());
                    tc.setColumnComment(AuxiliaryKit.parseTableColumnCommentName(tc.getColumnComment()));
                    tc.setColumnNameCamelCase(StrUtil.toCamelCase(tc.getColumnName()));
                })
                .collect(Collectors.toList());
        mv.addObject("tableName", tableName);
        mv.addObject("tableColumns", collect);
        return mv;
    }

    @GetMapping(value = "list/tableData")
    public R listTableData(String tableName, int current, int size) {
        return R.ok(tableService.listTablePage(tableName, current, size));
    }

    @PostMapping(value = "code")
    public R genCode(@RequestBody GenConfigVO genConfigVO) {
        LOGGER.info("genConfig={}", genConfigVO);
        String tableType = genConfigVO.getTableType();
        String tplName = tableType.equals("treegrid") ? "treegrid.vm" : "table.vm";
        VelocityContext ctx = new VelocityContext();
        List<ColumnsValueRelationDTO> columnsValueRelationsList = new ArrayList<>();    // 列备注的值对应说明关系列表
        List<ColumnConfigDTO> columns = genConfigVO.getColumns();
        columns.forEach(column -> {
            if (column.isFormat()) {
                List<ColumnCommentValueRelationDTO> columnValueRelations = AuxiliaryKit
                        .parseTableColumnCommentValueRelation(column.get_comment());
                ColumnsValueRelationDTO columnsValueRelations = new ColumnsValueRelationDTO();
                columnsValueRelations.setColumn(column.getName());
                columnsValueRelations.setColumnCommentValueRelations(columnValueRelations);
                columnsValueRelationsList.add(columnsValueRelations);
                /*if (CollectionUtil.isNotEmpty(columnValueRelations)) {

                }*/
            }
        });
        ctx.put("config", genConfigVO);
        ctx.put("columnsValueRelations", columnsValueRelationsList);
        Template t = VelocityKit.getTemplate(tplName);

        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

//        LOGGER.info("html={}", sw.toString());
        return R.ok(sw.toString());
    }

}

