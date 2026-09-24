<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>Register - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Create Customer Account</h2>

<% if (request.getAttribute("error") != null) { %>

    <p style="color: red;">
        <%= request.getAttribute("error") %>
    </p>

<% } %>

<form method="post"
      action="<%= request.getContextPath() %>/register">

    <div>
        <label for="fullName">Full Name:</label>
        <input
                type="text"
                id="fullName"
                name="fullName"
                value="<%= request.getAttribute("fullName") != null
                        ? request.getAttribute("fullName")
                        : "" %>"
                required>
    </div>

    <br>

    <div>
        <label for="email">Email:</label>
        <input
                type="email"
                id="email"
                name="email"
                value="<%= request.getAttribute("email") != null
                        ? request.getAttribute("email")
                        : "" %>"
                required>
    </div>

    <br>

    <div>
        <label for="phone">Phone:</label>
        <input
                type="text"
                id="phone"
                name="phone"
                value="<%= request.getAttribute("phone") != null
                        ? request.getAttribute("phone")
                        : "" %>">
    </div>

    <br>

    <div>
        <label for="username">Username:</label>
        <input
                type="text"
                id="username"
                name="username"
                value="<%= request.getAttribute("username") != null
                        ? request.getAttribute("username")
                        : "" %>"
                required>
    </div>

    <br>

    <div>
        <label for="password">Password:</label>
        <input
                type="password"
                id="password"
                name="password"
                required>
    </div>

    <br>

    <button type="submit">Register</button>

</form>

<p>
    Already have an account?
    <a href="<%= request.getContextPath() %>/login">
        Login
    </a>
</p>

</body>
</html>