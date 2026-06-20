package com.network.dal.mapper;

import com.network.dal.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(UserEntity user);

    int updateById(UserEntity user);

    int deleteById(@Param("id") Long id);

    UserEntity selectById(@Param("id") Long id);

    UserEntity selectByUsername(@Param("username") String username);

    List<UserEntity> selectList(UserEntity user);

    long selectCount(UserEntity user);

}
