package com.kh.toy.common.code;

public enum Config {

	//DOMAIN("http://www.pclass.com")
	DOMAIN("http://localhost:7070"),
	
	//mail 인증에 필요한 ID, PW
	SMTP_AUTHENTICATION_ID("shk134666@gmail.com"),
	SMTP_AUTHENTICATION_PASSWORD("tngusdl79**"),
	COMPANY_EMAIL("shk134666@gmail.com"),
	//UPLOAD_PATH("C:\\CODE\\upload\\"); //운영서버용
	UPLOAD_PATH("C:\\CODE\\upload\\"); //개발서버용
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}
	
}
