<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    String error = (String) request.getAttribute("error");

    List<Product> products =
            (List<Product>) request.getAttribute("products");

    int productCount = products == null ? 0 : products.size();

    String contextPath = request.getContextPath();
%>

<div class="page-heading">
    <div>
        <h1>My Products</h1>
        <p>Register and manage your electronic products.</p>
    </div>
</div>

<% if (error != null) { %>
<div class="profile-alert profile-error">
    <strong>Error:</strong> <%= escapeHtml(error) %>
</div>
<% } %>

<div class="products-summary">
    <div class="products-summary-icon">P</div>
    <div>
        <span class="products-summary-label">Registered Products</span>
        <strong><%= productCount %></strong>
    </div>
</div>

<div class="products-layout">

    <!-- Register Product Form -->
    <section class="products-card">
        <div class="products-card-heading">
            <div>
                <h2>Register New Product</h2>
                <p>Add a product to your ServiceTrack account.</p>
            </div>
        </div>

        <form action="<%= contextPath %>/customer/products"
              method="post">

            <div class="profile-field">
                <label for="productName">Product Name</label>
                <input type="text"
                       id="productName"
                       name="productName"
                       maxlength="100"
                       placeholder="Example: Laptop"
                       required>
            </div>

            <div class="profile-field">
                <label for="brand">Brand</label>
                <input type="text"
                       id="brand"
                       name="brand"
                       maxlength="100"
                       placeholder="Example: Dell"
                       required>
            </div>

            <div class="profile-field">
                <label for="modelNumber">Model Number</label>
                <input type="text"
                       id="modelNumber"
                       name="modelNumber"
                       maxlength="100"
                       placeholder="Enter model number">
            </div>

            <div class="profile-field">
                <label for="serialNumber">Serial Number</label>
                <input type="text"
                       id="serialNumber"
                       name="serialNumber"
                       placeholder="Enter unique serial number"
                       required>
            </div>

            <div class="profile-field">
                <label for="purchaseDate">Purchase Date</label>
                <input type="date"
                       id="purchaseDate"
                       name="purchaseDate"
                       required>
            </div>

            <button type="submit" class="profile-button">
                + Register Product
            </button>
        </form>
    </section>

    <!-- Registered Products -->
    <section class="products-card products-list-card">
        <div class="products-card-heading">
            <div>
                <h2>Registered Products</h2>
                <p>Products linked to your account.</p>
            </div>
            <span class="products-count"><%= productCount %> Total</span>
        </div>

        <% if (products == null || products.isEmpty()) { %>

        <div class="products-empty">
            <div class="products-empty-icon">□</div>
            <h3>No Products Yet</h3>
            <p>You haven't registered any products. Use the form to add your first product.</p>
        </div>

        <% } else { %>

        <div class="products-table-wrapper">
            <table class="products-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product</th>
                    <th>Brand</th>
                    <th>Model</th>
                    <th>Serial Number</th>
                    <th>Purchase Date</th>
                </tr>
                </thead>

                <tbody>
                <% for (Product product : products) { %>
                <tr>
                    <td><%= product.getProductId() %></td>
                    <td>
                        <strong><%= escapeHtml(product.getProductName()) %></strong>
                    </td>
                    <td><%= escapeHtml(product.getBrand()) %></td>
                    <td>
                        <%= product.getModelNumber() == null
                                || product.getModelNumber().trim().isEmpty()
                                ? "-"
                                : escapeHtml(product.getModelNumber()) %>
                    </td>
                    <td><%= escapeHtml(product.getSerialNumber()) %></td>
                    <td><%= product.getPurchaseDate() == null
                            ? "-"
                            : product.getPurchaseDate() %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <% } %>
    </section>

</div>

<div class="products-shortcuts">
    <a href="<%= contextPath %>/customer/warranty">
        My Warranty
    </a>

    <a href="<%= contextPath %>/customer/profile">
        Customer Profile
    </a>

    <a href="<%= contextPath %>/customer/">
        ← Back to Dashboard
    </a>
</div>