package com.example.shoppingmall.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mail.MailHandler;
import com.example.shoppingmall.Mail.TempKey;
import com.example.shoppingmall.Mapper.UserMapper;

@Service
public class UserService {

	private final UserMapper userMapper;
    private final JavaMailSender mailSender;
	private final Environment env;

    @Autowired
    public UserService(Environment env, UserMapper userMapper, JavaMailSender mailSender) {
        this.env = env;
        this.userMapper = userMapper;
        this.mailSender = mailSender;
    }

    public List<User> getUserList() {
        return userMapper.getUserList();
    }
    
    public User existCheck(User user) {
    	User result = userMapper.existCheck(user);
    	if(result != null) {
    		return result;
    	}
    	else {
    		return null;
    	}
    }
    
    public String sendEmail(String email) throws Exception {
    	String mailKey = new TempKey().getKey(30, false);
    	return mailKey;
    }
    
    // Async 어노테이션 이용해서 비동기처리
    @Async("mailExecutor")
    public void sendJoinCertificationMail(String mailKey, String email) throws MessagingException, UnsupportedEncodingException {
		MailHandler sendMail = new MailHandler(mailSender);
    	
    	sendMail.setSubject("[shoppingMall 인증메일 입니다.]");
    	sendMail.setText(
    			"<h1>shoppingMall 메일인증</h1>" +
    			"<br>shoppingMall에 오신것을 환영합니다!" +
				"<br>아래 인증번호를 입력해주세요." +
    			"<br>" + mailKey);
    	sendMail.setFrom(env.getProperty("mail.username") + "@gmail.com", "shoppingMall");
    	sendMail.setTo(email);
    	sendMail.send();
    }
    
    public void insertUser(User user) {
    	userMapper.insertUser(user);
    }
    
    public void updateUser(User user) {
    	userMapper.updateUser(user);
    }
    
    public void deleteUser(User user) {
    	userMapper.deleteUser(user);
    }
}