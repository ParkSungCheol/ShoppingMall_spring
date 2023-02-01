package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private UserMapper userMapper;

    public List<User> getUserList() {
        return userMapper.getUserList();
    }
}