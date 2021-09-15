<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
<%-- 사용자에게 알림을 주는 jsp --%>
	<script type="text/javascript">
	
	<%-- 안내창 출력 (msg라는 attribute가 존재하지 않는다면 alert를 띄운다, msg를 공백으로 처리하면 alter를 띄우지 않음 -> alter를 띄우지 않고 페이지만 이동시키고 싶을 때 ) --%>
	<c:if test="${not empty msg}"> 
		alert("${msg}");
	</c:if>
	
	<%-- 뒤로 가기 (back이라는 attribute가 존재한다면 뒤로가기) --%>
	<c:if test="${not empty back}">
		history.back();
	</c:if>
	
	<%-- 페이지 이동 (url이라는 attribute가 존재한다면 url로 돌아간다) --%>
	<c:if test="${not empty url}">
		location.href = '${url}'
	</c:if>
	
	</script>

</body>
</html>