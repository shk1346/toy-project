package com.kh.toy.common.exception;

public class PageNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -930316220873109881L;
	
	public PageNotFoundException() {
		//stackTrace를 비워주자.
		this.setStackTrace(new StackTraceElement[0]);
	}
	
		public PageNotFoundException(String message) {
				super(message);
	}
			
}
