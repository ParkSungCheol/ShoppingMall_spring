package com.example.shoppingmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.shoppingmall.AuthLoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	private final Environment env;
	
	@Autowired
    public WebConfig(Environment env) {
        this.env = env;
    }
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	String allowedOrigins = env.getProperty("web.allowedOrigins");
    	String[] allowedOriginsArray = allowedOrigins.split(",");
    	
        registry.addMapping("/**")
        		// CORS 허용 Origins
                .allowedOrigins(allowedOriginsArray)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "HEAD")
                // 쿠키 허용
                .allowCredentials(true)
                .exposedHeaders("Token", "Access-Control-Allow-Origin");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthLoginInterceptor())
                .addPathPatterns("/*") // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
                // session 검증 불필요한 API 요청
                .excludePathPatterns("/goods", "/login", "/sendEmail", "/checkEmail", "/sendMessage", "/checkMessage", "/signup", "/existCheck", "/updatePwd"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }
}