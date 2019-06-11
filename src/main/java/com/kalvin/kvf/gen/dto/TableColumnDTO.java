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
    private String columnComment;
    private String columnType;
    private String dataType;
}
