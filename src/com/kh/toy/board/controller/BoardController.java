package com.kh.toy.board.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.board.model.dto.Board;
import com.kh.toy.board.model.service.BoardService;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.file.FileDTO;
import com.kh.toy.common.file.FileUtil;
import com.kh.toy.common.file.MultiPartParams;
import com.kh.toy.member.model.dto.Member;
import com.oreilly.servlet.MultipartRequest;

//controller는 Servlet으로 만들기
@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BoardService boardService = new BoardService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[] uriArr = request.getRequestURI().split("/");
		
		switch (uriArr[uriArr.length-1]) {
		case "board-form":
			boardForm(request, response);
			break;
			
		case "upload":
			upload(request, response);
			break;
			
		case "board-detail":
			boardDetail(request, response);
			break;

		default: throw new PageNotFoundException();
		}
	}

	private void boardDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//게시글 상세 데이터를 조회할것이므로 각 게시글의 식별자를 받아와야함
		String bdIdx = request.getParameter("bdIdx");
		
		//하나의 매개변수로 두 가지의 값을 받아오기위해 Map 사용
		Map<String, Object> datas = boardService.selectBoardDetail(bdIdx);
		
		request.setAttribute("datas", datas);
		//datas엔 두 개의 값이 담겨있을 것.
		
		//bdIdx를 매개변수로 넘겨주고 나면 주소를 넘김
		request.getRequestDispatcher("/board/board-detail").forward(request, response);
		
	}

	private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		FileUtil util = new FileUtil();
		MultiPartParams params = util.fileUpload(request);
		
		Member member = (Member) request.getSession().getAttribute("authentication");
		
		Board board = new Board();
		board.setUserId(member.getUserId()); //로그인한 사용자만(=id가 있는 사람들만) 게시판에 접근 가능하도록
		board.setTitle(params.getParameter("title"));
		board.setContent(params.getParameter("content"));
		// id, title, content만 db에서 받아올 속성들이고 나머지는 db가 알아서 생성할 녀석들임
		
		List<FileDTO> fileDTOs = params.getFilesInfo(); //여기까지 service로 넘겨야 할 데이터는 다 받아온 것
		boardService.insertBoard(board, fileDTOs); //boardDTO와 fileDTOs를 넘기는 메서드
		
		//원래 게시글 작성이 완료되면 게시글 목록으로 리다이렉트시키는데 우린 없으므로
		response.sendRedirect("/");	
		
	}

	
	private void boardForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//board 작성이 가능한 등급은? 사용자 식별이 된 사람들(회원인 사람)
		request.getRequestDispatcher("/board/board-form").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
