package com.kalvin.kvf.modules.generator.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.kvf.common.utils.FileKit;
import com.kalvin.kvf.modules.generator.constant.ConfigConstant;
import com.kalvin.kvf.modules.generator.dto.ButtonConfigDTO;
import com.kalvin.kvf.modules.generator.dto.ColumnConfigDTO;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;
import com.kalvin.kvf.modules.generator.utils.AuxiliaryKit;
import com.kalvin.kvf.modules.generator.vo.GenConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by Kalvin on 2019/6/15
 */
@Service
public class GenServiceImpl implements IGenService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GenServiceImpl.class);

    @Autowired
    private ITableService tableService;

    @Override
    public GenConfigVO init(String tableName, String tableType, String tableComment) {
        GenConfigVO genConfig = new GenConfigVO(tableName, tableType, tableComment);
        String moduleName = tableName.substring(0, tableName.indexOf("_"));
        String funName = StrUtil.toCamelCase(tableName.substring(tableName.indexOf("_") + 1));
        genConfig.setModuleName(moduleName).setFunName(funName).setFirstCapFunName(StrUtil.upperFirst(funName));

        // 获取数据库表所有列字段数据
        List<TableColumnDTO> tableColumnDTOS = tableService.listTableColumn(tableName);

        // 获取并设置实体类所有需要导入的java包集合
        Set<String> sets = AuxiliaryKit.getEntityImportPkgs(tableColumnDTOS);
        genConfig.setPkgs(sets);

        // 处理表列数据
        tableColumnDTOS = AuxiliaryKit.handleTableColumns(tableColumnDTOS);
        AuxiliaryKit.handleAndSetAllColumnsValueRelations(tableColumnDTOS);
        genConfig.setAllColumns(tableColumnDTOS);

        // 设置主键
        Optional<TableColumnDTO> tOptional = tableColumnDTOS
                .stream().filter(tc -> tc.getColumnKey().equals("PRI")).findFirst();
        TableColumnDTO tableColumn = tOptional.get();
        genConfig.setPrimaryKey(tableColumn.getColumnName());
        genConfig.setPkCamelCase(StrUtil.toCamelCase(tableColumn.getColumnName()));
        genConfig.setFirstCapPk(StrUtil.upperFirst(genConfig.getPkCamelCase()));

        // 处理表列值说明关系
        List<ColumnConfigDTO> columnConfigDTOS = AuxiliaryKit.tableColumnsToColumnConfigs(tableColumnDTOS);
        AuxiliaryKit.handleAndSetColumnsValueRelations(columnConfigDTOS);
        genConfig.setColumns(columnConfigDTOS);

        // 读取半设置按钮配置信息并处理
//        String buttonInfo = FileUtil.readString(
//                new File(ClassUtil.getClassPath() + ConfigConstant.BUTTON_JSON_REL_PATH), "UTF-8");
        String buttonInfo = FileKit.readString(ConfigConstant.BUTTON_JSON_REL_PATH);
        JSONObject jsonObject = JSONUtil.parseObj(buttonInfo);
        JSONArray headButtons = JSONUtil.parseArray(jsonObject.get(ConfigConstant.HEAD_BUTTON_KEY));
        JSONArray rowButtons = JSONUtil.parseArray(jsonObject.get(ConfigConstant.ROW_BUTTON_KEY));
        List<ButtonConfigDTO> headBtnConfigs = JSONUtil.toList(headButtons, ButtonConfigDTO.class);
        List<ButtonConfigDTO> rowBtnConfigs = JSONUtil.toList(rowButtons, ButtonConfigDTO.class);
        List<ButtonConfigDTO> headCollect = headBtnConfigs.stream()
                .peek(hc -> hc.setPerId(hc.getPerId().replace("{m}", moduleName).replace("{f}", funName)))
                .collect(Collectors.toList());
        List<ButtonConfigDTO> rowCollect = rowBtnConfigs.stream()
                .peek(hc -> hc.setPerId(hc.getPerId().replace("{m}", moduleName).replace("{f}", funName)))
                .collect(Collectors.toList());
        genConfig.setHeadButtons(headCollect);
        genConfig.setRowButtons(rowCollect);
        genConfig.setQueryColumns(new ArrayList<>());

        return genConfig;
    }
}
