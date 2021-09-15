package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class HandlableException extends RuntimeException {

	private static final long serialVersionUID = -4962612719278058727L;
	
	public ErrorCode error;
	
	//생성자의 매개변수로 ErrorCodeEnom을 받는 친구
	//log를 남가지 않고 사용자에게 알림메시지만 전달하는 용도의 생성자
	public HandlableException(ErrorCode error) {
		this.error = error; //생성자 초기화
		this.setStackTrace(new StackTraceElement[0]); //stackTrace를 비워주자.
	}
	
	//printStackTrace를 찍는 = log를 남기는 생성자
	public HandlableException(ErrorCode error, Exception e) {
		this.error = error; //생성자 초기화
		e.printStackTrace();
		this.setStackTrace(new StackTraceElement[0]); //stackTrace를 비워주자.
	}
	
}
