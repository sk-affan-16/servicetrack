<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Raise Service Request</title>
</head>

<body>

<h2>Raise Service Request</h2>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }

    List<Product> products =
            (List<Product>) request.getAttribute("products");
%>

<%
    if (products == null || products.isEmpty()) {
%>

    <p>You have no registered products.</p>

    <a href="<%= request.getContextPath() %>/customer">
        Back to Customer Dashboard
    </a>

<%
    } else {
%>

<form method="post"
      action="<%= request.getContextPath() %>/customer/service-request/create">

    <div>
        <label for="productId">Select Product:</label>

        <select id="productId"
                name="productId"
                required>

            <option value="">-- Select Product --</option>

            <%
                for (Product product : products) {
            %>

                <option value="<%= product.getProductId() %>">
                    <%= product.getBrand() %>
                    <%= product.getModel() %>
                    -
                    <%= product.getSerialNumber() %>
                </option>

            <%
                }
            %>

        </select>
    </div>

    <br>

    <div>
        <label for="complaintDescription">
            Complaint Description:
        </label>
        <br>

        <textarea id="complaintDescription"
                  name="complaintDescription"
                  rows="6"
                  cols="50"
                  maxlength="1000"
                  required></textarea>
    </div>

    <br>

    <button type="submit">
        Raise Service Request
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/customer">
    Back to Customer Dashboard
</a>

<%
    }
%>

</body>
</html>