<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Login - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Login</h2>

<% if (request.getParameter("registered") != null) { %>

    <p style="color: green;">
        Registration successful. Please login.
    </p>

<% } %>

<% if (request.getAttribute("error") != null) { %>

    <p style="color: red;">
        <%= request.getAttribute("error") %>
    </p>

<% } %>

<form method="post"
      action="<%= request.getContextPath() %>/login">

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

    <button type="submit">Login</button>

</form>

<p>
    Don't have an account?
    <a href="<%= request.getContextPath() %>/register">
        Register
    </a>
</p>

</body>

</html>