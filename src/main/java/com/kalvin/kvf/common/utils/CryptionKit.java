package com.kalvin.kvf.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.kalvin.kvf.common.constant.SysConstant;

/**
 * 作用：加密解密<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 14:48
 */
public class CryptionKit {

    /**
     * 【作用】生成初始密码<br>
     * 【说明】系统用户密码
     * @param
     * @author Kalvin
     * @Date 2018/1/4 17:14
     */
    public static String genUserPwd() {
        return DigestUtil.md5Hex(SysConstant.DEFAULT_PWD);
    }


    /**
     * 生成并加密密码
     * @param pwd
     * @return
     */
    public static String genUserPwd(String pwd) {
        if (StrUtil.isEmpty(pwd)) {
            return genUserPwd();
        }
        return DigestUtil.md5Hex(pwd);
    }

}
