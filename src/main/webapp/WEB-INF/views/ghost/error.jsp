<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오류 안내 - GhostArchive</title>

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>

<div class="container" style="margin-top: 80px;">

    <div class="alert alert-danger text-center">

        <h3>요청을 처리할 수 없습니다.</h3>

        <p>
            <%=HtmlUtils.htmlEscape(
                String.valueOf(request.getAttribute("errorMessage") == null
                    ? "오류가 발생했습니다."
                    : request.getAttribute("errorMessage"))
            )%>
        </p>

    </div>

    <div class="text-center">
        <a href="/ghosts" class="btn btn-default">
            괴담 목록으로
        </a>
    </div>

</div>

</body>
</html>