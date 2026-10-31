<%@ page contentType="text/html;charset=UTF-8" %>

<header class="top-header">

    <div>
        <h2>ServiceTrack</h2>
        <p>Warranty & Repair Management System</p>
    </div>

    <div class="user-info">
        <span>
            <%= session.getAttribute("username") != null
                    ? session.getAttribute("username")
                    : "User" %>
        </span>

        <span class="role-label">
            <%= session.getAttribute("role") %>
        </span>

        <a href="<%= request.getContextPath() %>/logout"
           class="logout-link">Logout</a>
    </div>

</header>