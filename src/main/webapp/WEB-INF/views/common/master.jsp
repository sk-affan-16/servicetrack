<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String role = (String) session.getAttribute("role");

    if (role == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String contentPage = (String) request.getAttribute("contentPage");

    if (contentPage == null || !contentPage.startsWith("/WEB-INF/views/")) {
        response.sendError(500, "Page content not configured.");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ServiceTrack</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="app-layout">

    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <div class="main-area">

        <jsp:include page="/WEB-INF/views/common/header.jsp"/>

        <main class="page-content">

            <jsp:include page="<%= contentPage %>"/>

        </main>

        <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    </div>

</div>

</body>
</html>