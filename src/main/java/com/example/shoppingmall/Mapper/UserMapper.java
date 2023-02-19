package com.example.shoppingmall.Mapper;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.shoppingmall.Domain.User;

@Mapper
public interface UserMapper {

    List<User> getUserList();
    User loginCheck(@Param("user") User user);
}