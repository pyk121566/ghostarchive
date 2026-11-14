<%@page import="org.springframework.web.util.HtmlUtils"%>
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
<div id = "oldCommentList" style="display:none;">
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
			<div class="panel-body">

				<strong><%=HtmlUtils.htmlEscape(comment.getAuthor())%></strong>
				<p><%=HtmlUtils.htmlEscape(comment.getContent())%></p>

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
					method="post" style="display: none; margin-top: 15px;">

					<div class="form-group">
						<textarea name="content" class="form-control" rows="3"
							maxlength="1000" style="width: 100%; resize: vertical;" required>
							<%=HtmlUtils.htmlEscape(comment.getContent())%>
						</textarea>
					</div>

					<div style="display: flex; gap: 8px; justify-content: flex-end;">
						<button type="submit" class="btn btn-primary btn-xs">수정
							완료</button>

						<button type="button" class="btn btn-primary btn-xs"
							onclick="document.getElementById('edit-<%=comment.getCommentId()%>').style.display-'none">
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
		</div> <%-- oldCommentList 끝 --%> 
		
		<!-- 비동기 댓글 목록  -->
		<div id ="asyncCommentList">
		
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
</body>

<script>
	//댓글 목록 비동기 조회
	const getCommentList = async() => {
		
		try{
			const response = await fetch("/api/ghosts/<%=ghost.getGhostId()%>/comments");
		
			if(!response.ok){
				throw new Error("댓글 조회 실패");
			}
			
			const result = await response.json();
			
			console.log(result);
			
			const commentList = document.getElementById("asyncCommentList");
			
			commentList.replaceChildren(); //댓글 중복 방지를 위해 내부에 있는 자식요소를 모두 제거하는 메서드 사용
			
			//댓글이 없을 경우 안내 문구  표시
			if (result.data.length === 0) {
				const message = document.createElement("p");
				message.textContent = "아직 등록된 댓글이 없습니다";
				commentList.appendChild(message);
				
				return;
			}
			
			for(const comment of result.data) {	//댓글을 하나씩 꺼내는 반복문
				
				const panel = document.createElement("div");	//createElement() : 새로운 HTML 요소를 JS로 생성한다는 뜻
				panel.className="panel panel-default";
				
				const body = document.createElement("div");
				body.className="panel-body";
				
				const author = document.createElement("strong");
				author.textContent = comment.author;
				
				const content = document.createElement("p");
				content.textContent = comment.content;
				
				//댓글 작성일 표시
				const date = document.createElement("small");
				date.textContent = new Date(comment.createdAt).toLocaleString("ko-Kr");
				
				//수정 버튼 생성
				const editBtn = document.createElement("button");
				editBtn.type = "button";
				editBtn.className = "btn btn-default btn-xs";
				editBtn.textContent = "수정";
				
				//수정 버튼 클릭 이벤트
				editBtn.addEventListener("click",()=>{
					
					//수정 입력창 생성
					const editArea = document.createElement("textarea");
					editArea.className="form-control";
					editArea.rows = 3;
					editArea.maxLength = 1000;
					editArea.value = comment.content;
					
					//수정 완료 버튼 생성
					const saveBtn = document.createElement("button");
					saveBtn.type = "button";
					saveBtn.className="btn btn-primary btn-xs";
					saveBtn.textContent = "수정 완료";
					
				//취소 버튼 생성
				const cancelBtn = document.createElement("button");
				cancelBtn.type = "button";
				cancelBtn.className="btn btn-default btn-xs";
				cancelBtn.textContent = "취소";
				
				//수정 입력창을 담을 영역
				const editBox = document.createElement("div");
				editBox.style.marginTop = "10px";
				
				editBox.append(editArea, saveBtn, cancelBtn);
				
				//기존에 열려 있던 수정 입력창이 있다면 제거
				const oldEditBox = body.querySelector(".comment-edit-box");
				
				if(oldEditBox){
					oldEditBox.remove();
				}
				
				editBox.className = "comment-edit-box";
				
				body.appendChild(editBox);
				
				//취소 버튼 클릭
				cancelBtn.addEventListener("click", ()=>{
						editBox.remove();
					});
				
				//수정 완료 버튼 클릭 이벤트
				saveBtn.addEventListener("click", async () => {
					
					const newContent = editArea.value.trim();
					
					if(newContent === "") {
						alert("댓글 내용을 입력해 주세요.");
						return;
					}
					
					try{
						const response = await fetch(
								"/api/ghosts/<%=ghost.getGhostId()%>/comments/" + comment.commentId,
								{
									method:"PUT",
									headers :{"Content-Type" : "application/json"},
									body : JSON.stringify({
										content:newContent
									})
								}
					);
					
						const result = await response.json();
						
						if(!response.ok) {
							throw new Error(result.message || "댓글 수정 실패");
							}
						
							alert("댓글이 수정되었습니다");
							
							await getCommentList();
						
					}catch (error){
						console.error(error);
						alert(error.message);
					}
				});
			 });
				
				//삭제 버튼 생성
				const deleteBtn = document.createElement("button");
				deleteBtn.type="button";
				deleteBtn.className="btn btn-danger btn-xs";
				deleteBtn.textContent="삭제";
				
				//삭제 버튼 클릭 이벤트
				deleteBtn.addEventListener("click", async ()=>{
					
					const confirmed = confirm("정말 이 댓글을 삭제하시겠습니까?");
					
					if(!confirmed){
						return;
					}
					
					try{
						const response = await fetch(
								"/api/ghosts/<%=ghost.getGhostId()%>/comments/" + comment.commentId,{
									method:"DELETE"
									//삭제에는 번호만 필요하므로 body가 없다
								}
								);    
						
						const result = await response.json();
						
						if(!response.ok){
							throw new Error(result.message || "댓글 삭제 실패");
						}
						
						alert("댓글이 삭제되었습니다!");
					
					}catch(error){
						console.error(error);
						alert(error.message);
					}
					
				});
				
				//버튼을 담을 영역 생성
				const buttonBox = document.createElement("div");
				buttonBox.style.marginTop = "10px";
				
				buttonBox.append(editBtn, deleteBtn);
				
				body.append(author, content,date,buttonBox);	//append()는 부모 요소 안에 넣는 작업임
				panel.appendChild(body);
				commentList.appendChild(panel);
			}
			
		}catch(error) {
			console.error(error);
		}
	} ;
	
	getCommentList();

	
	//댓글 등록 폼 가져오기
	const commentForm = document.getElementById("commentForm");
	
	//폼 제출 이벤트
	commentForm.addEventListener("submit", async (event)=> {
		
		//기존 폼 제출 방식 막기
		event.preventDefault();
		
		//입력한 값 가져오기
		const author = commentForm.querySelector('[name="author"]').value.trim();
		const content = commentForm.querySelector('[name="content"]').value.trim();
		
		if(!author || !content) {
			alert("작성자와 댓글 내용을 입력해 주세요.");
			return;
		}
		
		try{
			const response = await fetch(
					"/api/ghosts/<%=ghost.getGhostId()%>/comments",
					{
						method:"POST",
						headers:{"Content-Type" : "application/json"},
						body:JSON.stringify({
							author: author,
							content:content
						})
					}
					);
			
			const result = await response.json();
			
			if(!response.ok){
				throw new Error(reuslt.message || "댓글 등록 실패");
			}
			
			alert ("댓글이 등록되었습니다!");
			
			//입력창 비우기
			commentForm.reset();
			
			//댓글 목록 다시 조회
			await getCommentList();
			
		} catch(error){
			console.error(error);
			alert(error.message);
		}
	});
	
	
</script>

</html>