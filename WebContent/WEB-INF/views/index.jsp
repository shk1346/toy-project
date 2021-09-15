<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">
	h1{
		text-align :center;
	}
</style>
</head>
<body>

<h1>PCLASS TOY PROJECT</h1>

<%-- 로그인이 되었을 때와 안 되었을 때를 구분 : "authentication"(회원정보)이 있는 지 없는 지에 따라 나눌 수 있음 --%>
<c:if test="${empty authentication}">
	<%-- 로그인이 안 되었을 때 --%>
	<h2><a href='/member/login-form'>login</a></h2>
	<h2><a href='/member/join-form'>회원가입</a></h2>
</c:if>

<c:if test="${not empty authentication}">
	<%-- 로그인이 되었을 때 --%>
	<h2><a href='/member/logout'>logout</a></h2>
	<h2><a href='/member/mypage'>마이페이지</a></h2>
	<h2><a href='/board/board-form'>게시글 쓰기</a></h2>
</c:if>

</body>
</html>