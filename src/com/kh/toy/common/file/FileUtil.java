package com.kh.toy.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class FileUtil {
	
	private static final int MAX_SIZE = 1024*1024*10; //10mb
	
	//multipart 요청 도착 
	// -> multipartParser를 사용해서 파일 업로드 처리 + 요청 파라미터 저장 + 파일과 관련된 FileDTO 생성
	//return해야되는 데이터의 종류가 두 개이므로 return type은 Map
	
	public MultiPartParams fileUpload(HttpServletRequest request){
		
		Map<String, List> res = new HashMap<String, List>();
		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		
		try {
			MultipartParser parser = new MultipartParser(request, MAX_SIZE);
			parser.setEncoding("UTF-8");
			Part part = null;
			
			
			while((part= parser.readNextPart())!=null) {
				
				if(part.isFile()) {
					
					FilePart filePart = (FilePart) part;
					
					if(filePart.getFileName() != null) {
						//input type = file 요소가 존재한다면, 사용자가 파일을 첨부하지 않더라도 빈 FilePart 객체가 넘어온다.
						// 단, 파일을 첨부하지 않았기 때문에 getFileName 메서드에서 Null이 반환된다.
						// 따라서 fileName이 null인 지 아닌 지에 따른 예외처리를 해줘야 함.
						
						FileDTO fileDTO = createFileDTO(filePart);
						filePart.writeTo(new File(getSavePath() + fileDTO.getRenameFileName())); //파일 저장
						fileDTOs.add(fileDTO); //fileDTOs List에 추가
					}			
					
				}else {
					ParamPart paramPart = (ParamPart) part;
					setParameterMap(paramPart, res);
				}
			}
			
			//files로 넘어오는 file들을  Map(res)에 넣어줄 것임
			res.put("com.kh.toy.files", fileDTOs);
			//System.out.println(res);	
			
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.FAILED_FILE_UPLOAD_ERROR,e);
		}
			
		return new MultiPartParams(res);
	}
	
	private String getSavePath() {
		
		//2. 저장경로를 웹어플리케이션 외부로 지정, 저장경로를 외부경로 + /연/월/일 형태로 지정
		//   외부경로는 config에 지정해둘 것임
		
		String subPath = getSubPath();
		String savePath = Config.UPLOAD_PATH.DESC + subPath; //저장경로
		
		File dir = new File(savePath);
		if(!dir.exists()) { //dir이 존재하지 않는다면 경로를 만들어주자
			dir.mkdirs();
		}
		
		return savePath;
	}
	
	private String getSubPath() {
		
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH)+1;
		int date = today.get(Calendar.DATE);
		return year + "\\" + month + "\\" + date + "\\";
	}
	
	private FileDTO createFileDTO(FilePart filePart) { //파일을 저장하는 메서드
		
		//1. 유니크한 파일명 생성
		String renameFileName = UUID.randomUUID().toString();
		//2. 파일 경로 생성
		String savePath = getSubPath();		
		//3. FileDTO 생성 (DB에 저장)
		FileDTO fileDTO = new FileDTO();
		fileDTO.setOriginFileName(filePart.getFileName());
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);
		
		return fileDTO;
	}
	
	private void setParameterMap(ParamPart paramPart, Map<String, List> res) throws UnsupportedEncodingException {
		
		//2. 해당 파라미터명으로 기존에 파라미터값이 존재할 경우
		if(res.containsKey(paramPart.getName())) {
			//기존 파라미터값을 가지고 있는 List를 불러서 그 뒤에 추가
			res.get(paramPart.getName()).add(paramPart.getStringValue());
			
		}else {//1. 해당 파라미터명으로 처음 파라미터값이 저장되는 경우
			List<String> param = new ArrayList<String>();
			param.add(paramPart.getStringValue());
			res.put(paramPart.getName(), param);
		}	
	}
}
