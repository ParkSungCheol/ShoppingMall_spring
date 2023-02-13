package com.example.shoppingmall;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.GoodsService;
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
@CrossOrigin(origins = {"http://ec2-43-200-181-206.ap-northeast-2.compute.amazonaws.com:8080", "http://localhost:8080", "https://www.juromarket.kro.kr"})
public class TestController {

	@Autowired
    private UserService userService;
	@Autowired
	private GoodsService goodsService;

    @GetMapping("/")
    public String getTest () {
        return "jenkins test";
    }

    @GetMapping("/user")
    public List<User> getUserList () {
        return userService.getUserList();
    }
    
    @GetMapping("/goods")
    public List<Goods> getGoodsList () {
        return goodsService.getGoodsList();
    }
}
