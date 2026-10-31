<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | ServiceTrack</title>
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
            <h2>Your service journey starts here.</h2>
            <p>Create your customer account to register products, check warranties and track repairs.</p>
        </div>

        <div class="auth-brand-footer">
            ServiceTrack © 2026
        </div>
    </div>

    <div class="auth-form-panel">

        <div class="auth-form-container register-container">

            <span class="auth-eyebrow">GET STARTED</span>
            <h2>Create your account</h2>
            <p class="auth-subtitle">Fill in your details to join ServiceTrack.</p>

            <% if (request.getAttribute("error") != null) { %>
            <div class="auth-error">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <form method="post"
                  action="<%= request.getContextPath() %>/register">

                <div class="auth-field">
                    <label for="fullName">Full Name</label>
                    <input
                            type="text"
                            id="fullName"
                            name="fullName"
                            placeholder="Enter your full name"
                            value="<%= request.getAttribute("fullName") != null
                                ? request.getAttribute("fullName") : "" %>"
                            autocomplete="name"
                            required>
                </div>

                <div class="auth-field">
                    <label for="email">Email Address</label>
                    <input
                            type="email"
                            id="email"
                            name="email"
                            placeholder="Enter your email"
                            value="<%= request.getAttribute("email") != null
                                ? request.getAttribute("email") : "" %>"
                            autocomplete="email"
                            required>
                </div>

                <div class="auth-field">
                    <label for="phone">Phone Number</label>
                    <input
                            type="text"
                            id="phone"
                            name="phone"
                            placeholder="Enter your phone number"
                            value="<%= request.getAttribute("phone") != null
                                ? request.getAttribute("phone") : "" %>"
                            autocomplete="tel">
                </div>

                <div class="auth-field">
                    <label for="username">Username</label>
                    <input
                            type="text"
                            id="username"
                            name="username"
                            placeholder="Choose a username"
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
                            placeholder="Create a password"
                            autocomplete="new-password"
                            required>
                </div>

                <button type="submit" class="auth-submit">
                    Create Account
                </button>

            </form>

            <p class="auth-switch">
                Already have an account?
                <a href="<%= request.getContextPath() %>/login">
                    Sign in
                </a>
            </p>

        </div>

    </div>

</div>

</body>
</html>