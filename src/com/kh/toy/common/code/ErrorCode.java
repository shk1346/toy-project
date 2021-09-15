package com.kh.toy.common.code;

public enum ErrorCode {
	
	DATABSE_ACCESS_ERROR("데이터베이스와 통신 중 에러가 발생하였습니다."),
	FAILED_VALIDATED_ERROR("데이터의 양식이 적합하지 않습니다."),
	MAIL_SEND_FAIL_ERROR("이메일 발송 중 에러가 발생하였습니다."),
	HTTP_CONNECT_ERROR("HTTP 통신 중 에러가 발생하였습니다."),
	AUTHENTICATION_FAILED_ERROR("유효하지 않은 인증입니다."),
	UNAUTHORIZED_PAGE("접근 권한이 없는 페이지 입니다."),
	REDIRECT_TO_LOGIN_PAGE_NO_MESSAGE("", "/member/login-form"), //msg는 없고 redirect될 url만 지정
	FAILED_FILE_UPLOAD_ERROR("파일 업로드에 실패했습니다.");
	
	//생성자 단계에서 초기화를 하지 않으면 인스턴스가 생김, (생성자 이후엔 초기화 단계가 없음) 그럼 null값이 들어가게됨
	public final String MESSAGE;
	public final String URL;
	
	//url을 지정하지 않으면 /index로 넘어가고
	ErrorCode(String msg){
		this.MESSAGE = msg;
		this.URL = "/index";
	}
	
	//url을 지정하면 지정한 url로 넘어감
	ErrorCode(String msg, String url){
		this.MESSAGE = msg;
		this.URL = url;
	}

}
