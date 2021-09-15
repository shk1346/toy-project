package com.kh.toy.common.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParams {
	//request 파라미터로 넘길 값들을 builder
	//request 파라미터는 사용자가 어떤 값을 넘기느냐에 따라 달라지기 때문에 Map 사용
	
	private Map<String, String> params = new LinkedHashMap<String, String>();
	
	private RequestParams(RequestParamsBuilder builder) { //생성자
		this.params = builder.params;
	}
	
	public static RequestParamsBuilder bilder() {
		return new RequestParamsBuilder();
	}
	
	public static class RequestParamsBuilder {
		
		private Map<String, String> params = new LinkedHashMap<String, String>();
		
		public RequestParamsBuilder params(String name, String value) {
			params.put(name, value);
			return this;
		}
		
		public RequestParams build() {
			return new RequestParams(this);
		}
	}

	//params에 대한 getter
	public Map<String, String> getParams() {
		return params;
	}
	
	

}
