package com.kalvin.kvf.modules.generator.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
public class TableDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;
    private String tableComment;
}
