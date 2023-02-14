package com.example.shoppingmall.Mapper;

import com.example.shoppingmall.Domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getUserList();
    void deleteUserList();
    void insertUserList();
}