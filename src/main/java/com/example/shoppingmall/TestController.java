package com.example.shoppingmall;

import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final UserService userService;

    @GetMapping("/")
    public String getUserList () {
        return "11";
    }
}
