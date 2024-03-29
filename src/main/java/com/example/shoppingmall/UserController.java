package com.example.shoppingmall;

import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shoppingmall.Domain.User;
import com.example.shoppingmall.Mail.TempKey;
import com.example.shoppingmall.Service.EncryptService;
import com.example.shoppingmall.Service.PhoneService;
import com.example.shoppingmall.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserController {

	@Autowired
    private UserService userService;
	@Autowired
	private PhoneService phoneService;
	@Autowired
	private EncryptService encryptService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping("/getSession")
    @Transactional(value="txManager")
    public ResponseEntity<?> getSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User user = (User) request.getSession(false).getAttribute("loginUserId");
    	if(user != null) {
    		return new ResponseEntity<>(user, HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/login")
    @Transactional(value="txManager")
    public ResponseEntity<?> loginCheck(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	String selected = param.get("selected");
    	
    	// 사용자가 로그인유지 선택 시 유효시간 MAX_VALUE 할당
    	if(selected.equals("1")) {
    		Cookie cookie = new Cookie("JSESSIONID", session.getId());
        	cookie.setMaxAge(Integer.MAX_VALUE);
        	response.addCookie(cookie);
    	}
    	
    	String id = param.get("id");
    	String pwd = param.get("pwd");
    	User user = new User();
    	user.setId(id);
    	user.setPwd(pwd);
    	User checkedUser = userService.existCheck(user);
    	
    	// 비밀번호 검증은 (입력받은 비밀번호 + 회원가입 시 설정된 salt값)의 인코딩값 과 DB에 저장되어있는 인코딩 결과값을 비교
    	if(checkedUser != null && encryptService.encrypt(user.getPwd() + checkedUser.getSalt()).equals(checkedUser.getPwd())) {
    		// 로그인 검증완료 시 세션에 로그인 정보 저장
    		session.setAttribute("loginUserId", checkedUser);
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
	    	session.invalidate();
	    	return new ResponseEntity<>("notFound", HttpStatus.SEE_OTHER);
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
    	userService.sendJoinCertificationMail(mailKey, email);
    	
    	// 추후 검증할 키값을 세션에 저장
    	session.setAttribute("mailKey", mailKey);
    	return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    
    @GetMapping("/checkEmail")
    @Transactional(value="txManager")
    public ResponseEntity<?> checkEmail(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
     	String userMailKey = param.get("email");
    	String mailKey = (String) session.getAttribute("mailKey");
    	
    	// 세션에 저장된 키값과 비교하여 이메일 검증
    	if(userMailKey.equals(mailKey)) {
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String phone = param.get("phone");
        String num = phoneService.sendMessage(phone);
        
        // 추후 검증할 키값을 세션에 저장
        session.setAttribute("num", num);
        
        if(num != null ) {
        	return new ResponseEntity<>("ok", HttpStatus.OK);
        }
        else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/checkMessage")
    @Transactional(value="txManager")
    public ResponseEntity<?> checkMessage(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
     	String userPhoneKey = param.get("phone");
    	String num = (String) session.getAttribute("num");
    	
    	// 세션에 저장된 키값과 비교하여 핸드폰번호 검증
    	if(userPhoneKey.equals(num)) {
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
    	}
    }
    
    @GetMapping("/existCheck")
    @Transactional(value="txManager")
    public ResponseEntity<?> idCheck(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ObjectMapper objectMapper = new ObjectMapper();
    	User user = objectMapper.convertValue(param, User.class);
    	User checkedUser = userService.existCheck(user);
    	
    	if(checkedUser != null) {
    		return new ResponseEntity<>(checkedUser, HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.ACCEPTED);
    	}
    }
    
    @GetMapping("/signup")
    @Transactional(value="txManager")
    public ResponseEntity<?> signup(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
     	String id = param.get("id");
     	String pwd = param.get("pwd");
     	String name = param.get("name");
     	String year = param.get("year");
     	String month = param.get("month").length() == 1 ? "0"+param.get("month") : param.get("month");
     	String day = param.get("day").length() == 1 ? "0"+param.get("day") : param.get("day");
     	String addressNumber = param.get("addressNumber");
     	String address = param.get("address");
     	String addressDetail = param.get("addressDetail");
     	String addressDetail2 = param.get("addressDetail2");
     	String email = param.get("email");
     	String phone = param.get("phone");
    	
     	User user = new User();
     	user.setId(id);
     	
     	// 비밀번호 암호화 시 필요한 salt값 랜덤생성
     	String num = new TempKey().getKey(4, true);
     	user.setSalt(num);
     	user.setPwd(encryptService.encrypt(pwd + num));
     	user.setName(name);
     	user.setBirth(year+month+day);
     	if(!addressDetail.equals("")) user.setAddress(addressNumber+"^"+address+"^"+addressDetail+"^"+addressDetail2);
     	else user.setAddress(addressNumber+"^"+address+"^"+addressDetail2);
     	user.setEmail(email);
     	user.setPhone(phone);
        user.setGrade('0');
        user.setCategory('U');
     	
        userService.insertUser(user);
        
        //MAPPER.INSERTUSER
        return new ResponseEntity<>("ok", HttpStatus.OK); 
    }
    @GetMapping("/updateUser")
    @Transactional(value="txManager")
    public ResponseEntity<?> updateUser(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
    	
     	String id = param.get("id");
     	String name = param.get("name");
     	String year = param.get("year");
     	String month = param.get("month") != null && param.get("month").length() == 1 ? "0"+param.get("month") : param.get("month");
     	String day = param.get("day")!= null && param.get("day").length() == 1 ? "0"+param.get("day") : param.get("day");
     	String addressNumber = param.get("addressNumber");
     	String address = param.get("address");
     	String addressDetail = param.get("addressDetail");
     	String addressDetail2 = param.get("addressDetail2");
     	String email = param.get("email");
     	String phone = param.get("phone");
     	String beforePwd = param.get("beforePwd");
     	String afterPwd = param.get("afterPwd");
    	
     	User user = new User();
     	user.setId(id);
     	if(name != null && !name.equals("")) user.setName(name);
     	if(year != null && !year.equals("")) user.setBirth(year+month+day);
     	if(addressDetail != null && !addressDetail.equals("")) user.setAddress(addressNumber+"^"+address+"^"+addressDetail+"^"+addressDetail2);
     	else if(addressNumber != null && !addressNumber.equals("")) user.setAddress(addressNumber+"^"+address+"^"+addressDetail2);
     	if(email != null && !email.equals("")) user.setEmail(email);
     	if(phone != null && !phone.equals("")) user.setPhone(phone);
     	
     	// MyPage에서 비밀번호 변경 시 기존 비밀번호 일치하는지 검증
     	if(beforePwd != null && !beforePwd.equals("")) {
     		User checkedUser = userService.existCheck(user);
     		if(checkedUser != null && encryptService.encrypt(beforePwd + checkedUser.getSalt()).equals(checkedUser.getPwd())) {
        		user.setPwd(encryptService.encrypt(afterPwd + checkedUser.getSalt()));
        	}
     		else if(checkedUser != null) {
     			return new ResponseEntity<>("notFound", HttpStatus.NOT_FOUND);
     		}
     		else {
     			return new ResponseEntity<>("internalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
     		}
     	}
     	
        userService.updateUser(user);
        
        // 비밀번호 변경 시 재로그인 유도(세션만료)
        if(beforePwd != null && !beforePwd.equals("")) { 
        	session.invalidate();
        }
        else {
	    	User checkedUser = userService.existCheck(user);
	        session.setAttribute("loginUserId", checkedUser);
        }
        
        return new ResponseEntity<>("ok", HttpStatus.OK); 
    }
    
    @GetMapping("/updatePwd")
    @Transactional(value="txManager")
    public ResponseEntity<?> updatePwd(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	ObjectMapper objectMapper = new ObjectMapper();
    	User user = objectMapper.convertValue(param, User.class);
    	User checkedUser = userService.existCheck(user);
    	
    	// 이미 핸드폰번호 또는 이메일 인증 시 해당 API가 호출되므로 별도의 인증절차 없음
    	user.setPwd(encryptService.encrypt(user.getPwd() + checkedUser.getSalt()));
        userService.updateUser(user);
        
        return new ResponseEntity<>("ok", HttpStatus.OK); 
    }
    
    @GetMapping("/deleteUser")
    @Transactional(value="txManager")
    public ResponseEntity<?> deleteUser(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(false);
    	ObjectMapper objectMapper = new ObjectMapper();
    	User user = objectMapper.convertValue(param, User.class);
    	User checkedUser = userService.existCheck(user);
    	
    	// 회원탈퇴 시 세션만료
    	if(checkedUser != null) {
    		userService.deleteUser(checkedUser);
    		session.invalidate();
    		return new ResponseEntity<>("ok", HttpStatus.OK); 
    	}
    	else {
    		return new ResponseEntity<>("notFound", HttpStatus.SEE_OTHER);
    	}
    }
}
