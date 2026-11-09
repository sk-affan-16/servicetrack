<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.SparePart" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventory Management - ServiceTrack</title>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="inventory-page">
<%
    String error = (String) request.getAttribute("error");
    List<SparePart> spareParts =
            (List<SparePart>) request.getAttribute("spareParts");
    int totalParts = spareParts == null ? 0 : spareParts.size();
    int totalStock = 0;
    int lowStockItems = 0;
    if (spareParts != null) {
        for (SparePart part : spareParts) {
            totalStock += part.getQuantity();
            if (part.getQuantity() <= 5) {
                lowStockItems++;
            }
        }
    }
%>
<!-- PAGE HEADING -->
<div class="inventory-heading">
    <div>
        <span class="inventory-eyebrow">ADMINISTRATION</span>
        <h1>Inventory Management</h1>
        <p>Manage spare parts, stock quantities and prices.</p>
    </div>
    <a class="inventory-back"
       href="${pageContext.request.contextPath}/admin/dashboard">
        ← Admin Dashboard
    </a>
</div>
<!-- ERROR MESSAGE -->
<% if (error != null) { %>
    <div class="inventory-alert inventory-error">
        <strong>Error:</strong> <%= error %>
    </div>
<% } %>
<!-- INVENTORY SUMMARY -->
<div class="inventory-summary">
    <div class="inventory-summary-card">
        <span>Total Spare Parts</span>
        <strong><%= totalParts %></strong>
    </div>
    <div class="inventory-summary-card">
        <span>Total Stock Units</span>
        <strong><%= totalStock %></strong>
    </div>
    <div class="inventory-summary-card">
        <span>Low Stock Items</span>
        <strong><%= lowStockItems %></strong>
    </div>
</div>
<!-- ADD SPARE PART -->
<div class="inventory-card">
    <div class="inventory-card-heading">
        <div>
            <h2>Add New Spare Part</h2>
            <p>Enter the details of the spare part.</p>
        </div>
    </div>
    <form method="post"
          action="${pageContext.request.contextPath}/admin/inventory">
        <input type="hidden"
               name="action"
               value="add">
        <div class="inventory-form-grid">
            <div class="inventory-field">
                <label for="partName">Part Name</label>
                <input type="text"
                       id="partName"
                       name="partName"
                       maxlength="150"
                       placeholder="Example: Laptop Keyboard"
                       required>
            </div>
            <div class="inventory-field">
                <label for="partNumber">Part Number</label>
                <input type="text"
                       id="partNumber"
                       name="partNumber"
                       maxlength="100"
                       placeholder="Example: KB-001"
                       required>
            </div>
            <div class="inventory-field">
                <label for="quantity">Initial Quantity</label>
                <input type="number"
                       id="quantity"
                       name="quantity"
                       min="0"
                       placeholder="Enter quantity"
                       required>
            </div>
            <div class="inventory-field">
                <label for="unitPrice">Unit Price (₹)</label>
                <input type="number"
                       id="unitPrice"
                       name="unitPrice"
                       min="0"
                       step="0.01"
                       placeholder="Enter price"
                       required>
            </div>
        </div>
        <button type="submit" class="inventory-button">
            + Add Spare Part
        </button>
    </form>
</div>
<!-- CURRENT SPARE PARTS -->
<div class="inventory-card">
    <div class="inventory-card-heading">
        <div>
            <h2>Current Spare Parts</h2>
            <p>View and manage your available inventory.</p>
        </div>
        <span class="inventory-count">
            <%= totalParts %> Items
        </span>
    </div>
    <% if (spareParts == null || spareParts.isEmpty()) { %>
        <div class="inventory-empty">
            <div class="inventory-empty-icon">📦</div>
            <h3>No Spare Parts Found</h3>
            <p>Add your first spare part using the form above.</p>
        </div>
    <% } else { %>
        <div class="inventory-table-wrapper">
            <table class="inventory-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Part Details</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Update Stock</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% for (SparePart sparePart : spareParts) { %>
                    <tr>
                        <!-- ID -->
                        <td>
                            <strong>
                                #<%= sparePart.getSparePartId() %>
                            </strong>
                        </td>
                        <!-- PART DETAILS -->
                        <td>
                            <strong>
                                <%= sparePart.getPartName() %>
                            </strong>
                            <br>
                            <small>
                                Part No: <%= sparePart.getPartNumber() %>
                            </small>
                        </td>
                        <!-- QUANTITY -->
                        <td>
                            <% if (sparePart.getQuantity() <= 5) { %>
                                <span class="stock-badge stock-low">
                                    Low Stock: <%= sparePart.getQuantity() %>
                                </span>
                            <% } else { %>
                                <span class="stock-badge stock-available">
                                    Available: <%= sparePart.getQuantity() %>
                                </span>
                            <% } %>
                        </td>
                        <!-- PRICE -->
                        <td class="inventory-price">
                            ₹<%= sparePart.getUnitPrice() %>
                        </td>
                        <!-- UPDATE STOCK -->
                        <td>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/inventory"
                                  class="stock-update-form">
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
                                <button type="submit"
                                        class="inventory-update-button">
                                    Update
                                </button>
                            </form>
                        </td>
                        <!-- DELETE -->
                        <td>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/inventory"
                                  onsubmit="return confirm('Are you sure you want to delete this spare part?');">
                                <input type="hidden"
                                       name="action"
                                       value="delete">
                                <input type="hidden"
                                       name="sparePartId"
                                       value="<%= sparePart.getSparePartId() %>">
                                <button type="submit"
                                        class="inventory-delete-button">
                                    Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    <% } %>
</div>
<!-- FOOTER -->
<div class="inventory-footer">
    <a href="${pageContext.request.contextPath}/admin/dashboard">
        ← Back to Admin Dashboard
    </a>
</div>
</div>
</body>
</html>