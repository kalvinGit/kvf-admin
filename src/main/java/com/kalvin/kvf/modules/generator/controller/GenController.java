package com.kalvin.kvf.modules.generator.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.common.utils.HttpServletContextKit;
import com.kalvin.kvf.modules.generator.constant.ConfigConstant;
import com.kalvin.kvf.modules.generator.dto.QueryColumnConfigDTO;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;
import com.kalvin.kvf.modules.generator.service.IGenService;
import com.kalvin.kvf.modules.generator.service.ITableService;
import com.kalvin.kvf.modules.generator.utils.AuxiliaryKit;
import com.kalvin.kvf.modules.generator.utils.VelocityKit;
import com.kalvin.kvf.modules.generator.vo.GenConfigVO;
import com.kalvin.kvf.modules.generator.vo.QuicklyGenParamsVO;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 代码生成 前端控制器
 * </p>
 *
 * @author Kalvin
 * @since 2019-05-10
 */
@RestController
@RequestMapping("generator")
public class GenController extends BaseController {

    @Autowired
    private ITableService tableService;

    @Autowired
    private IGenService genService;

    @GetMapping(value = "table/index")
    public ModelAndView table() {
        return new ModelAndView("generator/table");
    }

    @GetMapping(value = "setting/{tableName}")
    public ModelAndView setting(@PathVariable String tableName) {
        ModelAndView mv = new ModelAndView("generator/setting");
        List<TableColumnDTO> tableColumnDTOS = tableService.listTableColumn(tableName);
        mv.addObject("tableName", tableName);
        mv.addObject("tableColumns", AuxiliaryKit.handleTableColumns(tableColumnDTOS));
        return mv;
    }

    @GetMapping(value = "list/tableData")
    public R listTableData(String tableName, int current, int size) {
        return R.ok(tableService.listTablePage(tableName, current, size));
    }

    @PostMapping(value = "custom/generate/code")
    public R customGenerateCode(@RequestBody GenConfigVO genConfigVO) {
        LOGGER.info("genConfig={}", genConfigVO);
        String tableType = genConfigVO.getTableType();
        String tableTplName = tableType.equals("tree_grid") ? "treegrid.vm" : "table.vm";

        // 查询列参数设置坨峰字段
        List<QueryColumnConfigDTO> queryColumns = genConfigVO.getQueryColumns();
        if (CollUtil.isNotEmpty(queryColumns)) {
            queryColumns.forEach(column -> column.setNameCamelCase(StrUtil.toCamelCase(column.getName())));
        }

        // 处理表列值说明关系
        AuxiliaryKit.handleAndSetColumnsValueRelations(genConfigVO.getColumns());

        // 获取数据库表所有列字段数据
        List<TableColumnDTO> tableColumnDTOS = tableService.listTableColumn(genConfigVO.getTableName());

        // 获取并设置实体类所有需要导入的java包集合
        Set<String> sets = AuxiliaryKit.getEntityImportPkgs(tableColumnDTOS);
        genConfigVO.setPkgs(sets);

        // 处理设置所有表列数据
        tableColumnDTOS = AuxiliaryKit.handleTableColumns(tableColumnDTOS);
        AuxiliaryKit.handleAndSetAllColumnsValueRelations(tableColumnDTOS);
        genConfigVO.setAllColumns(tableColumnDTOS);
        genConfigVO.setFirstCapFunName(StrUtil.upperFirst(genConfigVO.getFunName()));
        genConfigVO.setPkCamelCase(StrUtil.toCamelCase(genConfigVO.getPrimaryKey()));
        genConfigVO.setFirstCapPk(StrUtil.upperFirst(genConfigVO.getPkCamelCase()));

        VelocityContext ctx = VelocityKit.getContext();
        ctx.put("config", genConfigVO);
        Template t = VelocityKit.getTemplate(tableTplName);
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

        // 生成所有模板代码
        VelocityKit.allToFile(genConfigVO);
        // 返回预览页面代码
        String tempUrl = genConfigVO.getModuleName() + "/" + genConfigVO.getFunName() + "/list/data";
        return R.ok(sw.toString().replace(tempUrl, "sys/user/list/data"));
    }

    @PostMapping(value = "quickly/generate/code")
    public R quicklyGenerateCode(QuicklyGenParamsVO quicklyGenParamsVO) {
        GenConfigVO config = genService.init(
                quicklyGenParamsVO.getTableName(), quicklyGenParamsVO.getTableType(), quicklyGenParamsVO.getTableComment());

        // 生成所有模板代码
        VelocityKit.allToFile(config);
        return R.ok();
    }

    @PostMapping(value = "quickly/generate/code/batch")
    public R quicklyGenerateCodeBatch(@RequestBody List<QuicklyGenParamsVO> quicklyGenParamsVOS) {
        quicklyGenParamsVOS.forEach(this::quicklyGenerateCode);
        return R.ok();
    }

    @GetMapping(value = "check/codeZip/isExists")
    public R checkCodeZipIsExists() {
        String fileUrl = ConfigConstant.CODE_GEN_PATH + "/" + ConfigConstant.CODE_ZIP_FILENAME;
        File file = new File(fileUrl);
        if (!file.exists()) {
            return R.fail("未找到生成的代码包，请生成代码后再下载。");
        }
        return R.ok();
    }

    @GetMapping(value = "download/codeZip")
    public void downloadCodeZip() {
        String fileUrl = ConfigConstant.CODE_GEN_PATH + "/" + ConfigConstant.CODE_ZIP_FILENAME;
        File file = new File(fileUrl);
        ServletUtil.write(HttpServletContextKit.getHttpServletResponse(), file);
    }

}

