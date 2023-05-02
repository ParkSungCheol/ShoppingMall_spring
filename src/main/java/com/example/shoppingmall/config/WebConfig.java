package com.example.shoppingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.shoppingmall.AuthLoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://ec2-43-200-181-206.ap-northeast-2.compute.amazonaws.com:8080", "http://localhost:8080", "https://www.jurospring.o-r.kr")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "HEAD")
                .allowCredentials(true)
                .exposedHeaders("Token");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthLoginInterceptor())
                .addPathPatterns("/*") // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
                .excludePathPatterns("/goods", "/login", "/sendEmail", "/checkEmail", "/sendMessage", "/checkMessage", "/signup", "/existCheck", "/updateUser"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }
}