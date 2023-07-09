package com.example.shoppingmall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.example.shoppingmall.Domain.User;

public class AuthLoginInterceptor implements HandlerInterceptor{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("preHandle");
		// POST나 GET 요청이 아닌 Preflight 요청 등은 쿠키를 보내지 않으므로 패스
		if (request.getMethod().equals("POST") || request.getMethod().equals("GET")) {
			if(request.getSession(false) != null) {
				User user = (User) request.getSession(false).getAttribute("loginUserId");
				if(user != null) {
					return true;
				}
			}
			// 세션 유효하지 않은 경우 300번대 응답 전달(브라우저에서 오류 보이지 않게 조치)
            response.setStatus(HttpStatus.SC_SEE_OTHER);
            return false; // 컨트롤러 실행을 중지하고 요청을 중단
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
}
