package com.kalvin.kvf.modules.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.modules.generator.dto.TableColumnDTO;
import com.kalvin.kvf.modules.generator.dto.TableDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TableMapper {

    @Select("select table_name,table_comment from information_schema.TABLES where TABLE_SCHEMA=(select database()) and table_name not like 'qrtz_%' and table_name not like 'QRTZ_%'")
    List<TableDTO> listTable(IPage page);

    @Select("select table_name,table_comment from information_schema.TABLES where TABLE_SCHEMA=(select database()) and table_name like concat(#{tableName}, '%')")
    List<TableDTO> listTableByName(String tableName, IPage page);

    @Select("select column_name, column_comment, column_type, data_type, is_nullable, column_key from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName} order by ORDINAL_POSITION asc")
    List<TableColumnDTO> listTableColumn(String tableName);

}
