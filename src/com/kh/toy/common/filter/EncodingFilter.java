package com.kh.toy.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

//servlet에 등록을 할 거기 때문에 WebFilter는 날려줌
public class EncodingFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EncodingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//모든 서블릿에서 겹치는 코드를 넣어주자
		request.setCharacterEncoding("utf-8");
		//그리고 다른 클래스에서 위의 코드를 모두 지워주고 돌려보면 한글이 깨지지 않고 출력됨을 확인
		
		//다음 filter에게 request, response 객체를 전달
		// 마지막 filter라면 Servlet 객체에게 request, response 객체를 전달
		chain.doFilter(request, response);
	}
	
	
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
