<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String userRole = (String) session.getAttribute("role");
    String dashboardUrl = request.getContextPath();

    if ("CUSTOMER".equals(userRole)) {
        dashboardUrl += "/customer/";
    } else if ("TECHNICIAN".equals(userRole)) {
        dashboardUrl += "/technician/";
    }
%>

<aside class="sidebar">
    <div class="brand">
        <h2>ServiceTrack</h2>
        <p>Service Portal</p>
    </div>

    <div class="sidebar-menu">
        <p class="menu-heading">MAIN MENU</p>
        <a href="<%= dashboardUrl %>" class="menu-item">
            <span>▦</span> Dashboard
        </a>

        <% if ("CUSTOMER".equals(userRole)) { %>
            <p class="menu-heading">CUSTOMER</p>

            <a href="<%= request.getContextPath() %>/customer/products" class="menu-item">
                <span>▣</span> My Products
            </a>

            <a href="<%= request.getContextPath() %>/customer/warranty" class="menu-item">
                <span>✓</span> My Warranty
            </a>

            <a href="<%= request.getContextPath() %>/customer/service-request/new" class="menu-item">
                <span>＋</span> Raise Service Request
            </a>

            <a href="<%= request.getContextPath() %>/customer/bills" class="menu-item">
                <span>₹</span> My Bills
            </a>

            <a href="<%= request.getContextPath() %>/customer/profile" class="menu-item">
                <span>◉</span> My Profile
            </a>

        <% } else if ("TECHNICIAN".equals(userRole)) { %>
            <p class="menu-heading">TECHNICIAN</p>
        <% } else if ("ADMIN".equals(userRole)) { %>
            <p class="menu-heading">ADMINISTRATION</p>
        <% } %>
    </div>

    <div class="sidebar-bottom">
        <p>ServiceTrack © 2026</p>
    </div>
</aside>
