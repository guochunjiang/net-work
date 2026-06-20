package com.network.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.network.common.base.PageResult;
import com.network.common.constant.ResultCode;
import com.network.common.exception.GlobalException;
import com.network.dal.entity.UserEntity;
import com.network.dal.mapper.UserMapper;
import com.network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEntity createUser(UserEntity user) {
        UserEntity existing = userMapper.selectByUsername(user.getUsername());
        if (existing != null) {
            throw new GlobalException(ResultCode.DATA_ALREADY_EXISTS);
        }
        user.setStatus(user.getStatus() == null ? 1 : user.getStatus());
        userMapper.insert(user);
        return userMapper.selectById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEntity updateUser(UserEntity user) {
        UserEntity existing = userMapper.selectById(user.getId());
        if (existing == null) {
            throw new GlobalException(ResultCode.DATA_NOT_FOUND);
        }
        userMapper.updateById(user);
        return userMapper.selectById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        UserEntity existing = userMapper.selectById(id);
        if (existing == null) {
            throw new GlobalException(ResultCode.DATA_NOT_FOUND);
        }
        userMapper.deleteById(id);
    }

    @Override
    public UserEntity getUserById(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new GlobalException(ResultCode.DATA_NOT_FOUND);
        }
        return user;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageResult<UserEntity> listUsers(UserEntity query, long page, long pageSize) {
        PageHelper.startPage((int) page, (int) pageSize);
        List<UserEntity> list = userMapper.selectList(query);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(list);
        return PageResult.of(page, pageSize, pageInfo.getTotal(), list);
    }

    @Override
    public List<UserEntity> listAll(UserEntity query) {
        return userMapper.selectList(query);
    }

}
