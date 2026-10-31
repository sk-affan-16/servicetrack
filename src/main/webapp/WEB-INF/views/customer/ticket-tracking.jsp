<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.Customer" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.ServiceRequest" %>
<%@ page import="com.servicetrack.model.Ticket" %>
<%@ page import="com.servicetrack.model.User" %>

<%
    Ticket ticket =
            (Ticket) request.getAttribute("ticket");

    ServiceRequest serviceRequest =
            (ServiceRequest) request.getAttribute("serviceRequest");

    Product product =
            (Product) request.getAttribute("product");

    Customer customer =
            (Customer) request.getAttribute("customer");

    User user =
            (User) request.getAttribute("user");

    String error =
            (String) request.getAttribute("error");
%>

<div class="ticket-tracking-page">

    <div class="ticket-page-heading">
        <div>
            <h2>Service Request Tracking</h2>
            <p>View your ticket, product and service request details.</p>
        </div>

        <a class="ticket-back-button"
           href="<%= request.getContextPath() %>/customer/service-requests">
            Back to Requests
        </a>
    </div>

    <% if (error != null) { %>

    <div class="ticket-error">
        <strong>Unable to display ticket</strong>
        <p><%= error %></p>
    </div>

    <% } else if (ticket != null
            && serviceRequest != null
            && product != null
            && customer != null
            && user != null) { %>

    <div class="ticket-summary-card">

        <div>
            <span class="ticket-summary-label">Ticket Number</span>
            <h3><%= ticket.getTicketNumber() %></h3>
        </div>

        <div class="ticket-status">
            <span class="ticket-summary-label">Current Status</span>
            <span class="ticket-status-value">
                    <%= ticket.getStatus() %>
                </span>
        </div>

    </div>

    <div class="ticket-details-grid">

        <section class="ticket-detail-card">

            <h3>Ticket Details</h3>

            <div class="ticket-detail-row">
                <span>Ticket ID</span>
                <strong><%= ticket.getTicketId() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Ticket Number</span>
                <strong><%= ticket.getTicketNumber() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Status</span>
                <strong><%= ticket.getStatus() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Created At</span>
                <strong><%= serviceRequest.getCreatedAt() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Last Updated</span>
                <strong><%= serviceRequest.getUpdatedAt() %></strong>
            </div>

        </section>

        <section class="ticket-detail-card">

            <h3>Customer Details</h3>

            <div class="ticket-detail-row">
                <span>Customer Name</span>
                <strong><%= user.getFullName() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Username</span>
                <strong><%= user.getUsername() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Email</span>
                <strong><%= user.getEmail() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Phone</span>
                <strong>
                    <%= user.getPhone() != null
                            ? user.getPhone()
                            : "Not provided" %>
                </strong>
            </div>

        </section>

        <section class="ticket-detail-card">

            <h3>Product Details</h3>

            <div class="ticket-detail-row">
                <span>Product ID</span>
                <strong><%= product.getProductId() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Brand</span>
                <strong><%= product.getBrand() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Model</span>
                <strong><%= product.getModelNumber() %></strong>
            </div>

            <div class="ticket-detail-row">
                <span>Serial Number</span>
                <strong><%= product.getSerialNumber() %></strong>
            </div>

        </section>

        <section class="ticket-detail-card">

            <h3>Complaint Details</h3>

            <div class="ticket-complaint">
                <%= serviceRequest.getComplaintDescription() %>
            </div>

        </section>

    </div>

    <% } else { %>

    <div class="ticket-error">
        Ticket information is not available.
    </div>

    <% } %>

</div>