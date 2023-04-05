package com.example.shoppingmall.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean
	public JavaMailSender javaMailSender(@Value("${spring.mail.host}") String host, 
										 @Value("${spring.mail.properties.mail.smtp.socketFactory.port}") int port,
										 @Value("${spring.mail.username}") String username,
										 @Value("${spring.mail.password}") String password) {
		Properties mailProperties = new Properties();
		mailProperties.put("mail.transport.protocol", "smtp");
		mailProperties.put("mail.smtp.auth", "true");
		mailProperties.put("mail.smtp.starttls.enable", "true");
		mailProperties.put("mail.smtp.debug", "true");
		mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("utf-8");
		return mailSender;
	}

}
