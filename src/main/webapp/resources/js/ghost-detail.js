const ghostId = document.getElementById("asyncCommentList").dataset.ghostId;

let currentSort = "latest";
let currentPage = 1;

const sortSelect = document.getElementById("commentSort");
//댓글 목록 조회 진행 상태
let isLoadingComments = false;	//let을 사용한 이유는 false 에서 true로, 다시 false로 값을 변경해야하기 때문에

console.log("현재 괴담 번호:", ghostId);

//REST API 응답 공통 처리
const handleResponse = async (response, errorMessage) => {

    const contentType = response.headers.get("content-type") || "";

    if (!contentType.includes("application/json")) {
        throw new Error(errorMessage);
    }

    const result = await response.json();

    if (!response.ok) {
        throw new Error(result.message || errorMessage);
    }

    return result;
};

//댓글 글자 수 표시 및 경고 색상 처리
const updateCharCount = (input, countElement) => {

    const length = input.value.length;

    countElement.textContent = length + "/1000자";

    if (length >= 900) {
        countElement.style.color = "red";
    } else {
        countElement.style.color = "";
    }
};

//댓글 수정 REST API 요청
const updateComment = async (commentId, newContent) => {

    const response = await fetch(
        "/api/ghosts/" + ghostId + "/comments/" + commentId, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            content: newContent
        })
    }
    );

    return await handleResponse(response, "댓글 수정 실패");
};

//댓글 삭제 REST API 요청
const deleteComment = async (commentId) => {

    const response = await fetch(
        "/api/ghosts/" + ghostId + "/comments/" + commentId, {
        method: "DELETE"
    }
    );

    return await handleResponse(response, "댓글 삭제 실패");
};

//댓글 등록 REST API 요청
const registComment = async (author, content) => {

    const response = await fetch(
        "/api/ghosts/" + ghostId + "/comments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            author: author,
            content: content
        })
    }
    );

    return await handleResponse(response, "댓글 등록 실패");
}


