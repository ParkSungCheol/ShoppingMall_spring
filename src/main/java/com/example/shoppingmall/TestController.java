package com.example.shoppingmall;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.GoodsService;
import com.example.shoppingmall.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

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
    	System.out.println("test");
        return "2023-02-21 test";
    }

    @GetMapping("/user")
    @Transactional(value="txManager")
    public List<User> getUserList () {
        return userService.getUserList();
    }
    
    @GetMapping("/goods")
    @Transactional(value="txManager")
    public List<Goods> getGoodsList () {
        return goodsService.getGoodsList();
    }
    
    @GetMapping("/test")
    @Transactional(value="txManager")
    public void insertDeleteUserList() {
    	goodsService.deleteGoodsList();
    	goodsService.insertGoodsList();
    }
    
    @GetMapping("/login")
    @Transactional(value="txManager")
    public ResponseEntity<?> loginCheck(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	User user = objectMapper.convertValue(param, User.class);
    	boolean isVaild = userService.loginCheck(user, session);
    	
    	if(isVaild) {
    		return new ResponseEntity<>(null, HttpStatus.OK); 
    	}
    	
    	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/getSession")
    @Transactional(value="txManager")
    public void getSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	System.out.println(request.getCookies());
    }

}
