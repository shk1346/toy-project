package com.kh.toy.common.wrapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper{
	
	private HttpServletRequest request;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request; //생성자에서 초기화
	}
	
	//다운캐스팅해서 이 메서드를 사용하면 아래 세 줄의 코드를 반복해서 사용하지 않아도 되게끔
	public String[] getReauestURIArray() {
		String uri = this.getRequestURI();
		String[] uriArr = uri.split("/");
		return uriArr;
	}
	
	@Override
	public RequestDispatcher getRequestDispatcher(String uri) {
		return request.getRequestDispatcher("/WEB-INF/views/" + uri + ".jsp");
	}
	
	///WEB-INF/views/ 이외의 경로에 대한 처리를 위한 메서드
	public RequestDispatcher getRequestDispatcher(String uri, String prefix, String suffix) {
		return request.getRequestDispatcher(prefix + uri + suffix);
	}
}
