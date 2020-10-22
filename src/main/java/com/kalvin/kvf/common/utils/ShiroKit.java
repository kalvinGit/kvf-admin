package com.kalvin.kvf.common.utils;

import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.shiro.UserRealm;
import com.kalvin.kvf.modules.sys.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 作用：Shiro工具类<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 13:35
 */
public class ShiroKit {

    /**
     * 密码散列算法为md5
     */
    public final static String HASH_ALGORITHM_NAME = "MD5";
    /**
     * 散列迭代次数
     */
    public final static int HASH_ITERATIONS = 1;

    public static String md5(String password) {
        return new SimpleHash(HASH_ALGORITHM_NAME, password, HASH_ITERATIONS).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static User getUser() {
        return (User)SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        Object kaptcha = getSessionAttribute(key);
        if(kaptcha == null){
            throw new KvfException("验证码已失效");
        }
        getSession().removeAttribute(key);
        return kaptcha.toString();
    }

    /**
     * 刷新权限
     */
    public static void flushPrivileges() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        // 只刷新当前用户权限
//        realm.clearCachedAuthorization();
        // 刷新所有在线用户权限
        Cache<Object, AuthorizationInfo> cache = realm.getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

}
