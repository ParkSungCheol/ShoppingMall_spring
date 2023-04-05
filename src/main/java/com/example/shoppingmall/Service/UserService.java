package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mail.MailHandler;
import com.example.shoppingmall.Mail.TempKey;
import com.example.shoppingmall.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class UserService {

	@Autowired
    private UserMapper userMapper;
	@Autowired
	private JavaMailSender mailSender;

    public List<User> getUserList() {
        return userMapper.getUserList();
    }
    
    public boolean loginCheck(User user, HttpSession session) {
    	User result = userMapper.loginCheck(user);
    	if(result != null && result.getPwd().equals(user.getPwd())) {
    		System.out.println("Success " + result.getId() + " " + result.getPwd());
    		session.setAttribute("loginUserId", result);
    		return true;
    	}
    	else {
    		System.out.println("Failed " + user.getId() + " " + user.getPwd());
    		return false;
    	}
    }
    
    public String sendEmail(String email) throws Exception {
    	String mailKey = new TempKey().getKey(30, false);
    	MailHandler sendMail = new MailHandler(mailSender);
    	
    	sendMail.setSubject("[shoppingMall 인증메일 입니다.]");
    	sendMail.setText(
    			"<h1>shoppingMall 메일인증</h1>" +
    			"<br>shoppingMall에 오신것을 환영합니다!" +
				"<br>아래 인증번호를 입력해주세요." +
    			"<br>" + mailKey);
    	sendMail.setFrom("skfo8gmlakd@gmail.com", "shoppingMall");
    	sendMail.setTo(email);
    	sendMail.send();
    	
    	return mailKey;
    }
}