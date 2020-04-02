package com.kalvin.kvf.modules.generator.utils;

import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.modules.generator.constant.ConfigConstant;
import com.kalvin.kvf.modules.generator.constant.DbColumnTypeEnum;
import com.kalvin.kvf.modules.generator.constant.JavaTypeEnum;
import com.kalvin.kvf.modules.generator.constant.TemplateTypeEnum;
import com.kalvin.kvf.modules.generator.dto.ColumnCommentValueRelationDTO;
import com.kalvin.kvf.modules.generator.dto.ColumnConfigDTO;
import com.kalvin.kvf.modules.generator.dto.ColumnsValueRelationDTO;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    /**
     * 处理并设置列备注的值对应说明关系
     * @param columns 列数据
     */
    public static void handleAndSetColumnsValueRelations(List<ColumnConfigDTO> columns) {
        columns.forEach(column -> {
            if (column.isFormat()) {
                List<ColumnCommentValueRelationDTO> columnValueRelations = AuxiliaryKit
                        .parseTableColumnCommentValueRelation(column.get_comment());
                ColumnsValueRelationDTO columnsValueRelations = new ColumnsValueRelationDTO();
                columnsValueRelations.setColumn(column.getName());
                columnsValueRelations.setColumnCommentValueRelations(columnValueRelations);
                column.setColumnCommentValueRelations(columnValueRelations);
            }
        });
    }

    /**
     * 处理并设置所有列备注的值对应说明关系
     * @param columns 列数据
     */
    public static void handleAndSetAllColumnsValueRelations(List<TableColumnDTO> columns) {
        columns.forEach(column -> {
            if (column.getDataType().equals("tinyint")) {
                List<ColumnCommentValueRelationDTO> columnValueRelations = AuxiliaryKit
                        .parseTableColumnCommentValueRelation(column.getComment());
                ColumnsValueRelationDTO columnsValueRelations = new ColumnsValueRelationDTO();
                columnsValueRelations.setColumn(column.getColumnNameCamelCase());
                columnsValueRelations.setColumnCommentValueRelations(columnValueRelations);
                column.setColumnCommentValueRelations(columnValueRelations);
            }
        });
    }

    /**
     * 处理当前数据库表所有列
     * @param tableColumns
     * @return
     */
    public static List<TableColumnDTO> handleTableColumns(List<TableColumnDTO> tableColumns) {
        return tableColumns.stream()
                .peek(tc -> {
                    tc.setComment(tc.getColumnComment());
                    tc.setColumnComment(AuxiliaryKit.parseTableColumnCommentName(tc.getColumnComment()));
                    tc.setColumnNameCamelCase(StrUtil.toCamelCase(tc.getColumnName()));
                    tc.setVariableType(AuxiliaryKit.dataTypeConvertVariableType(tc.getDataType()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 当前数据库表所有列转化为列配置项
     * @param tableColumns
     * @return
     */
    public static List<ColumnConfigDTO> tableColumnsToColumnConfigs(List<TableColumnDTO> tableColumns) {
        List<ColumnConfigDTO> columnConfigs = new ArrayList<>();
        tableColumns.forEach(tc -> {
            if (!"PRI".equals(tc.getColumnKey())) {
                ColumnConfigDTO columnConfig = new ColumnConfigDTO();
                columnConfig.setName(tc.getColumnNameCamelCase());
                columnConfig.setComment(tc.getColumnComment());
                columnConfig.set_comment(tc.getComment());
                columnConfig.setDataType(tc.getDataType());
                columnConfig.setIsNullable(tc.getIsNullable());
                columnConfig.setSort(false);
                columnConfig.setFormat(tc.getDataType().equals("tinyint"));
                columnConfigs.add(columnConfig);
            }
        });
        return columnConfigs;
    }

    /**
     * 数据库类型转化为java类型
     * @param dataType 数据库类型
     * @return
     */
    public static String dataTypeConvertVariableType(String dataType) {
        return AuxiliaryKit.getDbColumnTypeEnumByDbDataType(dataType).getJavaType();
    }

    public static Set<String> getEntityImportPkgs(List<TableColumnDTO> tableColumns) {
        Set<String> sets = new HashSet<>();
        tableColumns.forEach(tc -> {
            String javaType = AuxiliaryKit.dataTypeConvertVariableType(tc.getDataType());
            JavaTypeEnum javaTypeEnum = JavaTypeEnum.get(javaType);
            if (javaTypeEnum == null) {
                throw new RuntimeException("异常，不支持的Java类型：" + javaType);
            }
            if (StrUtil.isNotBlank(javaTypeEnum.getPkg())) {
                sets.add(javaTypeEnum.getPkg());
            }
        });
        return sets;
    }

    private static DbColumnTypeEnum getDbColumnTypeEnumByDbDataType(String dataType) {
        DbColumnTypeEnum dbColumnTypeEnum = DbColumnTypeEnum.get(dataType);
        if (dbColumnTypeEnum == null) {
            throw new RuntimeException("发生异常，不支持的数据库类型：" + dataType);
        }
        return dbColumnTypeEnum;
    }

    /**
     * 获取代码生成路径
     * @param typeEnum 模板类型
     * @param moduleName 模块名称
     * @param funName   方法名
     * @return
     */
    public static String getGenerateCodePath(TemplateTypeEnum typeEnum, String moduleName, String funName) {
        ConfigConstant.PackageConfig packageConfig = new ConfigConstant.PackageConfig();
        String pack = "", fileName = "", path = "";
        switch (typeEnum.getType()) {
            case "ENTITY":
                pack = packageConfig.ENTITY_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + ".java";
                break;
            case "MAPPER":
                pack = packageConfig.MAPPER_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + "Mapper.java";
                break;
            case "SERVICE":
                pack = packageConfig.SERVICE_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + "Service.java";
                break;
            case "SERVICE_IMPL":
                pack = packageConfig.SERVICE_IMPL_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + "ServiceImpl.java";
                break;
            case "CONTROLLER":
                pack = packageConfig.CONTROLLER_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + "Controller.java";
                break;
            case "XML":
                pack = packageConfig.XML_PACKAGE;
                fileName = StrUtil.upperFirst(funName) + "Mapper.xml";
                break;
            default:
                if (typeEnum.getType().equals("TABLE") || typeEnum.getType().equals("TREE_GRID")) {
                    path = "templates/" + moduleName;
                    fileName = funName + ".html";
                } else if (typeEnum.getType().equals("OPERATION")) {
                    path = "templates/" + moduleName;
                    fileName = funName + "_edit.html";
                }
        }
        if (!"".equals(pack)) {
            path = StrUtil.replace(packageConfig.BASE_PACKAGE + ".modules", ".", "/") + "/" + moduleName + "/" + pack;
        }

        if ("".equals(path)) {
            throw new RuntimeException("无法获取代码生成路径，请检查模板是否存在");
        }

        path = ConfigConstant.CODE_GEN_PATH + "/" + ConfigConstant.CODE_FOLDER_NAME + "/" + path;

        return path + "/" + fileName;
    }

}
