package com.kh.toy.common.code.member;

//enum(enumerated type) : 열거형
// 서로 연관된 상수들의 집합
// 서로 연관된 상수들을 하나의 묶음으로 다루기 위한 enum만의 문법적 형식과 메서드를 제공해준다.

public enum MemberGrade {
	
	//변하지 않는 이 정보들을 하나의 변수로 다룰 수 있게끔
	//서로 연관된 상수들을 하나의 묶음으로 처리 = enum
	
	//회원등급코드가 ME00이면 등급명은 '일반'이고, 연장가능횟수는 1회
	
	//내부적으로 enum은 class이다.
	//ME00("일반", 1) → public static final MemberGrade ME00 = new MemberGrade("일반", 1);
	// ME00이라는 코드는 내부적으로 컴파일러가 돌릴 땐 위와같이 돌아감
	// ME00는 인스턴스이므로 아래에 생성한 desc, extendableCnt 메서드를 사용할 수 있음
	// 따라서 desc를 호출하면 "일반", extendableCnt을 호출하면 1이 반환됨
	// enum은 public이기 때문에 어디에서나 접근이 가능하고,
	// static이기 때문에 언제나 접근이 가능한 인스턴스에
	// 등급명과 연장가능횟수를 저장해두고, getter를 통해서 반환받아 사용한다.
	ME00("일반", "user", 1),
	ME01("성실", "user", 2),
	ME03("우수", "user", 3),
	ME04("vip", "user", 4),
	
	AD00("super", "admin"),
	AD01("member", "admin"),
	AD02("book", "admin");
	
	
	//필드변수
	public final String DESC;
	public final String ROLE;
	public final int EXTENDABLE_CNT;
	
	//생성자에서는 필드변수를 초기화하기위해 매개변수로 받아준다.
	private MemberGrade(String desc, String role) {
		this.DESC = desc;
		this.ROLE = role;
		this.EXTENDABLE_CNT = 9999;
	}
	
	private MemberGrade(String desc, String role, int extendableCnt) {
		this.DESC = desc;
		this.ROLE = role;
		this.EXTENDABLE_CNT = extendableCnt;
	}
	
	
	
	//enum은 클래스이므로 원하는 메서드를 만들 수 있다
	public String DESC() { //desc를 반환하는 메서드
	 return DESC;
	  
	  }
	
	  public int EXTENDABLE_CNT() { //extendableCnt를 반환하는 메서드 return
		  return EXTENDABLE_CNT; 
	}
	

}
