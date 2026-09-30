<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.servicetrack.model.Customer" %>

<%
    String fullName = (String) session.getAttribute("fullName");
    String username = (String) session.getAttribute("username");
    Customer customer = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
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

<% if (error != null) { %>

<p style="color:red;">
    <%= error %>
</p>

<% } %>

<hr>

<h3>Customer Profile</h3>

<% if (customer == null) { %>

<p>
    Your customer profile has not been created yet.
</p>

<% } else { %>

<p>
    Address:
    <%= customer.getAddress() == null
            ? ""
            : customer.getAddress() %>
</p>

<p>
    City:
    <%= customer.getCity() == null
            ? ""
            : customer.getCity() %>
</p>

<p>
    State:
    <%= customer.getState() == null
            ? ""
            : customer.getState() %>
</p>

<p>
    Pincode:
    <%= customer.getPincode() == null
            ? ""
            : customer.getPincode() %>
</p>

<% } %>

<hr>

<h3>Customer Services</h3>

<p>
    <a href="<%= request.getContextPath() %>/customer/profile">
        Customer Profile
    </a>
</p>

<p>
    <a href="<%= request.getContextPath() %>/customer/products">
        My Products
    </a>
</p>

<p>
    <a href="<%= request.getContextPath() %>/customer/warranty">
        My Warranty
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