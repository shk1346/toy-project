package com.kh.toy.member.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.member.model.dto.Member;

public class MemberDao {
	
	JDBCTemplate template = JDBCTemplate.getInstance(); 

	public MemberDao() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Member memberAuthenticate(String userId, String password, Connection conn) {
		
		//1. connection생성은 service에서 받아오므로 관련 코드 지우기
		Member member = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		try {
			
			String query = "select * from member "
					+ " where user_id = ? and password = ?";
			pstm = conn.prepareStatement(query);
			pstm.setNString(1, userId);
			pstm.setNString(2, password);
			rset = pstm.executeQuery(); 
			
			if(rset.next()) {
				
				member = converRowToMember(rset);
			} 
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(rset, pstm); //2. close에서 conn 빼주기
			
			}
			
		   return member;
		}
		
	
	//사용자의 아이디를 넘겨받아 회원 조회
	public Member selectMemberById(String userId, Connection conn){
		
        Member member = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		String query = " select * from member where user_id = ?";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setNString(1, userId);
			
			rset = pstm.executeQuery();
			
			if(rset.next()) {
				member = converRowToMember(rset);
			}
			
		} catch (SQLException e) { //SQLException 이 발생했을 때 DataAccessException 처리
			throw new DataAccessException(e);
		} finally {
			template.close(rset, pstm);
		}
		
			return member;
	
	}
	
	
	// 전체 멤버 조회 메서드
	// 다중행이기 때문에 배열 사용!
	public List<Member> selectMemberList(Connection conn){
		
		List<Member> memberList = new ArrayList<Member>();
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		String query = "select * from member";
		
		try {
			
			pstm = conn.prepareStatement(query);
			rset = pstm.executeQuery();		
			
			while(rset.next()) { 
				
				memberList.add(converRowToMember(rset));
			 }
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally {
			template.close(rset, pstm);
		}
		
			return memberList;
		
	}
		
	// 회원 추가 메서드
	public int insertMember(Member member, Connection conn)  {
		int res = 0;
		PreparedStatement pstm = null;
		
		String query = "insert into member(user_id, password, email, tell) "
			      + " values (?,?,?,?) ";
		
		try {
						
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getUserId());
			pstm.setString(2, member.getPassword());
			pstm.setString(3, member.getEmail());
			pstm.setString(4, member.getTell());
				
			res = pstm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
		
		return res;
		
		}
		
		//회원 수정 메서드
    public int updateMember(Member member, Connection conn) {
		int res = 0;
    	PreparedStatement pstm = null; 
	    String query = "update member set password = ? "
			      + " where user_id = ?";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setNString(1, member.getPassword());
			pstm.setNString(2, member.getUserId());
			
			res = pstm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
		
		return res;
    }
	
	
	//회원 삭제 메서드
    public int deleteMember(String userId, Connection conn) {
    	
    	int res = 0;
    	PreparedStatement pstm = null;
    	
    	String query = "delete from member where user_id = ?";
    	
    	try {
            pstm = conn.prepareStatement(query);
			pstm.setNString(1, userId);
			
			res = pstm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
    	
    	return res;
     }
	
	
	private Member converRowToMember(ResultSet rset) throws SQLException {
		
		
		Member member = new Member();
		member.setUserId(rset.getString("user_id"));
		member.setPassword(rset.getString("password"));
		member.setGrade(rset.getString("grade"));
		member.setTell(rset.getString("tell"));
		member.setRegDate(rset.getDate("reg_date"));
		member.setEmail(rset.getString("email"));
		member.setRentableDate(rset.getDate("rentable_date"));
		member.setIsLeave(rset.getInt("is_leave"));
			
		return member;
		
	}

	
}


