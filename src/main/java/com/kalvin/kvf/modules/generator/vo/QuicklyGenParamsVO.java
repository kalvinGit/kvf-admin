package com.kalvin.kvf.modules.generator.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Create by Kalvin on 2019/6/20.
 */
@Data
@Accessors(chain = true)
@ToString
public class QuicklyGenParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;
    private String tableType;
    private String tableComment;
}
