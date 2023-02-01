package com.example.shoppingmall;

import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("http://ec2-43-201-71-180.ap-northeast-2.compute.amazonaws.com:8080")
public class TestController {

    private UserService userService;

    @GetMapping("/")
    public String getTest () {
        return "how..?";
    }

    @GetMapping("/data")
    public List<User> getUserList () {
        return userService.getUserList();
    }
}
