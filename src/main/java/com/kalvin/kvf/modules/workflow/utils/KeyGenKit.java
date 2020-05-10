package com.kalvin.kvf.modules.workflow.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

/**
 * Create by Kalvin on 2020/5/4.
 */
public class KeyGenKit {

    public static String getKey(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        return prefix + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(4);
    }
}
