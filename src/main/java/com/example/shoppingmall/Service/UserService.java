package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class UserService {

	@Autowired
    private UserMapper userMapper;

    public List<User> getUserList() {
        return userMapper.getUserList();
    }
    
    public boolean loginCheck(User user, HttpSession session) {
    	User result = userMapper.loginCheck(user);
    	if(result != null && result.getPwd().equals(user.getPwd())) {
    		System.out.println("Success " + result.getId() + " " + result.getPwd());
    		session.setAttribute("loginUser", result);
    		return true;
    	}
    	else {
    		System.out.println("Failed " + user.getId() + " " + user.getPwd());
    		return false;
    	}
    }

}