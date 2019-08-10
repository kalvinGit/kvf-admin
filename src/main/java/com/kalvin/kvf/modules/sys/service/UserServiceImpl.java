package com.kalvin.kvf.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.modules.sys.entity.User;
import com.kalvin.kvf.modules.sys.mapper.UserMapper;
import com.kalvin.kvf.modules.sys.vo.UserQueryVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Page<User> listUserPage(UserQueryVO queryVO) {
        Page<User> page = new Page<>(queryVO.getCurrent(), queryVO.getSize());
        List<User> users = baseMapper.selectUserList(queryVO, page);
        return page.setRecords(users);
    }

    @Override
    public User getByUsername(String username) {
        return super.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void updateUserPassword(Long id, String password) {
        super.update(new LambdaUpdateWrapper<User>()
                .set(User::getPassword, password)
                .eq(User::getId, id));
    }
}
