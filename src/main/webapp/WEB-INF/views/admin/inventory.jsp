<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.SparePart" %>

<!DOCTYPE html>
<html>
<head>
    <title>Spare Parts Inventory</title>
</head>

<body>

<h2>Spare Parts Inventory</h2>

<%
    String error = (String) request.getAttribute("error");

    List<SparePart> spareParts =
            (List<SparePart>) request.getAttribute("spareParts");
%>

<% if (error != null) { %>
<p style="color:red;">
    <strong>Error:</strong>
    <%= error %>
</p>
<% } %>

<hr>

<h3>Add Spare Part</h3>

<form method="post"
      action="<%= request.getContextPath() %>/admin/inventory">

    <input type="hidden"
           name="action"
           value="add">

    <label for="partName">Part Name:</label><br>
    <input type="text"
           id="partName"
           name="partName"
           maxlength="150"
           required>
    <br><br>

    <label for="partNumber">Part Number:</label><br>
    <input type="text"
           id="partNumber"
           name="partNumber"
           maxlength="100"
           required>
    <br><br>

    <label for="quantity">Quantity:</label><br>
    <input type="number"
           id="quantity"
           name="quantity"
           min="0"
           required>
    <br><br>

    <label for="unitPrice">Unit Price:</label><br>
    <input type="number"
           id="unitPrice"
           name="unitPrice"
           min="0"
           step="0.01"
           required>
    <br><br>

    <button type="submit">
        Add Spare Part
    </button>

</form>

<hr>

<h3>Current Spare Parts</h3>

<% if (spareParts == null || spareParts.isEmpty()) { %>

<p>No spare parts found.</p>

<% } else { %>

<table border="1" cellpadding="8">

    <tr>
        <th>ID</th>
        <th>Part Name</th>
        <th>Part Number</th>
        <th>Quantity</th>
        <th>Unit Price</th>
        <th>Update Stock</th>
        <th>Delete</th>
    </tr>

    <% for (SparePart sparePart : spareParts) { %>

    <tr>

        <td>
            <%= sparePart.getSparePartId() %>
        </td>

        <td>
            <%= sparePart.getPartName() %>
        </td>

        <td>
            <%= sparePart.getPartNumber() %>
        </td>

        <td>
            <%= sparePart.getQuantity() %>
        </td>

        <td>
            <%= sparePart.getUnitPrice() %>
        </td>

        <td>

            <form method="post"
                  action="<%= request.getContextPath() %>/admin/inventory">

                <input type="hidden"
                       name="action"
                       value="update">

                <input type="hidden"
                       name="sparePartId"
                       value="<%= sparePart.getSparePartId() %>">

                <input type="number"
                       name="quantity"
                       min="0"
                       value="<%= sparePart.getQuantity() %>"
                       required>

                <button type="submit">
                    Set Stock
                </button>

            </form>

        </td>

        <td>

            <form method="post"
                  action="<%= request.getContextPath() %>/admin/inventory">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="sparePartId"
                       value="<%= sparePart.getSparePartId() %>">

                <button type="submit">
                    Delete
                </button>

            </form>

        </td>

    </tr>

    <% } %>

</table>

<% } %>

</body>
</html>