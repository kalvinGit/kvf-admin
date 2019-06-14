package com.kalvin.kvf.gen.vo;

import com.kalvin.kvf.gen.dto.ButtonConfigDTO;
import com.kalvin.kvf.gen.dto.ColumnConfigDTO;
import com.kalvin.kvf.gen.dto.QueryColumnConfigDTO;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成核心配置封装类
 * @author Kalvin
 * @Date 2019/06/13 10:39
 */
@Data
@Accessors(chain = true)
@ToString
public class GenConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;
    private String primaryKey;  // 主键字段
    private String moduleName;   // 模块名称：如sys
    private String funName;     // 功能名称：如user
    private String tableType;   // 类型：table、treegrid
    private List<ColumnConfigDTO> columns;
    private List<QueryColumnConfigDTO> queryColumns;
    private List<ButtonConfigDTO> headButtons;
    private List<ButtonConfigDTO> rowButtons;
}
