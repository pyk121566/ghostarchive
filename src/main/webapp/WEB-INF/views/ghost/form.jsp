<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GhostArchive - 괴담 등록</title>
   <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<h2>👻 괴담 아카이브 👻</h2>
		<h3>괴담 등록</h3>
	
		<form action="/ghosts" method="POST">
		
			<!--제목 -->
			<div class="form-group">
				<label for="title">제목</label>
				<input type="text" class="form-control" id="title" name="title" maxlength="200" required>
			</div>\
			
			<!--작성자  -->
			<div class="form-group">
				<label for="author">작성자</label>
				<input type="text" class="form-control" id="author" name="author" required>
			</div>
			
			<!--내용 -->
			<div class="form-group">
				<label for="content">괴담 내용</label>
				<textarea class="form-control" id="content" name="content" rows="10" required></textarea>
			</div>
			
			<button type="submit" class="btn btn-primary">등록하기</button>
			<button type="button" class="btn btn-default" onClick="location.href='/ghost';">취소</button>
			
		</form>
	</div>

</body>
</html>