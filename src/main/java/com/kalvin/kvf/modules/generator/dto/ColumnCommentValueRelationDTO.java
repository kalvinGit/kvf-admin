package com.kalvin.kvf.modules.generator.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 列备注的值对应说明关系，如：0：正常；1：禁用
 * @author Kalvin
 * @Date 2019/06/14 11:01
 */
@Data
@Accessors(chain = true)
@ToString
public class ColumnCommentValueRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;

    public ColumnCommentValueRelationDTO(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
