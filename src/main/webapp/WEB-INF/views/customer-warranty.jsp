<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.Warranty" %>
<%@ page import="com.servicetrack.controller.WarrantyServlet.ProductWarranty" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>My Warranties - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>My Product Warranties</h2>

<%
    String error =
            (String) request.getAttribute("error");

    if (error != null) {
%>

<p style="color:red;">
    <%= error %>
</p>

<%
    }
%>

<hr>

<h3>Create Warranty</h3>

<%
    List<Product> products =
            (List<Product>) request.getAttribute("products");
%>

<%
    if (products == null || products.isEmpty()) {
%>

<p>
    No registered products found.
</p>

<%
} else {
%>

<form action="<%= request.getContextPath() %>/customer/warranty"
      method="post">

    <label for="productId">
        Product:
    </label>

    <select id="productId"
            name="productId"
            required>

        <option value="">
            Select Product
        </option>

        <%
            for (Product product : products) {
        %>

        <option value="<%= product.getProductId() %>">

            <%= product.getProductName() %>
            -
            <%= product.getSerialNumber() %>

        </option>

        <%
            }
        %>

    </select>

    <br><br>

    <label for="warrantyStart">
        Warranty Start Date:
    </label>

    <input type="date"
           id="warrantyStart"
           name="warrantyStart"
           required>

    <br><br>

    <label for="warrantyEnd">
        Warranty End Date:
    </label>

    <input type="date"
           id="warrantyEnd"
           name="warrantyEnd"
           required>

    <br><br>

    <button type="submit">
        Create Warranty
    </button>

</form>

<%
    }
%>

<hr>

<h3>Warranty Details</h3>

<%
    List<ProductWarranty> productWarranties =
            (List<ProductWarranty>)
                    request.getAttribute(
                            "productWarranties"
                    );
%>

<%
    if (productWarranties == null
            || productWarranties.isEmpty()) {
%>

<p>
    No products available.
</p>

<%
} else {
%>

<table border="1"
       cellpadding="8"
       cellspacing="0">

    <tr>

        <th>
            Product
        </th>

        <th>
            Brand
        </th>

        <th>
            Serial Number
        </th>

        <th>
            Purchase Date
        </th>

        <th>
            Warranty Start
        </th>

        <th>
            Warranty End
        </th>

        <th>
            Status
        </th>

    </tr>

    <%
        for (ProductWarranty item :
                productWarranties) {

            Product product =
                    item.getProduct();

            Warranty warranty =
                    item.getWarranty();
    %>

    <tr>

        <td>
            <%= product.getProductName() %>
        </td>

        <td>
            <%= product.getBrand() %>
        </td>

        <td>
            <%= product.getSerialNumber() %>
        </td>

        <td>
            <%= product.getPurchaseDate() %>
        </td>

        <%
            if (warranty == null) {
        %>

        <td colspan="2">
            Not Created
        </td>

        <td>
            Not Available
        </td>

        <%
        } else {
        %>

        <td>
            <%= warranty.getWarrantyStart() %>
        </td>

        <td>
            <%= warranty.getWarrantyEnd() %>
        </td>

        <td>
            <%= warranty.getWarrantyStatus() %>
        </td>

        <%
            }
        %>

    </tr>

    <%
        }
    %>

</table>

<%
    }
%>

<br>

<a href="<%= request.getContextPath() %>/customer/products">
    My Products
</a>

<br><br>

<a href="<%= request.getContextPath() %>/customer/">
    Customer Dashboard
</a>

<br><br>

<a href="<%= request.getContextPath() %>/logout">
    Logout
</a>

</body>

</html>