package com.kh.toy.admin.member.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberController
 */
@WebServlet("/admin/member/*")
public class AdminMemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MemberService memberService = new MemberService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminMemberController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String[] uriArr = request.getRequestURI().split("/");
		
		switch (uriArr[uriArr.length-1]) {
		case "member-list":
			memberList(request, response);
			break;
			
		case "leave":
			leave(request, response);
			break;

		default: throw new PageNotFoundException();
		}
	}

	private void leave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userId = request.getParameter("userId");
		HttpSession session = request.getSession();
		
		memberService.deleteMember((String) session.getAttribute(userId));
		session.removeAttribute(userId);
		
		
	}

	private void memberList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		List<Member> memberList = memberService.selectMemberList(); //회원정보를 List배열에 받기
		request.setAttribute("members", memberList); //member-list.jsp에 members로 저장된 값들 List에 받기
		request.getRequestDispatcher("/admin/member-list").forward(request, response); //요청 재지정
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
