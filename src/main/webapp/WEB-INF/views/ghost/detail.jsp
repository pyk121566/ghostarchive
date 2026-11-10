<%@page import="java.util.List"%>
<%@page import="com.study.ghostarchive.dto.GhostCommentDTO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.study.ghostarchive.dto.GhostDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
//Controller 애서 전달한 괴담 객체 한 개 가져오기
GhostDTO ghost = (GhostDTO) request.getAttribute("ghost");
//작성일 형식 변경
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//Controller에서 전달한 댓글 목록 가져오기
List<GhostCommentDTO> commentList = (List<GhostCommentDTO>) request.getAttribute("commentList");
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
				<td><%=ghost.getHit()%></td>
			</tr>

			<tr>
				<td>괴담 내용</td>
				<td><%=ghost.getContent()%></td>
			</tr>
		</table>

		<!-- 댓글 목록 -->
		<h3 style="margin-top: 35px; margin-bottom: 20px;">💬 댓글</h3>

		<%
		if (commentList == null || commentList.isEmpty()) {
		%>

		<p>아직 등록된 댓글이 없습니다.</p>

		<%
		} else {
		%>
		<%
		for (int i = 0; i < commentList.size(); i++) {
			GhostCommentDTO comment = commentList.get(i);
		%>

		<div class="panel panel-default">
			<div class="pannel-body">

				<strong><%=comment.getAuthor()%></strong>
				<p><%=comment.getContent()%></p>

				<small> <%=sdf.format(comment.getCreatedAt())%>
				</small>

				<form
					action="/ghosts/<%=ghost.getGhostId()%>/comments/<%=comment.getCommentId()%>/delete"
					method="post" style="display: inline-block; margin-left: 10px;"
					onsubmit="return confirm('이 댓글을 삭제하시겠습니까?');">

					<button type="button" class="btn btn-default btn-xs"
								onclick="document.getElementById('edit-<%=comment.getCommentId()%>').style.display='block'">수정</button>
								
					<button type="submit" class="btn btn-danger btn-xs">삭제</button>


				</form>
				
				<!-- 댓글 수정 폼 -->
				<form id="edit-<%=comment.getCommentId()%>" 
						action="/ghosts/<%=ghost.getGhostId()%>/comments/<%=comment.getCommentId()%>/edit"
							method="post"	style="display:none; margin-top:15px;">
							
					<div class="form-group">
						<textarea name="content" class="form-conrtrol" rows="3" maxlength="1000" 
							style="width: 100%; resize: vertical;" required>
							<%=comment.getContent() %>
						</textarea>
					</div>		
					
					<div style="display: flex; gap:8px; justify-content: flex-end;">
					<button type="submit" class="btn btn-primary btn-xs">수정 완료</button>
					
					<button type="button" class="btn btn-primary btn-xs"
								onclick="document.getElementById('edit-<%=comment.getCommentId()%>').style.display-'non">
								취소</button>
					</div>
					
				</form>

			</div>
		</div>
		<%
		}
		%>
		<%
		}
		%>
		<div style="display: flex; gap: 8px; margin-top: 20px;">
			<a href="/ghosts" class="btn btn-default">목록으로</a>

			<form action="/ghosts/<%=ghost.getGhostId()%>/delete" method="POST"
				onsubmit="return confirm('정말 삭제하시겠습니까?');">

				<button type="submit" class="btn btn-danger">삭제하기</button>

			</form>
		</div>

		<!-- 댓글 작성 -->
		<div class="container" style="margin-top: 30px; margin-bottom: 40px">

			<h3>댓글 작성</h3>

			<form action="/ghosts/<%=ghost.getGhostId()%>/comments" method="post">
				<div class="form-group">
					<label>작성자</label> <input type="text" name="author"
						class="form-control" maxlength="50" required>
				</div>

				<div class="form-group">
					<label>댓글 내용</label>
					<textarea name="content" class="form-control" rows="3"
						mexlength="1000" required></textarea>
				</div>

				<button type="submit" class="btn btn-primary">댓글 등록</button>

			</form>
		</div>
</body>
</html>