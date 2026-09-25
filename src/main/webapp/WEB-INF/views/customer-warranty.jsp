<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.Warranty" %>
<%@ page import="com.servicetrack.controller.WarrantyServlet.ProductWarranty" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>My Warranty - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>My Warranty</h2>

<p>
    View warranty information for your registered products.
</p>

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

<%
    List<ProductWarranty> productWarranties =
            (List<ProductWarranty>)
                    request.getAttribute("productWarranties");
%>

<%
    if (productWarranties == null
            || productWarranties.isEmpty()) {
%>

<p>
    You have not registered any products yet.
</p>

<p>
    <a href="<%= request.getContextPath() %>/customer/products">
        Register a Product
    </a>
</p>

<%
} else {
%>

<table border="1"
       cellpadding="8"
       cellspacing="0">

    <tr>

        <th>Product</th>
        <th>Brand</th>
        <th>Model</th>
        <th>Serial Number</th>
        <th>Purchase Date</th>
        <th>Warranty Start</th>
        <th>Warranty End</th>
        <th>Status</th>

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

        <%
            if (warranty == null) {
        %>

        <td>
            Not Available
        </td>

        <td>
            Not Available
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