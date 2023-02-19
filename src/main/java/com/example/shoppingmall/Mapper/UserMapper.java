package com.example.shoppingmall.Mapper;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;

import com.example.shoppingmall.Domain.User;

@Mapper
public interface UserMapper {

    List<User> getUserList();
    List<User> loginCheck(user);
}