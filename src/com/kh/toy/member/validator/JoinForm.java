package com.kh.toy.member.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.kh.toy.member.model.service.MemberService;

public class JoinForm {

	//validate를 전담할 클래스
	//회원가입할 때 form에 넣는 애들
	private String userId;
	private String password;
	private String email;
	private String tell;
	private HttpServletRequest request;
	private MemberService memberService = new MemberService();
	
	//validator를 통과할 수도, 못 할 수도 있음. 통과가 안 된 파라미터들을 기억할 예정
	//validation에 실패한 애들을 저장할 List 배열
	private Map<String, String>failValidation = new HashMap<String, String>();
	
	public JoinForm(ServletRequest request) {
		this.request = (HttpServletRequest)request;
		this.userId = request.getParameter("userId");
		this.password = request.getParameter("password");
		this.email = request.getParameter("email");
		this.tell = request.getParameter("tell");
	}
	
	public boolean test() {
		
		//pattern 객체 API에서 확인
		//하나라도 통과하지 못하는 게 있는 지 검증
		boolean isFailed = false;
				
		//사용자 아이디가 DB에 이미 존재하는 지 확인, userId를 넘긴 값이 null이 아니라면
		if(userId.equals("") || memberService.selectMemberById(userId) != null) { 
			failValidation.put("userId", userId);
			isFailed = true;
		}
				
		//비밀번호가 영어, 숫자, 특수문자 조합의 8자리 이상의 문자열인지 확인
		if(!Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}", password)) { 
			failValidation.put("password", password);
			isFailed = true;
		}
			
		//전화번호가 숫자로만 이루어져있는 지 확인
		if(!Pattern.matches("\\d{9,11}", tell)) {
			failValidation.put("tell", tell); 
			isFailed = true;
		}
				
		if(isFailed) { //사용자가 잘못된 값을 입력했을 경우,
			//사용자가 나쁜방법으로 잘못된 요청을 보냈을 경우, 잘못되었음을 알려주고 사용자가 입력했던 값들을 그대로 입력이 된 상태로 만들어줄거임
			//join-form이 가지고 있는 파라미터값들을 그대로 가지고 있어야하므로  join-form객체를 저장해줄거임
			request.getSession().setAttribute("joinValid", failValidation); //joinValid는 join-form.jsp에서 사용
			request.getSession().setAttribute("joinForm", this);
			return false;
		}else { //성공했다면 기존에 저장했던 값들을 모두 삭제
			request.getSession().removeAttribute("joinForm");
			request.getSession().removeAttribute("joinValid");
			return true;
		}
	}


	public String getUserId() {
		return userId;
	}


	public String getPassword() {
		return password;
	}


	public String getEmail() {
		return email;
	}


	public String getTell() {
		return tell;
	}
	
}
