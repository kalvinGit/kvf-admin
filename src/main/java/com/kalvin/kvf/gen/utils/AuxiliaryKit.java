package com.kalvin.kvf.gen.utils;

import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.gen.dto.ColumnCommentValueRelationDTO;
import com.kalvin.kvf.gen.dto.ColumnConfigDTO;
import com.kalvin.kvf.gen.dto.ColumnsValueRelationDTO;
import com.kalvin.kvf.gen.dto.TableColumnDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 辅助工具
 * @author Kalvin
 */
public class AuxiliaryKit {

    public static String parseTableColumnCommentName(String columnComment) {
        if (StrUtil.isBlank(columnComment)) {
            return "";
        }
        String[] split = columnComment.split("。");
        return split[0];
    }

    /**
     * 解析表列备注的值对应说明关系
     * 注：列注释必须要符合统一规范，如：sex的注释为：性别。0：未知；1：男；2：女
     * 句号前部分为列说明，句号后部分为列其它说明，如果需要声明值对应说明关系，格式为value：说明（如上面例子，注意：分隔符都是中文的）；
     * @param columnComment 列注释
     * @return list
     */
    public static List<ColumnCommentValueRelationDTO> parseTableColumnCommentValueRelation(String columnComment) {
        List<ColumnCommentValueRelationDTO> list = new ArrayList<>();
        if (StrUtil.isBlank(columnComment) || !columnComment.contains("。")) {
            return list;
        }
        String[] split = columnComment.split("。");
        String vr = split[1].trim();
        for (String item : vr.split("；")) {
            if (item.contains("：")) {
//                throw new RuntimeException("格式化列失败：无法解析列注释，注释不符合统一规范：" + vr);
                String[] itemArr = item.split("：");
                list.add(new ColumnCommentValueRelationDTO(itemArr[0], itemArr[1]));
            }
        }
        return list;
    }

    public static List<ColumnsValueRelationDTO> getColumnsValueRelations(List<ColumnConfigDTO> columns) {
        List<ColumnsValueRelationDTO> columnsValueRelationsList = new ArrayList<>();    // 列备注的值对应说明关系列表
        columns.forEach(column -> {
            if (column.isFormat()) {
                List<ColumnCommentValueRelationDTO> columnValueRelations = AuxiliaryKit
                        .parseTableColumnCommentValueRelation(column.get_comment());
                ColumnsValueRelationDTO columnsValueRelations = new ColumnsValueRelationDTO();
                columnsValueRelations.setColumn(column.getName());
                columnsValueRelations.setColumnCommentValueRelations(columnValueRelations);
                columnsValueRelationsList.add(columnsValueRelations);
            }
        });
        return columnsValueRelationsList;
    }

    public static List<TableColumnDTO> handleTableColumns(List<TableColumnDTO> tableColumns) {
        return tableColumns.stream()
                .peek(tc -> {
                    tc.setComment(tc.getColumnComment());
                    tc.setColumnComment(AuxiliaryKit.parseTableColumnCommentName(tc.getColumnComment()));
                    tc.setColumnNameCamelCase(StrUtil.toCamelCase(tc.getColumnName()));
                })
                .collect(Collectors.toList());
    }

    public static List<ColumnConfigDTO> tableColumnsToColumnConfigs(List<TableColumnDTO> tableColumns) {
        List<ColumnConfigDTO> columnConfigs = new ArrayList<>();
        tableColumns.forEach(tc -> {
            ColumnConfigDTO columnConfig = new ColumnConfigDTO();
            columnConfig.setName(tc.getColumnName());
            columnConfig.setComment(tc.getColumnComment());
            columnConfig.set_comment(tc.getComment());
            columnConfig.setSort(false);
            columnConfig.setFormat(tc.getColumnKey().equals("PRI"));
            columnConfigs.add(columnConfig);
        });
        return columnConfigs;
    }

}
