(()=>{ //즉시실행함수로 캡슐화, isIdCheck가 전역에 있으면 위험
	   
	 
	   let confirmId = '';
	   //사용자가 submit(가입) 버튼을 누르면 적절한 양식대로 입력했는 지 검증
	   
	   document.querySelector('#btnIdCheck').addEventListener('click', function(){
	   			
		let userId = document.querySelector('#userId').value;
	   			
	   			if(!userId){
	   				document.querySelector('#idCheck').innerHTML = '사용 불가능한 아이디입니다.';
	   				return;
	   			}
	   			
	   			fetch("/member/id-check?userId=" + userId)
	   			.then(response => {
					if(response.ok){
						return response.text()
					}else{
						throw new Error(response.status);
					}
				})
	   			.then(text => {
	   				if(text == 'available'){
	   					confirmId = userId;
	   					//idCheck라는 <span>에 innerHTML로 값을 넣어주자
	   					document.querySelector('#idCheck').innerHTML = '사용 가능한 아이디입니다.';
	   				}else{
	   					document.querySelector('#idCheck').innerHTML = '사용 불가능한 아이디입니다.';
	   				}
	   			})
				.catch(error => {
					document.querySelector('#idCheck').innerHTML = '응답에 실패했습니다. 상태코드 : ' + error;
				})
	   		});
	   
	   document.querySelector('#frm_join').addEventListener('submit',e =>{
		   
		   let userId = document.querySelector('#userId').value;
		   let password = document.querySelector('#password').value; //password값 가져오기
		   let tell = document.querySelector('#tell').value;
		   
		   let pwReg = /(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9])(?=.{8,})/; //비밀번호 체크
		   let tellReg = /^\d{9,11}$/; //\d=숫자, 9~11자리인지 확인
		   
		   if(!isIdCheck != userId){ //id check가 안 되었다면
			  
			   document.querySelector('#idCheck').innerHTML = '아이디 중복 검사를 하지 않았습니다.';
			   document.querySelector('#userId').focus();
			   e.preventDefault(); //기본이벤트 막기
		   }
		   
		   /*if(!pwReg.test(password)){ //password check가 안 되었다면
			  
			   document.querySelector('#pwCheck').innerHTML = '비밀번호는 숫자, 영문자, 특수문자 조합의 8글자 이상인 문자열입니다.';
			   e.preventDefault(); //기본이벤트 막기
		   }
		   
		   if(!tellReg.test(tell)){
			   
			   document.querySelector('#tellCheck').innerHTML = '전화번호는 9~11자리의 숫자입니다.';
			   e.preventDefault(); //기본이벤트 막기
		   }*/
		   
	   })
	   
	   
   })();