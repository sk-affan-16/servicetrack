<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Product" %>

<%!
    private String escapeFormHtml(String value) {
        if (value == null) return "";

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

    String contextPath = request.getContextPath();
%>

<div class="request-form-page-heading">
    <div>
        <h1>Raise Service Request</h1>
        <p>Tell us about the problem with your registered product.</p>
    </div>
</div>

<% if (error != null) { %>
<div class="profile-alert profile-error">
    <strong>Error:</strong> <%= escapeFormHtml(error) %>
</div>
<% } %>

<section class="request-form-card">

    <div class="request-form-card-heading">
        <div class="request-form-icon">+</div>
        <div>
            <h2>New Service Request</h2>
            <p>Complete the details below to submit your repair request.</p>
        </div>
    </div>

    <% if (products == null || products.isEmpty()) { %>

    <div class="request-form-empty">
        <h3>No Registered Products</h3>
        <p>You need to register a product before raising a service request.</p>

        <a class="request-form-submit"
           href="<%= contextPath %>/customer/products">
            Register a Product
        </a>
    </div>

    <% } else { %>

    <form method="post"
          action="<%= contextPath %>/customer/service-request/create">

        <div class="request-form-field">
            <label for="productId">Select Product</label>

            <select id="productId"
                    name="productId"
                    required>

                <option value="">-- Select Product --</option>

                <% for (Product product : products) { %>

                <option value="<%= product.getProductId() %>">
                    <%= escapeFormHtml(product.getProductName()) %>
                    -
                    <%= escapeFormHtml(product.getBrand()) %>
                    -
                    <%= product.getModelNumber() == null
                            ? "Model N/A"
                            : escapeFormHtml(product.getModelNumber()) %>
                    -
                    <%= escapeFormHtml(product.getSerialNumber()) %>
                </option>

                <% } %>

            </select>
        </div>

        <div class="request-form-field">
            <label for="complaintDescription">
                Complaint Description
            </label>

            <textarea id="complaintDescription"
                      name="complaintDescription"
                      rows="7"
                      maxlength="1000"
                      placeholder="Describe the issue with your product..."
                      required></textarea>

            <span class="request-form-hint">
                    Maximum 1000 characters.
                </span>
        </div>

        <button type="submit" class="request-form-submit">
            Submit Service Request
        </button>

    </form>

    <% } %>

</section>

<div class="request-form-back">
    <a href="<%= contextPath %>/customer/service-requests">
        ← Back to My Service Requests
    </a>

    <a href="<%= contextPath %>/customer/">
        Customer Dashboard
    </a>
</div>