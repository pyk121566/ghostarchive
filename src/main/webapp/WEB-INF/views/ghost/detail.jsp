<%@page import="org.springframework.web.util.HtmlUtils"%>
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
<htm lang="ko"></htm>
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
		<h3><%=HtmlUtils.htmlEscape(ghost.getTitle())%></h3>

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
				<td><%=ghost.getHit()%></td>
			</tr>

			<tr>
				<td>괴담 내용</td>
				<td><%=HtmlUtils.htmlEscape(ghost.getContent())%></td>
			</tr>
		</table>

		<!-- 댓글 목록 -->
		<h3 style="margin-top: 35px; margin-bottom: 20px;">💬 댓글</h3>

		<%
		if (request.getAttribute("errorMessage") != null) {
		%>

		<div class="alert alert-danger">
			<%=request.getAttribute("errorMessage")%>
		</div>

		<%
		}
		%>

		<%
		if (request.getAttribute("successMessage") != null) {
		%>

		<div class="alert alert-success">
			<%=request.getAttribute("successMessage")%>
		</div>

		<%
		}
		%>

		<!-- 비동기 댓글 목록  -->
		<div id ="asyncCommentList" data-ghost-id="<%=ghost.getGhostId()%>">
		
		</div>
		
		<div style="display: flex; gap: 8px; margin-top: 20px;">
			<a href="/ghosts" class="btn btn-default">목록으로</a> <a
				href="/ghosts/<%=ghost.getGhostId()%>/edit" class="btn btn-primary">수정하기</a>

			<form action="/ghosts/<%=ghost.getGhostId()%>/delete" method="POST"
				onsubmit="return confirm('정말 삭제하시겠습니까?');">

				<button type="submit" class="btn btn-danger">삭제하기</button>

			</form>
		</div>

		<!-- 댓글 작성 -->
		<div class="container" style="margin-top: 30px; margin-bottom: 40px">

			<h3>댓글 작성</h3>

			<form id="commentForm">
				<div class="form-group">
					<label>작성자</label> <input type="text" name="author"
						class="form-control" maxlength="50" required>
				</div>

				<div class="form-group">
					<label>댓글 내용</label>
					<textarea name="content" class="form-control" rows="3"
						maxlength="1000" required></textarea>
				</div>

				<button type="submit" class="btn btn-primary">댓글 등록</button>

			</form>
		</div>

<script src="<%=request.getContextPath()%>/resources/js/ghost-detail.js"></script>

</body>


</html>