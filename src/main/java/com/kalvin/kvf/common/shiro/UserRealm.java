package com.kalvin.kvf.common.shiro;

import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.common.constant.SysConstant;
import com.kalvin.kvf.common.utils.CryptionKit;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.modules.sys.entity.Menu;
import com.kalvin.kvf.modules.sys.entity.User;
import com.kalvin.kvf.modules.sys.service.IMenuService;
import com.kalvin.kvf.modules.sys.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 作用：认证授权<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 13:30
 */
@Component
public class UserRealm extends AuthorizingRealm {

    // 在shiro框架中(UserRealm)使用过@Autowire注入的类，缓存注解和事务注解都失效。
    // 解决方法：
    // 1.在Shiro框架中注入Bean时，不使用@Autowire，使用ApplicationContextRegister.getBean()方法，手动注入bean。保证该方法只有在程序完全启动运行时，才被注入。
    // 2.使用@Autowire+@Lazy注解，设置注入到Shiro框架的Bean延时加载（即在第一次使用的时候加载）。
    // 原文链接：https://blog.csdn.net/elonpage/article/details/78965176
    @Lazy
    @Autowired
    private IUserService userService;

    @Lazy
    @Autowired
    private IMenuService menuService;

    /**
     * 授权(验证权限时调用)
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User)principals.getPrimaryPrincipal();
        Long userId = user.getId();
        String username = user.getUsername();

        List<String> permsList;

        // todo 系统管理员，拥有最高权限（不用在系统设置任何权限）
        /*if (SysConstant.ADMIN.equals(username)) {
            List<Menu> menuList = menuService.list();
            permsList = new ArrayList<>(menuList.size());
            for (Menu menu : menuList) {
                permsList.add(menu.getPermission());
            }
        } else {
            permsList = menuService.getPermission(userId);
        }*/
        permsList = menuService.getPermission(userId);

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StrUtil.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);

        return info;
    }

    /**
     * 认证(登录时调用)
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        //查询用户信息
        User user = userService.getByUsername(token.getUsername());
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        String password = CryptionKit.genUserPwd(new String((char[]) token.getCredentials()));
        if (!user.getPassword().equals(password)) {
            // 密码错误
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        if (user.getStatus() != SysConstant.VALID_USER) {
            // 账号锁定
            throw new LockedAccountException("账号已被锁定，请联系管理员");
        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroKit.HASH_ALGORITHM_NAME);
        shaCredentialsMatcher.setHashIterations(ShiroKit.HASH_ITERATIONS);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }

    /**
     * 清理权限缓存
     */
    public void clearCachedAuthorization() {
        super.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
