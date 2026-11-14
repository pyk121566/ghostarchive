
const ghostId = document.getElementById("asyncCommentList").dataset.ghostId;

console.log("현재 괴담 번호:" ,ghostId);

//댓글 목록 비동기 조회
const getCommentList = async() => {
	
	try{
		const response = await fetch("/api/ghosts/"+ghostId+"/comments");
	
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
							"/api/ghosts/"+ghostId+"/comments/" + comment.commentId,
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
							"/api/ghosts/"+ghostId+"/comments/" + comment.commentId,{
								method:"DELETE"
								//삭제에는 번호만 필요하므로 body가 없다
							}
							);    
					
					const result = await response.json();
					
					if(!response.ok){
						throw new Error(result.message || "댓글 삭제 실패");
					}
					
					alert("댓글이 삭제되었습니다!");
					
					await getCommentList();
				
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
				"/api/ghosts/"+ghostId+"/comments",
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
			throw new Error(result.message || "댓글 등록 실패");
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
