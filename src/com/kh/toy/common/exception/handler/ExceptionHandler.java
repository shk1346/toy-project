package com.kh.toy.common.exception.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.exception.HandlableException;

@WebServlet("/exception-handler")
public class ExceptionHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ExceptionHandler() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		System.out.println("DataAccessException");
		//servlet container는 HandlableException이 발생하면 요청을 HandlerException으로 재지정
		//이 때 ExceptionHandler 서비스 메서드로 넘겨주는 request의 속성(javax.servlet.error.exception)에 발생한 예외객체를 함께 넘겨준다.
		//request를 받을 때 예외객체를 함께 받아올 수 있다
		
		//예외를 꺼내자 = 사용자에게 알려주고 싶은 메시지, redirect시킬 url이 담겨있는 ErrorCode Enum이 담겨있음
		HandlableException e = (HandlableException) request.getAttribute("javax.servlet.error.exception");
		//e엔 enum도 함게 들어있다.
		//msg를 지정하지 않으면 공백으로 넘어감
		
		request.setAttribute("msg", e.error.MESSAGE);
		request.setAttribute("url", e.error.URL);
		request.getRequestDispatcher("/WEB-INF/views/common/result.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
