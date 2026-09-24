<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    com.servicetrack.model.Customer customer =
            (com.servicetrack.model.Customer) request.getAttribute("customer");

    String error =
            (String) request.getAttribute("error");

    String fullName =
            (String) session.getAttribute("fullName");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Customer Profile - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Customer Profile</h2>

<p>
    Welcome,
    <strong><%= fullName != null ? fullName : "Customer" %></strong>
</p>

<% if (error != null) { %>
<p style="color: red;">
    <%= error %>
</p>
<% } %>

<form method="post"
      action="<%= request.getContextPath() %>/customer/profile">

    <label for="address">Address:</label>
    <br>
    <textarea id="address"
              name="address"
              rows="4"
              cols="40"
              maxlength="255"><%= customer != null && customer.getAddress() != null
            ? customer.getAddress()
            : "" %></textarea>

    <br><br>

    <label for="city">City:</label>
    <br>
    <input type="text"
           id="city"
           name="city"
           maxlength="100"
           value="<%= customer != null && customer.getCity() != null
                ? customer.getCity()
                : "" %>">

    <br><br>

    <label for="state">State:</label>
    <br>
    <input type="text"
           id="state"
           name="state"
           maxlength="100"
           value="<%= customer != null && customer.getState() != null
                ? customer.getState()
                : "" %>">

    <br><br>

    <label for="pincode">Pincode:</label>
    <br>
    <input type="text"
           id="pincode"
           name="pincode"
           maxlength="10"
           value="<%= customer != null && customer.getPincode() != null
                ? customer.getPincode()
                : "" %>">

    <br><br>

    <button type="submit">
        Save Profile
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/customer/">
    Back to Dashboard
</a>

<br><br>

<a href="<%= request.getContextPath() %>/logout">
    Logout
</a>

</body>
</html>