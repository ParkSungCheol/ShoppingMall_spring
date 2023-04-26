package com.example.shoppingmall.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.shoppingmall.Domain.User;

@Mapper
public interface UserMapper {

    List<User> getUserList();
    User existCheck(@Param("user") User user);
    void insertUser(@Param("user") User user);
    void updateUser(@Param("user") User user);
    void deleteUser(@Param("user") User user);
}