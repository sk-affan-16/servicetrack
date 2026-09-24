<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Products - ServiceTrack</title>
</head>

<body>

<h1>My Products</h1>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>
<p style="color:red;"><%= error %></p>
<%
    }
%>

<h2>Register New Product</h2>

<form action="<%= request.getContextPath() %>/customer/products"
      method="post">

    <label>Product Name:</label>
    <input type="text"
           name="productName"
           required>
    <br><br>

    <label>Brand:</label>
    <input type="text"
           name="brand"
           required>
    <br><br>

    <label>Model Number:</label>
    <input type="text"
           name="modelNumber">
    <br><br>

    <label>Serial Number:</label>
    <input type="text"
           name="serialNumber"
           required>
    <br><br>

    <label>Purchase Date:</label>
    <input type="date"
           name="purchaseDate"
           required>
    <br><br>

    <button type="submit">
        Register Product
    </button>

</form>

<hr>

<h2>Registered Products</h2>

<%
    List<Product> products =
            (List<Product>) request.getAttribute("products");

    if (products == null || products.isEmpty()) {
%>

<p>No products registered yet.</p>

<%
} else {
%>

<table border="1" cellpadding="8" cellspacing="0">

    <tr>
        <th>ID</th>
        <th>Product Name</th>
        <th>Brand</th>
        <th>Model Number</th>
        <th>Serial Number</th>
        <th>Purchase Date</th>
    </tr>

    <%
        for (Product product : products) {
    %>

    <tr>
        <td><%= product.getProductId() %></td>

        <td><%= product.getProductName() %></td>

        <td><%= product.getBrand() %></td>

        <td>
            <%= product.getModelNumber() == null
                    ? "-"
                    : product.getModelNumber() %>
        </td>

        <td><%= product.getSerialNumber() %></td>

        <td><%= product.getPurchaseDate() %></td>
    </tr>

    <%
        }
    %>

</table>

<%
    }
%>

<br>

<a href="<%= request.getContextPath() %>/customer/home">
    Back to Customer Home
</a>

</body>
</html>