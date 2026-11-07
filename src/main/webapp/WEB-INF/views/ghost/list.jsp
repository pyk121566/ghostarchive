<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.study.ghostarchive.dto.GhostDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%
 	//Controller 에서 전달한 괴담 목록 가져오기
 	List<GhostDTO> ghostList = (List<GhostDTO>) request.getAttribute("ghostList");
 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>GhostArchive - 괴담 게시판</title>
	    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Bootstrap JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<table class="table table-hover">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
			</tr>
		</thead>
		
		<tbody>
			<%
				//등록된 괴담이 없을 경우
				if(ghostList == null || ghostList.isEmpty()){
			%> 
			
			<tr>
				<td colspan="4" class="text-center">
					아직 등록된 괴담이 없습니다.
				</td>
			</tr>	
			
		<%
				}else {
					//괴담 목록 반복 출력
				for(int i = 0; i < ghostList.size(); i++){
            	GhostDTO ghost = ghostList.get(i);
				%>
		
		<tr>
		
			<td>
				<%=ghost.getGhostId() %>
			</td>
			<td>
				<a href="/ghosts/<%=ghost.getGhostId() %>">
					<%=ghost.getTitle() %>
				</a>
			</td> 
			<td><%=ghost.getAuthor() %></td>
			<td><%=sdf.format(ghost.getCreatedAt()) %></td>
		</tr>
		<%
					}
			}
		%>
		
		</tbody>	
		</table>
		<button type="button" class="btn btn-primary" onClick="location.href='/ghosts/new';">괴담등록</button>
		
	</div>

</body>
</html>