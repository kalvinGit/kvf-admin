package com.kalvin.kvf.modules.workflow.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by Kalvin on 2020/4/22.
 */
@Data
@ToString
@Accessors(chain = true)
public class ModelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String modelName;
    private String modelKey;
    private String description;

}
