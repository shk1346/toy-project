package com.kh.toy.board.model.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.toy.board.model.dao.BoardDao;
import com.kh.toy.board.model.dto.Board;
import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.common.file.FileDTO;

public class BoardService {
	
	//transaction 관리는 Service에서 하므로 JDBCTemplate 생성
	private JDBCTemplate template = JDBCTemplate.getInstance();
	private BoardDao boardDao = new BoardDao();
	
	
	public void insertBoard(Board board, List<FileDTO> fileDTOs) {
		// 우리가 게시글을 입력하는 시점에 시퀀스를 통해 board idx가 생성됨
		// 그 게시글 번호를 가지고 와서 file테이블에 데이터를 넣으면 됨
		// Type Idx는 외래키로 동작함 (file이 board를 참조하고 있음 board에 insert하는 코드가 먼저 생성되어야 함)
		
		Connection conn = template.getConnection();
		
		try {
			boardDao.insertBoard(board, conn);
			
			//시퀀스는 세션이 만료되면 죽음 = connection을 닫으면
			//file에 데이터를 넣는 시점엔 세션이 죽지 않았음.
			//그러므로 우리는 board에 insert할 때 생성된 시퀀스값의 curvar를 사용할 수 있음
			for (FileDTO fileDTO : fileDTOs) {
				boardDao.insertFile(fileDTO, conn);
			}
			template.commit(conn);
		}catch(DataAccessException e) {
			template.rollback(conn);
			throw e;
		}finally {
			template.close(conn);
		}
		
	}


	public Map<String, Object> selectBoardDetail(String bdIdx) {

		Connection conn = template.getConnection();
		 Map<String, Object> res = new HashMap<String, Object>(); //반환해줄 Map 선언
		
		
		//select니까 catch는 필요없음
		try {
			//Dao에서 받아오기
			Board board = boardDao.selectBoardDetail(bdIdx, conn);
			List<FileDTO> files = boardDao.selectFileDTOs(bdIdx,conn);
			//List로 땡겨오기
			res.put("board", board);
			res.put("files", files);		
			
		} finally {
			template.close(conn);
		}	
		return res;
	}

}
