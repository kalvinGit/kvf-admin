package com.kalvin.kvf.common.xss;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * SQL过滤
 * Create by Kalvin on 2019/6/25.
 */
@Slf4j
public final class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public String cleanSqlKeyWords(String str) {
        final String oldStr = str;
        if(StrUtil.isBlank(str)) {
            return "";
        }
        if (StrUtil.isNullOrUndefined(str)) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = StrUtil.replace(str, "'", "");
        str = StrUtil.replace(str, "\"", "");
        str = StrUtil.replace(str, ";", "");
        str = StrUtil.replace(str, "\\", "");

        // 转换成小写
//        str = str.toLowerCase();

        String[] values = str.split(" ");

        if (values.length < 2) {
            return str;
        }

        // 非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop", "show"};
        String badKeyStr = "'|and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#";

        // 判断是否包含非法字符
        for (String badKey : badKeyStr.split("\\|")) {
            for (String value : values) {
                if (value.equalsIgnoreCase(badKey)) {
                    str = StrUtil.replace(str, badKey, "INVALID");
                    log.error("当前参数({})包含非法的sql关键词({})，系统已自动过滤。", oldStr, badKey);
                }
            }
        }

        return str;
    }

}
