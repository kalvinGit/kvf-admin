package com.kalvin.kvf.gen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kalvin.kvf.gen.dto.TableColumnDTO;
import com.kalvin.kvf.gen.dto.TableDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TableMapper {

    @Select("select table_name,table_comment from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<TableDTO> listTable(IPage page);

    @Select("select table_name,table_comment from information_schema.TABLES where TABLE_SCHEMA=(select database()) and table_name like concat(#{tableName}, '%')")
    List<TableDTO> listTableByName(String tableName, IPage page);

    @Select("select column_name, column_comment, column_type, data_type from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<TableColumnDTO> listTableColumn(String tableName);

}
