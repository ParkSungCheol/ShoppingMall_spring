package com.example.shoppingmall;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Service.GoodsService;
import com.example.shoppingmall.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TestController {

	@Autowired
    private UserService userService;
	@Autowired
	private GoodsService goodsService;

    @GetMapping("/")
    public String getTest () {
    	System.out.println("test");
        return "2023-03-03";
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
    
    @GetMapping("/login")
    @Transactional(value="txManager")
    public ResponseEntity<?> loginCheck(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	User user = objectMapper.convertValue(param, User.class);
    	boolean isVaild = userService.loginCheck(user, session);
    	System.out.println("session::::::::::::"+session);
    	System.out.println("isValid::::::::::::"+isVaild);
    	if(isVaild) {
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
	    	session.invalidate();
	    	return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/getSession")
    @Transactional(value="txManager")
    public ResponseEntity<?> getSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User user = (User) request.getSession(false).getAttribute("loginUserId");
    	System.out.println(user);
    	if(user != null) {
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/logout")
    @Transactional(value="txManager")
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
    	
    	if(session != null) {
    		session.invalidate();
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }

    @GetMapping("/sendEmail")
    @Transactional(value="txManager")
    public ResponseEntity<?> sendEmail(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
     	String email = param.get("email");
    	String mailKey = userService.sendEmail(email);
    	
    	session.setAttribute("mailKey", mailKey);
    	return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    
    @GetMapping("/checkEmail")
    @Transactional(value="txManager")
    public ResponseEntity<?> checkEmail(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
     	String userMailKey = param.get("email");
    	String mailKey = (String) session.getAttribute("mailKey");
    	
    	if(userMailKey.equals(mailKey)) {
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
}
