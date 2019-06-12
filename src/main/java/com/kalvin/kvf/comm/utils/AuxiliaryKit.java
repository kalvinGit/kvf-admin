package com.kalvin.kvf.comm.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 辅助工具
 * @author Kalvin
 */
public class AuxiliaryKit {

    public static String handleTableColumnCommentRemark(String columnComment) {
        if (StrUtil.isBlank(columnComment)) {
            return "";
        }
        String[] split = columnComment.split("。");
        return split[0];
    }

}
