<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="com.study.ghostarchive.dto.GhostDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// Controller 에서 전달한 기존 괴담 정보
GhostDTO ghost = (GhostDTO) request.getAttribute("ghost");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<tittle>GhostArchive - 괴담 수정
</title>

<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
	<h2>👻 괴담 아카이브 👻</h2>
	<h3>괴담 수정</h3>

	<form action="/ghosts/<%=ghost.getGhostId()%>/edit" method="POST">

		<!--제목 -->
		<div class="form-group">
			<label for="title">제목</label> <input type="text" class="form-control"
				id="title" name="title" value="<%=HtmlUtils.htmlEscape(ghost.getTitle())%>" required>
		</div>

		<!-- 작성자-->
		<div class="form-group">
			<label for="author">작성자</label> <input type="text"
				class="form-control" id="author" name="author"
				value="<%=HtmlUtils.htmlEscape(ghost.getAuthor())%>" required>
		</div>

		<!-- 내용 -->
		<div class="form-group">
			<label for="author">괴담 내용</label>
			<textarea type="text" class="form-control" id="contentr"
				name="content" rows="10" required><%=HtmlUtils.htmlEscape(ghost.getContent())%></textarea>
				
		</div>
		
		<button type ="submit" class="btn btn-primary">수정 완료</button>
		<button type ="button" class="btn btn-default" onclick="location.href-'/ghosts/<%=ghost.getGhostId() %> ';">취소</button>
	</form>

</body>
</html>