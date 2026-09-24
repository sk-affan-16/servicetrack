<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.servicetrack.model.Customer" %>

<%
    String fullName =
            (String) session.getAttribute("fullName");

    String username =
            (String) session.getAttribute("username");

    Customer customer =
            (Customer) request.getAttribute("customer");

    String error =
            (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Customer Dashboard - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Customer Dashboard</h2>

<p>
    Welcome, <%= fullName %>!
</p>

<p>
    Username: <%= username %>
</p>

<p>
    You are successfully authenticated as a CUSTOMER.
</p>

<p>
    Session authentication is working.
</p>

<%
    if (error != null) {
%>

<p style="color:red;">
    <%= error %>
</p>

<%
    }
%>

<hr>

<h3>Customer Profile</h3>

<p>
    <a href="<%= request.getContextPath() %>/customer/profile">
        Customer Profile
    </a>
</p>

<hr>

<h3>Customer Services</h3>

<p>
    <a href="<%= request.getContextPath() %>/customer/products">
        My Products
    </a>
</p>

<hr>

<p>
    <a href="<%= request.getContextPath() %>/logout">
        Logout
    </a>
</p>

</body>

</html>