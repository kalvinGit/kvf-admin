package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.modules.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.modules.sys.vo.UserQueryVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
public interface IUserService extends IService<User> {

    /**
     * 获取用户列表。分页
     * @param queryVO 查询参数
     * @return
     */
    Page<User> listUserPage(UserQueryVO queryVO);

    User getByUsername(String username);

    void updateUserPassword(Long id, String password);

}
