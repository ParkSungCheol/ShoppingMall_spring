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
    public List<User> loginCheck(User user) {
    	List<User> result = userMapper.loginCheck(user);
    	if(result != null) {
    		
    	}
    	return result;
    }

}