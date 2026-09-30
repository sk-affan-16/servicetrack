<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.servicetrack.model.User" %>
<%@ page import="com.servicetrack.model.Customer" %>

<%
    User user = (User) request.getAttribute("user");
    Customer customer = (Customer) request.getAttribute("customer");

    String error = (String) request.getAttribute("error");
    String updated = request.getParameter("updated");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile - ServiceTrack</title>
</head>

<body>

<h1>My Profile</h1>

<% if ("user".equals(updated)) { %>
    <p>Personal information updated successfully.</p>
<% } %>

<% if ("customer".equals(updated)) { %>
    <p>Customer information updated successfully.</p>
<% } %>

<% if (error != null) { %>
    <p><strong>Error:</strong> <%= error %></p>
<% } %>


<h2>Personal Information</h2>

<form method="post"
      action="<%= contextPath %>/customer/profile">

    <input type="hidden" name="formType" value="user">

    <label for="fullName">Full Name:</label><br>
    <input
            type="text"
            id="fullName"
            name="fullName"
            value="<%= user != null && user.getFullName() != null
                    ? user.getFullName() : "" %>"
            required>
    <br><br>

    <label for="email">Email:</label><br>
    <input
            type="email"
            id="email"
            name="email"
            value="<%= user != null && user.getEmail() != null
                    ? user.getEmail() : "" %>"
            required>
    <br><br>

    <label for="phone">Phone:</label><br>
    <input
            type="text"
            id="phone"
            name="phone"
            value="<%= user != null && user.getPhone() != null
                    ? user.getPhone() : "" %>">
    <br><br>

    <button type="submit">Update Personal Information</button>

</form>


<hr>


<h2>Customer Information</h2>

<form method="post"
      action="<%= contextPath %>/customer/profile">

    <input type="hidden" name="formType" value="customer">

    <label for="address">Address:</label><br>
    <textarea
            id="address"
            name="address"
            rows="3"
            cols="40"><%= customer != null && customer.getAddress() != null
                ? customer.getAddress() : "" %></textarea>
    <br><br>

    <label for="city">City:</label><br>
    <input
            type="text"
            id="city"
            name="city"
            value="<%= customer != null && customer.getCity() != null
                    ? customer.getCity() : "" %>">
    <br><br>

    <label for="state">State:</label><br>
    <input
            type="text"
            id="state"
            name="state"
            value="<%= customer != null && customer.getState() != null
                    ? customer.getState() : "" %>">
    <br><br>

    <label for="pincode">Pincode:</label><br>
    <input
            type="text"
            id="pincode"
            name="pincode"
            value="<%= customer != null && customer.getPincode() != null
                    ? customer.getPincode() : "" %>">
    <br><br>

    <button type="submit">Update Customer Information</button>

</form>


<hr>

<p>
    <a href="<%= contextPath %>/customer/">Back to Customer Home</a>
</p>

<p>
    <a href="<%= contextPath %>/logout">Logout</a>
</p>

</body>
</html>
