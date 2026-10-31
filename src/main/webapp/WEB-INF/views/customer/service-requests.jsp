
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.ServiceRequest" %>
<%@ page import="com.servicetrack.model.Product" %>

<%!
    private String escapeRequestHtml(String value) {
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

    List<ServiceRequest> serviceRequests =
            (List<ServiceRequest>) request.getAttribute("serviceRequests");

    List<Product> products =
            (List<Product>) request.getAttribute("products");

    int requestCount = serviceRequests == null ? 0 : serviceRequests.size();
    int productCount = products == null ? 0 : products.size();

    String contextPath = request.getContextPath();
%>

<div class="request-page-heading">
    <div>
        <h1>My Service Requests</h1>
        <p>View and track repair requests for your registered products.</p>
    </div>

    <a href="<%= contextPath %>/customer/service-request/new"
       class="request-primary-button">
        + Raise New Request
    </a>
</div>

<% if (error != null) { %>
<div class="profile-alert profile-error">
    <strong>Error:</strong> <%= escapeRequestHtml(error) %>
</div>
<% } %>

<div class="request-summary-grid">

    <div class="request-summary-card">
        <span>My Products</span>
        <strong><%= productCount %></strong>
    </div>

    <div class="request-summary-card">
        <span>Total Service Requests</span>
        <strong><%= requestCount %></strong>
    </div>

</div>

<section class="request-list-card">

    <div class="request-list-heading">
        <div>
            <h2>Service Request History</h2>
            <p>Your submitted service and repair requests.</p>
        </div>

        <span class="request-total-badge">
            <%= requestCount %> Requests
        </span>
    </div>

    <% if (serviceRequests == null || serviceRequests.isEmpty()) { %>

    <div class="request-empty">
        <div class="request-empty-icon">□</div>
        <h3>No Service Requests Found</h3>
        <p>You have not submitted any service requests yet.</p>

        <a href="<%= contextPath %>/customer/service-request/new"
           class="request-primary-button">
            Raise Your First Request
        </a>
    </div>

    <% } else { %>

    <div class="request-table-wrapper">
        <table class="request-table">

            <thead>
            <tr>
                <th>Request ID</th>
                <th>Product ID</th>
                <th>Complaint</th>
                <th>Created At</th>
                <th>Action</th>
            </tr>
            </thead>

            <tbody>
            <% for (ServiceRequest serviceRequest : serviceRequests) { %>
            <tr>
                <td>
                    <strong>#<%= serviceRequest.getServiceRequestId() %></strong>
                </td>

                <td><%= serviceRequest.getProductId() %></td>

                <td class="request-complaint">
                    <%= escapeRequestHtml(
                            serviceRequest.getComplaintDescription()
                    ) %>
                </td>

                <td>
                    <%= serviceRequest.getCreatedAt() == null
                            ? "-"
                            : serviceRequest.getCreatedAt() %>
                </td>

                <td>
                    <a class="request-view-link"
                       href="<%= contextPath %>/customer/service-request/details?serviceRequestId=<%= serviceRequest.getServiceRequestId() %>">
                        View Details →
                    </a>
                </td>
            </tr>
            <% } %>
            </tbody>

        </table>
    </div>

    <% } %>

</section>

<div class="request-page-links">
    <a href="<%= contextPath %>/customer/products">My Products</a>
    <a href="<%= contextPath %>/customer/warranty">My Warranty</a>
    <a href="<%= contextPath %>/customer/">← Back to Dashboard</a>
</div>
