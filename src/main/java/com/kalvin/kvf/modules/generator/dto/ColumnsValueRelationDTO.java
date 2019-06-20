package com.kalvin.kvf.modules.generator.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kalvin
 * @Date 2019/06/14 12:00
 */
@Data
@Accessors(chain = true)
@ToString
public class ColumnsValueRelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String column;
    private List<ColumnCommentValueRelationDTO> columnCommentValueRelations;
}
