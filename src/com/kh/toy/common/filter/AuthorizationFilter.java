package com.kh.toy.common.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.code.member.MemberGrade;
import com.kh.toy.common.exception.HandlableException;
import com.kh.toy.member.model.dto.Member;

public class AuthorizationFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthorizationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//uri분리
		System.out.println(httpRequest.getRequestURI());
		String[] uriArr = httpRequest.getRequestURI().split("/");
		//System.out.println(Arrays.toString(uriArr));
		
		if(uriArr.length != 0) {
			
			switch (uriArr[1]) {
				case "member":
					memberAuthorize(httpRequest, httpResponse, uriArr);
					break;
					
				case "admin":
					adminAuthorize(httpRequest, httpResponse, uriArr);
					break;
					
				case "board":
					boardAuthorize(httpRequest, httpResponse, uriArr);
					break;
					
				default:
					break;
			}
		}	
		chain.doFilter(request, response);
	}
	
	private void boardAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member) session.getAttribute("authentication");
		
		switch (uriArr[2]) {
		case "board-form":
			if(member == null) { //비회원이면 board-form으로 넘어갈 수 없다.
				throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
			}
			break;
			
		case "upload":
			if(member == null) { //비회원이면upload으로 넘어갈 수 없다.
				throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
			}
			break;

		default:
			break;
		}
		
	}

	private void adminAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {

		Member member = (Member) httpRequest.getSession().getAttribute("authentication");
		
		//넘어온 인증정보가 관리자인지 사용자인지 판단
		if(member == null || MemberGrade.valueOf(member.getGrade()).ROLE == "user") { 
				throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
		}
		
		MemberGrade adminGrade = MemberGrade.valueOf(member.getGrade());
		if(adminGrade.DESC.equals("super")) return; //super이면 모든 페이지에 접근이 가능 
		
		switch (uriArr[2]) {
		
			case "member": //회원과 관련된 관리를 수행하는 admin의 등급은 AD01
				
				if(!adminGrade.DESC.equals("member")) {
					//super도, memeber도 아닌 사람이 member로 넘어온다면
					throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
				}
				
				break;
				
			case "board":  //게시판과 관련된 관리를 수행하는 admin의 등급은 AD02 ... 이렇게 세부적으로 접근 조건을 나눌 수도 있고
				
				if(!adminGrade.ROLE.equals("board")) {
					//super도, memeber도 아닌 사람이 member로 넘어온다면
					throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
				}
				break;
	
			default:
				break;
	}
	}

	private void memberAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) throws ServletException, IOException {
		
		//admin들의 공통 조건 : 일반 회원은 접근할 수 없다. .. 이렇게 공통된 조건을 함께 처리할 수도 있다.
		
		
		
		//권한필터에서는 validator로 처리하지 않을 것임
		//예쁘게 처리 안 해도 됨 (그냥 예외를 던질 예정)
		switch (uriArr[2]) {
		
		case "join-impl": 
			String serverToken = (String) httpRequest.getSession().getAttribute("persist-token");
			String clientToken = httpRequest.getParameter("persist-token");
			
			if(serverToken == null || !serverToken.equals(clientToken)) { //serverToken이 null이거나 serverToken이 clientToken와 같지 않을 때
				throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
			}
			
			break;
			
		case "mypage": 
			
			if(httpRequest.getSession().getAttribute("authentication") == null) { //권한이 맞지 않다면
				//login page로 넘어가도록
				throw new HandlableException(ErrorCode.REDIRECT_TO_LOGIN_PAGE_NO_MESSAGE);
			}
			break;
			
		default:
			break;
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
