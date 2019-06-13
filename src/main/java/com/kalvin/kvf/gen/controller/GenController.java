package com.kalvin.kvf.gen.controller;


import com.kalvin.kvf.comm.utils.AuxiliaryKit;
import com.kalvin.kvf.controller.BaseController;
import com.kalvin.kvf.dto.R;
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
                .map(tc -> tc.setColumnComment(AuxiliaryKit.handleTableColumnCommentRemark(tc.getColumnComment())))
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
        VelocityContext ctx = new VelocityContext();
        ctx.put("config", genConfigVO);
        Template t = VelocityKit.getTemplate("table.vm");
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

//        LOGGER.info("html={}", sw.toString());
        return R.ok(sw.toString());
    }

}

