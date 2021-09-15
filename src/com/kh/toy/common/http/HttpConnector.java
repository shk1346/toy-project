package com.kh.toy.common.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;

public class HttpConnector {
	
	private static Gson gson = new Gson();

	//get을 두 종류로 나눔 
	//1. header가 필요하지 않은 get
	//외부에서 전달받아야 할 것은 매개변수로 (url = http://localhost:7070/mail?mail-template=join-auth-email)
	public String get(String url) {
		
		String responseBody = "";
		
		try {
			HttpURLConnection conn = getConnection(url, "GET"); 
			responseBody = getResponseBody(conn);
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
	}
	
	//2. header가 필요한 get
	public JsonElement getAsJson(String url, Map<String, String> headers) {
		
		String responseBody = "";
		JsonElement datas = null;
		
		try {
			HttpURLConnection conn = getConnection(url, "GET"); 
			setHeaders(headers, conn); //외부로부터 전달받은 http 헤더를 적용
			responseBody = getResponseBody(conn);
			datas = gson.fromJson(responseBody, JsonElement.class);
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return datas;
	}
	
	public String post(String url, Map<String, String>headers, String body) {
		
		String responseBody = "";
		
		try {
			//connection 생성
			HttpURLConnection conn = getConnection(url, "POST");
			setHeaders(headers, conn);
			setBody(body, conn);			
			responseBody = getResponseBody(conn);
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		
		return responseBody;
	}
	
	public String postAsJson(String url, Map<String, String> headers) {
		
		String responseBody = "";
		JsonElement datas = null;
		
		try {
			HttpURLConnection conn = getConnection(url, "GET"); 
			setHeaders(headers, conn); //외부로부터 전달받은 http 헤더를 적용
			responseBody = getResponseBody(conn);
			datas = gson.fromJson(responseBody, JsonElement.class);
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
	}
	
	
	 //연결을 수립할 때 url, method(ex."GET")이 필요함
	private HttpURLConnection getConnection(String url, String method) throws IOException { //IOException는 checkedException = 우리잘못X, 일괄적으로 예외처리하자
		
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod(method);
		
		//*** POST 방식일 경우 HttpURLConnection이 출력스트림 사용여부를 true로 지정을 해줘야 보낼 수 있음.
		if(method.equals("POST")) {
			conn.setDoOutput(true);
		}
		
		//connection에 문제가 생겨서 무한정대기가 되는 것을 방지하기 위해 걸리는 시간을 정해주자
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000); //5초가 지나면 연결을 아예 끊음
		
		return conn;
	}
	
	private String getResponseBody(HttpURLConnection conn) throws IOException {
		
		StringBuffer responseBody = new StringBuffer();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));) { //body로부터 데이터를 읽어오는 코드
			
			String line = null;
			while((line = br.readLine())!=null) { //br이 null일 때까지 가져옴
				responseBody.append(line);
			}
			//System.out.println(responseBody.toString());
		} 
		return responseBody.toString();
	}
	
	private void setHeaders(Map<String, String>headers, HttpURLConnection conn) {
		//Map을 사용하여 header를 받아오자
		
		for (String key : headers.keySet()) {
			conn.setRequestProperty(key, headers.get(key));
		}
	}
	
	private void setBody(String body, HttpURLConnection conn) throws IOException {
		
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));){
			bw.write(body);
			bw.flush();
		}
	}
	
	public String urlEncodedForm(RequestParams requestParams) {
		
		String res = "";
		Map<String, String> params = requestParams.getParams();
		
		try {
			for (String key : params.keySet()) {
				res += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
				}
			
				if(res.length() > 0) {
					res = res.substring(1); //1번 인덱스까지 잘라주고 그 다음 인덱스부터 res에 담아줌
				}
				
			} catch (UnsupportedEncodingException e) {
				// 오타만 안 내면 되므로 따로 예외처리는 하지 않을게
				e.printStackTrace();
			}				
		return res;	
		}
	
}
