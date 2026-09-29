<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.User" %>

<%
    User user = (User) request.getAttribute("user");

    String fullName = "";
    String email = "";
    String phone = "";

    if (user != null) {
        fullName = user.getFullName() != null
                ? user.getFullName() : "";

        email = user.getEmail() != null
                ? user.getEmail() : "";

        phone = user.getPhone() != null
                ? user.getPhone() : "";
    } else {
        Object fullNameAttr = request.getAttribute("fullName");
        Object emailAttr = request.getAttribute("email");
        Object phoneAttr = request.getAttribute("phone");

        if (fullNameAttr != null) {
            fullName = fullNameAttr.toString();
        }

        if (emailAttr != null) {
            email = emailAttr.toString();
        }

        if (phoneAttr != null) {
            phone = phoneAttr.toString();
        }
    }

    String error = (String) request.getAttribute("error");

    String updated = request.getParameter("updated");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile - ServiceTrack</title>
</head>

<body>

<h1>My Profile</h1>

<%
    if ("true".equals(updated)) {
%>
    <p style="color: green;">
        Profile updated successfully.
    </p>
<%
    }
%>

<%
    if (error != null) {
%>
    <p style="color: red;">
        <%= error %>
    </p>
<%
    }
%>

<form method="post"
      action="<%= request.getContextPath() %>/customer/profile">

    <label for="fullName">Full Name:</label>
    <br>

    <input type="text"
           id="fullName"
           name="fullName"
           value="<%= fullName %>"
           maxlength="100"
           required>

    <br><br>

    <label for="email">Email:</label>
    <br>

    <input type="email"
           id="email"
           name="email"
           value="<%= email %>"
           maxlength="150"
           required>

    <br><br>

    <label for="phone">Phone:</label>
    <br>

    <input type="text"
           id="phone"
           name="phone"
           value="<%= phone %>"
           maxlength="20">

    <br><br>

    <button type="submit">
        Update Profile
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/customer/">
    Back to Customer Dashboard
</a>

<br><br>

<a href="<%= request.getContextPath() %>/logout">
    Logout
</a>

</body>
</html>