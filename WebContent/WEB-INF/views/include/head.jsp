<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   <!-- tag library 추가 -->

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- getContextPath로 가져와야되는데
	el태그를 사용해서 request 객체에 접근하려면 pageContext객체로 접근할 수 있다.
	pageContext.(get)request.(get)contextPath 라는 뜻임 
--%>

<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${contextPath}/resources/css/all.css">
<script type="text/javascript" src="${contextPath}/resources/js/webUtil.js"></script>
