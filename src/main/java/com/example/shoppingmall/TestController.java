package com.example.shoppingmall;

import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://ec2-43-200-181-206.ap-northeast-2.compute.amazonaws.com", "http://localhost", "http://www.juromarket.kro.kr"})
public class TestController {

	@Autowired
    private UserService userService;

    @GetMapping("/")
    public String getTest () {
        return "TTTTtest1212..?";
    }

    @GetMapping("/data")
    public List<User> getUserList () {
        return userService.getUserList();
    }
}
