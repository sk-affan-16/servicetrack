<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | ServiceTrack</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body class="auth-body">

<div class="auth-wrapper">

    <div class="auth-brand-panel">
        <div class="auth-logo">ST</div>
        <h1>ServiceTrack</h1>
        <p>Warranty & Repair Service Management</p>

        <div class="auth-brand-message">
            <h2>Reliable service.<br>Complete peace of mind.</h2>
            <p>Manage your products, warranties and repair requests in one place.</p>
        </div>

        <div class="auth-brand-footer">
            ServiceTrack © 2026
        </div>
    </div>

    <div class="auth-form-panel">

        <div class="auth-form-container">

            <span class="auth-eyebrow">WELCOME BACK</span>
            <h2>Sign in to your account</h2>
            <p class="auth-subtitle">Enter your credentials to continue.</p>

            <% if (request.getParameter("registered") != null) { %>
            <div class="auth-success">
                Registration successful. Please login.
            </div>
            <% } %>

            <% if (request.getAttribute("error") != null) { %>
            <div class="auth-error">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <form method="post"
                  action="<%= request.getContextPath() %>/login">

                <div class="auth-field">
                    <label for="username">Username</label>
                    <input
                            type="text"
                            id="username"
                            name="username"
                            placeholder="Enter your username"
                            value="<%= request.getAttribute("username") != null
                                ? request.getAttribute("username") : "" %>"
                            autocomplete="username"
                            required>
                </div>

                <div class="auth-field">
                    <label for="password">Password</label>
                    <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Enter your password"
                            autocomplete="current-password"
                            required>
                </div>

                <button type="submit" class="auth-submit">
                    Sign In
                </button>

            </form>

            <p class="auth-switch">
                Don't have an account?
                <a href="<%= request.getContextPath() %>/register">
                    Create an account
                </a>
            </p>

        </div>

    </div>

</div>

</body>
</html>