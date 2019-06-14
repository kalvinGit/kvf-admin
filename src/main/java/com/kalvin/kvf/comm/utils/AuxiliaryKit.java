package com.kalvin.kvf.comm.utils;

import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.gen.dto.ColumnCommentValueRelationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助工具
 * @author Kalvin
 */
public class AuxiliaryKit {

    public static String parseTableColumnCommentName(String columnComment) {
        if (StrUtil.isBlank(columnComment)) {
            return "";
        }
        String[] split = columnComment.split("。");
        return split[0];
    }

    /**
     * 解析表列备注的值对应说明关系
     * 注：列注释必须要符合统一规范，如：sex的注释为：性别。0：未知；1：男；2：女
     * 句号前部分为列说明，句号后部分为列其它说明，如果需要声明值对应说明关系，格式为value：说明（如上面例子，注意：分隔符都是中文的）；
     * @param columnComment 列注释
     * @return list
     */
    public static List<ColumnCommentValueRelationDTO> parseTableColumnCommentValueRelation(String columnComment) {
        List<ColumnCommentValueRelationDTO> list = new ArrayList<>();
        if (StrUtil.isBlank(columnComment) || !columnComment.contains("。")) {
            return list;
        }
        String[] split = columnComment.split("。");
        String vr = split[1].trim();
        if (!vr.contains("：")) {
            throw new RuntimeException("格式化列失败：无法解析列注释，注释不符合统一规范：" + vr);
        }
        for (String item : vr.split("；")) {
            String[] itemArr = item.split("：");
            list.add(new ColumnCommentValueRelationDTO(itemArr[0], itemArr[1]));
        }
        return list;
    }

}
