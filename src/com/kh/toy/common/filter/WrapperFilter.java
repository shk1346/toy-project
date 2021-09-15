package com.kh.toy.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.wrapper.RequestWrapper;

//넘어온 request객체를 wrapper로 감싸서 다시 넘기는 역할
public class WrapperFilter implements Filter {

    public WrapperFilter() {
       
    }

	public void destroy() {
		
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest)request);
		chain.doFilter(requestWrapper, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
