package com.kalvin.kvf.gen.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

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

}
