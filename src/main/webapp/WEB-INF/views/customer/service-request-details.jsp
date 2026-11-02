<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.Customer" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.ServiceRequest" %>

<%!
    private String escapeDetailsHtml(String value) {
        if (value == null) return "Not provided";

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    ServiceRequest serviceRequest =
            (ServiceRequest) request.getAttribute("serviceRequest");

    Product product =
            (Product) request.getAttribute("product");

    Customer customer =
            (Customer) request.getAttribute("customer");

    String error =
            (String) request.getAttribute("error");

    String contextPath = request.getContextPath();
%>

<div class="service-detail-page">

    <div class="service-detail-heading">
        <div>
            <h1>Service Request Details</h1>
            <p>View complete information about your repair request.</p>
        </div>

        <a href="<%= contextPath %>/customer/service-requests"
           class="service-detail-back">
            Back to Requests
        </a>
    </div>

    <% if (error != null) { %>

        <div class="service-detail-error">
            <strong>Unable to load request</strong>
            <p><%= escapeDetailsHtml(error) %></p>
        </div>

    <% } else if (serviceRequest != null
            && product != null
            && customer != null) { %>

        <div class="service-detail-summary">
            <div>
                <span>Service Request ID</span>
                <h2>#<%= serviceRequest.getServiceRequestId() %></h2>
            </div>

            <div>
                <span>Product</span>
                <h3><%= escapeDetailsHtml(product.getProductName()) %></h3>
            </div>
        </div>

        <div class="service-detail-grid">

            <section class="service-detail-card">
                <h3>Request Information</h3>

                <div class="service-detail-row">
                    <span>Request ID</span>
                    <strong>#<%= serviceRequest.getServiceRequestId() %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Created At</span>
                    <strong><%= serviceRequest.getCreatedAt() == null
                            ? "Not available" : serviceRequest.getCreatedAt() %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Last Updated</span>
                    <strong><%= serviceRequest.getUpdatedAt() == null
                            ? "Not available" : serviceRequest.getUpdatedAt() %></strong>
                </div>
            </section>

            <section class="service-detail-card">
                <h3>Product Information</h3>

                <div class="service-detail-row">
                    <span>Product Name</span>
                    <strong><%= escapeDetailsHtml(product.getProductName()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Brand</span>
                    <strong><%= escapeDetailsHtml(product.getBrand()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Model Number</span>
                    <strong><%= escapeDetailsHtml(product.getModelNumber()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Serial Number</span>
                    <strong><%= escapeDetailsHtml(product.getSerialNumber()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Purchase Date</span>
                    <strong><%= product.getPurchaseDate() == null
                            ? "Not available" : product.getPurchaseDate() %></strong>
                </div>
            </section>

            <section class="service-detail-card">
                <h3>Customer Information</h3>

                <div class="service-detail-row">
                    <span>Customer ID</span>
                    <strong><%= customer.getCustomerId() %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Address</span>
                    <strong><%= escapeDetailsHtml(customer.getAddress()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>City</span>
                    <strong><%= escapeDetailsHtml(customer.getCity()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>State</span>
                    <strong><%= escapeDetailsHtml(customer.getState()) %></strong>
                </div>

                <div class="service-detail-row">
                    <span>Pincode</span>
                    <strong><%= escapeDetailsHtml(customer.getPincode()) %></strong>
                </div>
            </section>

            <section class="service-detail-card">
                <h3>Complaint Information</h3>

                <div class="service-detail-complaint"><%= escapeDetailsHtml(
                        serviceRequest.getComplaintDescription()) %></div>
            </section>

        </div>

    <% } else { %>

        <div class="service-detail-error">
            Service request information is not available.
        </div>

    <% } %>

</div>
