package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

	@Autowired
    private UserMapper userMapper;

    public List<User> getUserList() {
        return userMapper.getUserList();
    }
    
    public void deleteUserList() {
    	userMapper.deleteUserList();
    }
    
    public void insertUserList() {
    	userMapper.insertUserList();
    }
}