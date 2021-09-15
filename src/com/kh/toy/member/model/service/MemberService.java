package com.kh.toy.member.model.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.HandlableException;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;


public class MemberService {
	
	private MemberDao memberDao = new MemberDao();
	private JDBCTemplate template = JDBCTemplate.getInstance(); 
	
	

	public Member memberAuthenticate(String userId, String password) {
		
		Member member = null;
		Connection conn = template.getConnection(); //connection 생성
		
		
		try {
			member = memberDao.memberAuthenticate(userId, password, conn); 
			//connection을 dao에 넘겨주기
		}finally {
			template.close(conn); //connection 종료
		}
		
		return member;
	}
	

	public Member selectMemberById(String userId) {
		
		Member member = null;
		Connection conn = template.getConnection();
		
		try {
			member = memberDao.selectMemberById(userId, conn);
			
		} finally {
			template.close(conn);
		}
		
		return member;
	}
	
	public List<Member> selectMemberList() {
		
		List<Member> memberList = null;
		Connection conn = template.getConnection();
		
		try {
			memberList = memberDao.selectMemberList(conn);
		}finally {
			template.close(conn);
		}
		return memberList;
	}
	
	
	public int insertMember(Member member) {
		
		Connection conn = template.getConnection();
		int res = 0;
		
		try {
			//회원가입처리
			res = memberDao.insertMember(member, conn);
			//회원가입이후 자동 로그인처리
			//하려면 방금 가입한 회원의 정보를 다시 조회해야함
			// 다시 조회를 하기 위해선 memberdao에서 selectMemberById메서드 호출해야됨
			// memberDao.selectMemberById(member.getUserId());
			// Dao를 통해 사용자 정보를 받아서 해당 정보로 로그인 처리 진행
			
			Member m = memberDao.selectMemberById(member.getUserId(), conn);
			
			System.out.println(m.getUserId() + "의 로그인처리 로직이 동작했습니다.");
			
			template.commit(conn); //메서드가 성공적으로 수행되었다면 commit
			
		} catch (Exception e) { //모든 예외를 처리하도록 Exception로 바꿔주자
			template.rollback(conn); //메서드가 수행이 실패됐다면 rollback
			throw e;
		}finally {
			template.close(conn);   //connection 닫아주기
		}
		return res;
	}


	public int updateMember(Member member) {
		Connection conn = template.getConnection();
		int res = 0;
		
		try {
			res = memberDao.updateMember(member, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			e.printStackTrace(); //이걸 찍어놔야 예외사항을 발견하고 처리 가능함
		}finally {
			template.close(conn);
		}
		return res;
	}
	

	public int deleteMember(String userId) {
		Connection conn = template.getConnection();
		int res = 0;
		
		try {
			res = memberDao.deleteMember(userId, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		return res;
	}


	public void authenticateEmail(Member member, String persistToken) { //email인증
		
			HttpConnector conn = new HttpConnector();
			
			String queryString = conn.urlEncodedForm(RequestParams.bilder()
													.params("mail-template", "join-auth-email")
													.params("persistToken", persistToken)
													.params("userId", member.getUserId())
													.build());
			
			String mailTemplate = conn.get("http://localhost:7070/mail?"+queryString);
			MailSender sender = new MailSender();
			sender.sendEmail(member.getEmail(), "환영합니다." + member.getUserId()+"님", mailTemplate);
	}	
}