//댓글 하나의 HTML 요소 생성
const createCommentElement = (comment) => {
    const panel = document.createElement("div");	//createElement() : 새로운 HTML 요소를 JS로 생성한다는 뜻
    panel.className = "panel panel-default";

    const body = document.createElement("div");
    body.className = "panel-body";

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
    editBtn.addEventListener("click", () => {

        //수정창이 이미 열려 있으면 새로 생성하지 않음
        if (body.querySelector(".comment-edit-box")) {
            return;
        }
        panel.style.borderColor = "#337ab7";

        //수정 입력창 생성
        const editArea = document.createElement("textarea");
        editArea.className = "form-control";
        editArea.rows = 3;
        editArea.maxLength = 1000;
        editArea.value = comment.content;

        //댓글 글자 수 표시
        const charCount = document.createElement("small");

        charCount.style.display = "block";	//요소가 한 줄의 영역을 차지하도록 설정
        charCount.style.textAlign = "right";	//요소 내부의 글자를 오른쪽으로 정렬

        //수정창을 처음 열었을 때 글자 수와 색상 설정
        updateCharCount(editArea, charCount);

        //댓글 수정 글자 수 실시간 표시
        editArea.addEventListener("input", () => {

            updateCharCount(editArea, charCount);
        });

        //수정 완료 버튼 생성
        const saveBtn = document.createElement("button");
        saveBtn.type = "button";
        saveBtn.className = "btn btn-primary btn-xs";
        saveBtn.textContent = "수정 완료";

        const originalSaveBtnText = saveBtn.textContent;

        //취소 버튼 생성
        const cancelBtn = document.createElement("button");
        cancelBtn.type = "button";
        cancelBtn.className = "btn btn-default btn-xs";
        cancelBtn.textContent = "취소";

        //수정 입력창을 담을 영역
        const editBox = document.createElement("div");
        editBox.style.marginTop = "10px";

        //댓글 수정 안내 문구
        const editMessage = document.createElement("small");

        editMessage.textContent = "댓글 수정 중";
        editMessage.style.color = "#337ab7";
        editMessage.style.display = "block";
        editMessage.style.marginBottom = "5px";
        editMessage.style.fontWeight = "bold";
        editMessage.style.fontSize = "13px";

        editBox.append(editMessage, editArea, charCount, saveBtn, cancelBtn);

        editBox.className = "comment-edit-box";

        body.appendChild(editBox);

        //취소 버튼 클릭
        cancelBtn.addEventListener("click", () => {
            editBox.remove();
            panel.style.borderColor = "";
        });

        //수정 완료 버튼 클릭 이벤트
        saveBtn.addEventListener("click", async () => {

            const newContent = editArea.value.trim();

            if (newContent === "") {
                alert("댓글 내용을 입력해 주세요.");
                return;
            }

            //수정 요청 중 중복 클릭 방지
            if (saveBtn.disabled) {
                return;
            }

            saveBtn.disabled = true;
            saveBtn.textContent = "수정 중...";
            cancelBtn.disabled = true;
            editArea.disabled = true;

            try {
                //댓글 수정 REST API 호출
                await updateComment(comment.commentId, newContent);

                alert("댓글이 수정되었습니다!");

                //수정된 댓글 목록 다시 조회
                await getCommentList();

            } catch (error) {
                console.error("댓글 수정 중 오류 발생:", error.message, error);
                alert(error.message);

            } finally {
                saveBtn.disabled = false;
                saveBtn.textContent = originalSaveBtnText;
                cancelBtn.disabled = false;
                editArea.disabled = false;
            }

        });
    });
    //삭제 버튼 생성
    const deleteBtn = document.createElement("button");
    deleteBtn.type = "button";
    deleteBtn.className = "btn btn-danger btn-xs";
    deleteBtn.textContent = "삭제";

    const originalDeleteBtnText = deleteBtn.textContent;

    //삭제 버튼 클릭 이벤트
    deleteBtn.addEventListener("click", async () => {

        //이미 삭제 요청 중이면 실행하지 않음
        if (deleteBtn.disabled) {
            return;
        }

        //삭제 여부 확인
        const confirmed = confirm("정말 이 댓글을 삭제하시겠습니까?");

        if (!confirmed) {
            return;
        }

        //삭제 요청 시작
        deleteBtn.disabled = true;
        deleteBtn.textContent = "삭제 중...";

        try {
            //댓글 삭제 REST API 호출
            await deleteComment(comment.commentId);

            alert("댓글이 삭제되었습니다!");

            // 최신 댓글 목록 다시 조회
            await getCommentList();

        } catch (error) {
            console.error("댓글 삭제 중 오류 발생:", error.message, error);
            alert(error.message);

        } finally {
            deleteBtn.disabled = false;
            deleteBtn.textContent = originalDeleteBtnText;
        }

    });

    //버튼을 담을 영역 생성
    const buttonBox = document.createElement("div");
    buttonBox.style.marginTop = "10px";

    buttonBox.append(editBtn, deleteBtn);

    body.append(author, content, date, buttonBox);	//append()는 부모 요소 안에 넣는 작업임
    panel.appendChild(body);

    return panel;
};

//댓글 페이지 버튼 생성
const renderCommentPagination = (totalPages)=>{
	
	const pagination = document.getElementById("commentPagination");
	
	pagination.replaceChildren();
	
	for(let page =1; page<= totalPages;  page++){
		const button = document.createElement("button");
		
		button.type="button";
		button.textContent=page;
		
		pagination.appendChild(button);
	}
};


