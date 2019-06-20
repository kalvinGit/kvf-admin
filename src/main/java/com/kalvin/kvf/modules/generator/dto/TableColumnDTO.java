package com.kalvin.kvf.modules.generator.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class TableColumnDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnName;
    private String columnNameCamelCase;
    private String columnComment;   // 仅列名称备注，不包括其它说明
    private String comment; // 列备注（全）
    private String columnType;
    private String dataType;
    private String isNullable;  // YES/NO
    private String columnKey;   // PRI(主键)
    private String variableType;   // 成员变量类型
    private List<ColumnCommentValueRelationDTO> columnCommentValueRelations;    // 列值对应说明关系列表数据

}
