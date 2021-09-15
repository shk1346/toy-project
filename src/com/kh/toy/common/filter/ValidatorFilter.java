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

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;
import com.kh.toy.member.validator.JoinForm;


public class ValidatorFilter implements Filter {

    
    public ValidatorFilter() {
        
    }
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//uri분리
		//System.out.println(httpRequest.getRequestURI());
		String[] uriArr = httpRequest.getRequestURI().split("/");
		//System.out.println(Arrays.toString(uriArr));
		
		if(uriArr.length != 0) {
			
			String redirectURI = null;
			
			switch (uriArr[1]) {
				case "member":
					redirectURI = memberValidation(httpRequest, httpResponse, uriArr);
					break;
				default:
					break;
			}
			
			if(redirectURI != null) {
				httpResponse.sendRedirect(redirectURI);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	

	private String memberValidation(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		String redirectURI = null;
		switch (uriArr[2]) {
			case "join": //회원가입할 때 사용자가 입력한 값을 검증
					JoinForm joinForm = new JoinForm(httpRequest);
					if(!joinForm.test()) { //통과 못 했다면
						redirectURI = "/member/join-form?err=1"; //err 파라미터
					}
				break;
	
			default:
				break;
		}
		
		return redirectURI;
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
