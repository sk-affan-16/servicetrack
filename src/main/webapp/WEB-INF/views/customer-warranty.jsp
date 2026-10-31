<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.Warranty" %>
<%@ page import="com.servicetrack.controller.WarrantyServlet.ProductWarranty" %>

<%!
    private String escapeWarrantyHtml(String value) {
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

    List<ProductWarranty> productWarranties =
            (List<ProductWarranty>)
                    request.getAttribute("productWarranties");

    int totalProducts = productWarranties == null
            ? 0 : productWarranties.size();

    int activeCount = 0;
    int expiredCount = 0;
    int unavailableCount = 0;

    if (productWarranties != null) {
        for (ProductWarranty item : productWarranties) {
            Warranty warranty = item.getWarranty();

            if (warranty == null) {
                unavailableCount++;
            } else if ("ACTIVE".equalsIgnoreCase(
                    warranty.getWarrantyStatus())) {
                activeCount++;
            } else if ("EXPIRED".equalsIgnoreCase(
                    warranty.getWarrantyStatus())) {
                expiredCount++;
            }
        }
    }

    String contextPath = request.getContextPath();
%>

<div class="page-heading">
    <div>
        <h1>My Warranty</h1>
        <p>Check warranty coverage for your registered products.</p>
    </div>
</div>

<% if (error != null) { %>
<div class="profile-alert profile-error">
    <strong>Error:</strong>
    <%= escapeWarrantyHtml(error) %>
</div>
<% } %>

<div class="warranty-summary-grid">

    <div class="warranty-summary-card">
        <span class="warranty-summary-label">Total Products</span>
        <strong><%= totalProducts %></strong>
    </div>

    <div class="warranty-summary-card warranty-active-card">
        <span class="warranty-summary-label">Active Warranties</span>
        <strong><%= activeCount %></strong>
    </div>

    <div class="warranty-summary-card warranty-expired-card">
        <span class="warranty-summary-label">Expired Warranties</span>
        <strong><%= expiredCount %></strong>
    </div>

    <div class="warranty-summary-card">
        <span class="warranty-summary-label">Not Available</span>
        <strong><%= unavailableCount %></strong>
    </div>

</div>

<section class="warranty-main-card">

    <div class="warranty-card-heading">
        <div>
            <h2>Product Warranty Details</h2>
            <p>Warranty information for each registered product.</p>
        </div>
    </div>

    <% if (productWarranties == null
            || productWarranties.isEmpty()) { %>

    <div class="warranty-empty">
        <div class="warranty-empty-icon">□</div>
        <h3>No Products Registered</h3>
        <p>Register a product first to view its warranty information.</p>

        <a class="profile-button warranty-register-button"
           href="<%= contextPath %>/customer/products">
            Register a Product
        </a>
    </div>

    <% } else { %>

    <div class="warranty-table-wrapper">

        <table class="warranty-table">

            <thead>
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
            </thead>

            <tbody>

            <% for (ProductWarranty item : productWarranties) {

                Product product = item.getProduct();
                Warranty warranty = item.getWarranty();

                String status = warranty == null
                        ? "NOT AVAILABLE"
                        : warranty.getWarrantyStatus();

                String statusClass = "warranty-unavailable";

                if ("ACTIVE".equalsIgnoreCase(status)) {
                    statusClass = "warranty-active";
                } else if ("EXPIRED".equalsIgnoreCase(status)) {
                    statusClass = "warranty-expired";
                } else if ("VOID".equalsIgnoreCase(status)) {
                    statusClass = "warranty-void";
                }
            %>

            <tr>

                <td>
                    <strong>
                        <%= escapeWarrantyHtml(product.getProductName()) %>
                    </strong>
                </td>

                <td>
                    <%= escapeWarrantyHtml(product.getBrand()) %>
                </td>

                <td>
                    <%= product.getModelNumber() == null
                            || product.getModelNumber().trim().isEmpty()
                            ? "-"
                            : escapeWarrantyHtml(product.getModelNumber()) %>
                </td>

                <td>
                    <%= escapeWarrantyHtml(product.getSerialNumber()) %>
                </td>

                <td>
                    <%= product.getPurchaseDate() == null
                            ? "-"
                            : product.getPurchaseDate() %>
                </td>

                <% if (warranty == null) { %>

                <td>-</td>
                <td>-</td>

                <% } else { %>

                <td><%= warranty.getWarrantyStart() %></td>
                <td><%= warranty.getWarrantyEnd() %></td>

                <% } %>

                <td>
                            <span class="warranty-status <%= statusClass %>">
                                <%= escapeWarrantyHtml(status) %>
                            </span>
                </td>

            </tr>

            <% } %>

            </tbody>

        </table>

    </div>

    <% } %>

</section>

<div class="warranty-shortcuts">
    <a href="<%= contextPath %>/customer/products">
        My Products
    </a>

    <a href="<%= contextPath %>/customer/profile">
        Customer Profile
    </a>

    <a href="<%= contextPath %>/customer/">
        ← Back to Dashboard
    </a>
</div>