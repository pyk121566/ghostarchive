<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.study.ghostarchive.dto.GhostDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
//Controller 애서 전달한 괴담 객체 한 개 가져오기
GhostDTO ghost = (GhostDTO) request.getAttribute("ghost");

//작성일 형식 변경
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<!DOCTYPE html>
<htm lang="ko">
<head>
<meta charset="UTF-8">
<title>GhostArchive - 괴담 상세</title>

<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">


</head>
<body>
	<div class="container">
		<h2>👻 괴담 아카이브 👻</h2>
		<h3><%=ghost.getTitle()%></h3>

		<table class="table table-bordered">
			<tr>
				<th>번호</th>
				<th><%=ghost.getGhostId()%></th>
			</tr>

			<tr>
				<th>작성자</th>
				<th><%=ghost.getAuthor()%></th>
			</tr>

			<tr>
				<td>작성일</td>
				<td><%=sdf.format(ghost.getCreatedAt())%></td>
			</tr>
			
			<tr>
				<th>조회수</th>
				<td><%=ghost.getHit() %></td>
			</tr>

			<tr>
				<td>괴담 내용</td>
				<td><%=ghost.getContent()%></td>
			</tr>
		</table>

		<button class="btn btn-default" onClick="location.href='/ghosts';">목록으로</button>
		
		<form action="/ghosts/<%=ghost.getGhostId()%>/delete" method="POST"
			onsubmit="return confirm('정말 삭제하시겠습니까?');">
			
			<button type="submit" class="btn btn-danger">삭제하기</button>
			
			</form>

	</div>


</body>
</html>