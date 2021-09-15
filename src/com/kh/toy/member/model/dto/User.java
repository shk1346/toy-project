package com.kh.toy.member.model.dto;

//Builder 패턴 학습용 클래스
public class User {

	//객체 생성 패턴 
	//객체의 속성을 초기화하고 생성하는 디자인 패턴 
	
	//1. 점층적 생성자 패턴
	// 생성자의 매개변수를 통해 객체의 속성을 초기화하고 생성하는 패턴
	// 단점 : 코드만 보고 생성자의 매개변수가 어떤 객체를 초기화하는 지 알기 어렵다. = 가독성이 좋지 않다.
	
	//2. 자바빈 패턴
	// getter/setter
	// 단점 : public 메서드인 setter를 통해 속성을 초기화하기 때문에, 변경 불가능한 객체(immutable)를 만들 수 없다.
	
	//3. 빌더패턴
	//Effective Java 책에 나온 Builder 패턴
	// 어떤 값이 들어가는 지 쉽게 파악할 수 있음
	
	
	//변수를 캡슐화
	private String userId;
	private String password;
	private String email;
	private String tell;
	
	//User class는 외부에서 생성할 수 없다. UserBuilder를 통해서만 생성 가능
	private User(UserBuilder builder) { //클래스 내부에서만 생성 가능한 생성자
		//사용자로부터 초기화될 속성값들을 가지고 있는 UserBuilder를 매개변수로 받아, builder를 통해 값을 초기화
		this.userId = builder.userId;
		this.password = builder.password;
		this.email = builder.email;
		this.tell = builder.tell;
	}
   
	public static UserBuilder builder() {
		return new UserBuilder();
	
	}
	//생성될 User 객체의 속성을 초기화하기 위한 값을 전달받아, 해당 값으로 속성을 초기화하고 User 인스턴스를 반환할 inner Class
	public static class UserBuilder{
		
		//외부에 선언된 변수들을 모두 가지고 있어야 됨
		private String userId;
		private String password;
		private String email;
		private String tell;
		
		public UserBuilder userId(String userId) {
			this.userId = userId;
			return this; //자기자신을 반환 (userId가 초기화된 UserBuilder를 반환
		}
		
		public UserBuilder password(String password) {
			this.password = password;
			return this; 
		}
		
		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		public UserBuilder tell(String tell) {
			this.tell = tell;
			return this; 
		}
		
		public User build() {
			return new User(this);
		}
		
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", email=" + email + ", tell=" + tell + "]";
	}
	
}
