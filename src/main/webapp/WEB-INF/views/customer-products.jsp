<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>My Products - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>My Products</h2>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>

<p style="color:red;">
    <%= error %>
</p>

<%
    }
%>

<hr>

<h3>Register New Product</h3>

<form action="<%= request.getContextPath() %>/customer/products"
      method="post">

    <p>
        <label for="productName">
            Product Name:
        </label>

        <input type="text"
               id="productName"
               name="productName"
               required>
    </p>

    <p>
        <label for="brand">
            Brand:
        </label>

        <input type="text"
               id="brand"
               name="brand"
               required>
    </p>

    <p>
        <label for="modelNumber">
            Model Number:
        </label>

        <input type="text"
               id="modelNumber"
               name="modelNumber">
    </p>

    <p>
        <label for="serialNumber">
            Serial Number:
        </label>

        <input type="text"
               id="serialNumber"
               name="serialNumber"
               required>
    </p>

    <p>
        <label for="purchaseDate">
            Purchase Date:
        </label>

        <input type="date"
               id="purchaseDate"
               name="purchaseDate"
               required>
    </p>

    <p>
        <button type="submit">
            Register Product
        </button>
    </p>

</form>

<hr>

<h3>Registered Products</h3>

<%
    List<Product> products =
            (List<Product>) request.getAttribute("products");

    if (products == null || products.isEmpty()) {
%>

<p>
    No products registered yet.
</p>

<%
} else {
%>

<table border="1"
       cellpadding="8"
       cellspacing="0">

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

        <td>
            <%= product.getProductId() %>
        </td>

        <td>
            <%= product.getProductName() %>
        </td>

        <td>
            <%= product.getBrand() %>
        </td>

        <td>
            <%= product.getModelNumber() == null
                    ? "-"
                    : product.getModelNumber() %>
        </td>

        <td>
            <%= product.getSerialNumber() %>
        </td>

        <td>
            <%= product.getPurchaseDate() %>
        </td>

    </tr>

    <%
        }
    %>

</table>

<%
    }
%>

<hr>

<h3>Customer Services</h3>

<p>
    <a href="<%= request.getContextPath() %>/customer/warranty">
        My Warranty
    </a>
</p>

<p>
    <a href="<%= request.getContextPath() %>/customer/profile">
        Customer Profile
    </a>
</p>

<p>
    <a href="<%= request.getContextPath() %>/customer/">
        Back to Customer Dashboard
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