//댓글 목록 비동기 조회
const getCommentList = async () => {


    //이미 조회 중이면 중복 요청 방지
    if (isLoadingComments) {
        console.log("이미 댓글 조회 중이므로 중복 요청을 건너뜁니다.");
        return;
    }
    isLoadingComments = true;

    console.log("댓글 조회 요청 시작");

    //댓글 목록 영역 가져오기
    const commentList = document.getElementById("asyncCommentList");

    //로딩 메시지 표시
    commentList.textContent = "댓글을 불러오는 중입니다...";

    try {

        const response = await fetch(`/api/ghosts/${ghostId}/comments?sort=${currentSort}&page=${currentPage}&size=10`);

        const result = await handleResponse(response, "댓글 조회 실패");
		const comments = result.data.comments;

        console.log(result);

        const commentList = document.getElementById("asyncCommentList");

        commentList.replaceChildren(); //댓글 중복 방지를 위해 내부에 있는 자식요소를 모두 제거하는 메서드 사용

        //댓글이 없을 경우 안내 문구  표시
        if (comments.length === 0) {
            const message = document.createElement("p");
            message.textContent = "아직 등록된 댓글이 없습니다";
            commentList.appendChild(message);

            return;
        }

		//댓글 목록 출력
        for (const comment of comments) {	//댓글을 하나씩 꺼내는 반복문

            const panel = createCommentElement(comment);

            commentList.appendChild(panel);
        }

    } catch (error) {
        console.error("댓글 조회 중 오류 발생:", error.message, error);

        const commentList = document.getElementById("asyncCommentList");

        commentList.replaceChildren();

        //오류 안내 메시지
        const message = document.createElement("p");

        message.textContent = "댓글을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.";
        message.style.color = "red";

        commentList.appendChild(message);

        //다시 시도 버튼 생성
        const retryBtn = document.createElement("button");

        retryBtn.type = "button";
        retryBtn.className = "btn btn-default btn-sm";
        retryBtn.textContent = "다시 시도";

        //버튼 클릭 시 댓글 다시 조회
        retryBtn.addEventListener("click", async () => {
            retryBtn.disabled = true;

            await getCommentList();
        });

        commentList.appendChild(retryBtn);

    } finally {
        isLoadingComments = false;
    }
};

sortSelect.addEventListener("change", async () => {
    currentSort = sortSelect.value;
    await getCommentList();
});

getCommentList();


//댓글 등록 폼 가져오기
const commentForm = document.getElementById("commentForm");

//댓글 등록 버튼 가져오기
const submitBtn = commentForm.querySelector('button[type="submit"]');
const originalBtnlText = submitBtn.textContent;	//originalBtnText 에는 페이지가 처음 로드될 떄 버튼에 표시된 글자가 저장됨

//댓글 작성자 입력창 가져오기
const authorInput = commentForm.querySelector('[name="author"]');

//작성자 최대 50자 제한
authorInput.maxLength = 50;

//댓글 등록 입력창 가져오기
const contentInput = commentForm.querySelector('[name="content"]');

//최대 글자 수 설정
contentInput.maxLength = 1000;

//글자 수 표시 요소 생성
const registCharCount = document.createElement("small");

registCharCount.textContent = contentInput.value.length + "/1000자";

registCharCount.style.display = "block";
registCharCount.style.textAlign = "right";

//초기 글자 수 와 색상 설정
updateCharCount(contentInput, registCharCount);

//입력창 아래에 글자 수 표시
contentInput.insertAdjacentElement("afterend", registCharCount);

//댓글 등록 글자 수 실시간 표시
contentInput.addEventListener("input", () => {

    updateCharCount(contentInput, registCharCount);
});

//댓글 등록 폼 제출 이벤트
commentForm.addEventListener("submit", async (event) => {

    //기존 폼 제출 방식 막기
    event.preventDefault();

    //입력한 값 가져오기
    const author = commentForm.querySelector('[name="author"]').value.trim();
    const content = commentForm.querySelector('[name="content"]').value.trim();

    if (!author || !content) {
        alert("작성자와 댓글 내용을 입력해 주세요.");
        return;
    }

    //등록 요청 중 중복 클릭 방지
    if (submitBtn.disabled) {
        return;
    }

    submitBtn.textContent = "등록 중...";

    try {
        // 댓글 등록 REST API 호출
        await registComment(author, content);

        alert("댓글이 등록되었습니다!");

        //입력창 초기화
        commentForm.reset();

        //댓글 등록 후 글자 수 초기화
        updateCharCount(contentInput, registCharCount);

        //최신 댓글 목록 다시 조회
        await getCommentList();

    } catch (error) {
        console.error("댓글 등록 중 오류 발생:", error.message, error);
        alert(error.message);

    } finally {	//정상 처리 되든 오류가 발생하든 마지막에 실행되는 부분
        submitBtn.disabled = false;
        submitBtn.textContent = originalBtnlText;
    }
});



