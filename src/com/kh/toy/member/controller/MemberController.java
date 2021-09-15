package com.kh.toy.member.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.model.service.MemberService;

/**
 member로 시작하는 모든 요청을 여기에서 받아줌
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MemberService memberService = new MemberService();
       
    public MemberController() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String[] uriArr = request.getRequestURI().split("/");
		
		switch (uriArr[uriArr.length-1]) {
		case "login-form":
			loginForm(request, response);
			break;
			
		case "login":
			login(request, response);
			break;
			
		case "logout":
			logout(request, response);
			break;
			
		case "join-form":
			joinForm(request, response);
			break;
			
		case "join":
			join(request, response);
			break;
			
		case "join-impl":
			joinImpl(request, response);
			break;
			
		case "id-check":
			checkID(request, response);
			break;
			
		case "mypage":
			mypage(request, response);
			break;

			//원하는 url이 아닌 경우
		default: throw new PageNotFoundException(); //404를 던지는 대신 PageNotFoundException를 던지자
		}
	}


	private void mypage(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		request.getRequestDispatcher("/member/mypage").forward(request, response);
		
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession().removeAttribute("authentication"); //login할 때 담아뒀던 세션정보 를 삭제
		response.sendRedirect("/"); //index로 돌려준다.
		
	}

	private void checkID(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String userId = request.getParameter("userId");
		
		//id가 있는 지 없는 지 확인하는 메서드 selectMemberById
		Member member = memberService.selectMemberById(userId);
		
		if(member == null) { //겹치는 아이디가 없을 때
			response.getWriter().print("available");
		}else { //겹치는 아이디가 있을 때 
			response.getWriter().print("disable");
		}		
	}

	private void joinForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			request.getRequestDispatcher("/member/join-form").forward(request, response);
		
	}
	
	private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String tell = request.getParameter("tell");
		String email = request.getParameter("email");
		
		Member member = new Member();
		member.setUserId(userId);
		member.setPassword(password);
		member.setTell(tell);
		member.setEmail(email);
		
		//UUID.randomUUID() 랜덤한 unique ID를 생성해주는 자바의 클래스
		String persistToken = UUID.randomUUID().toString();
		
		//요청이 들어오면 persistUser라는 이름으로 회원정보를 session에 저장
		request.getSession().setAttribute("persistUser", member);
		
		//persist-toekn가 유효한 지 안 한 지를 판단
		request.getSession().setAttribute("persist-token", persistToken);
		
		memberService.authenticateEmail(member, persistToken);
		
		//사용자에게 알리기
		request.setAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
		request.setAttribute("url", "/member/login-form");
		request.getRequestDispatcher("/common/result").forward(request, response);
	}
	
	private void joinImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		memberService.insertMember((Member) session.getAttribute("persistUser"));
		
		//같은 persistUser값이 두 번 DB에 입력되지 않도록 사용자 정보와 인증을 만료시킴
		session.removeAttribute("persistUser");
		session.removeAttribute("persist-token");
		response.sendRedirect("/member/login-form");
	}

	private void loginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.getRequestDispatcher("/member/login").forward(request, response);
		
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		//index.html을 켜서 로그인이 되는 지 확인해보자
		/* System.out.println(member); */
		
		//1. 시스템에서 문제가 생겨서 (ex, DB가 뻗었다던가 ... 외부 API 서버가 죽었다던가 ...)
		//	예외가 발생하는 경우 => 예외처리를 service 단에서 처리
		Member member = memberService.memberAuthenticate(userId, password); //member 조회
		
		//2. 사용자가 잘못된 아이디나 비밀번호를 입력한 경우
		//	사용자에게 아이디나 비밀번호가 틀렸음을 알림, login-form으로 redirect
		if(member == null) {
			response.sendRedirect("/member/login-form?err=1"); //브라우저는 err에 1값을 넣어서 해당 주소로 넘어감
			return;
		}
		
		//login이 잘 되는 경우 member를 받아 index페이지로 넘긴다.
		request.getSession().setAttribute("authentication", member);
		response.sendRedirect("/index");		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
