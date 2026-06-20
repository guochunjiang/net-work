package com.network.service;

import com.network.common.base.PageResult;
import com.network.dal.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity createUser(UserEntity user);

    UserEntity updateUser(UserEntity user);

    void deleteUser(Long id);

    UserEntity getUserById(Long id);

    UserEntity getUserByUsername(String username);

    PageResult<UserEntity> listUsers(UserEntity query, long page, long pageSize);

    List<UserEntity> listAll(UserEntity query);

}
