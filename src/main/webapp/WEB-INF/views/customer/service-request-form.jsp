<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Raise Service Request</title>
</head>

<body>

<h2>Raise Service Request</h2>

<%-- Display validation/error message --%>
<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>
    <p style="color: red;">
        <%= error %>
    </p>
<%
    }
%>

<form method="post"
      action="<%= request.getContextPath() %>/customer/service-request/create">

    <div>
        <label for="productId">Product ID:</label>
        <input type="number"
               id="productId"
               name="productId"
               required>
    </div>

    <br>

    <div>
        <label for="complaintDescription">
            Complaint Description:
        </label>
        <br>

        <textarea id="complaintDescription"
                  name="complaintDescription"
                  rows="6"
                  cols="50"
                  maxlength="1000"
                  required></textarea>
    </div>

    <br>

    <button type="submit">
        Raise Service Request
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/customer">
    Back to Customer Dashboard
</a>

</body>
</html>