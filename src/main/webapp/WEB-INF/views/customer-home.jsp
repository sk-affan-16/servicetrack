<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.servicetrack.model.Customer" %>

<%
    String fullName = (String) session.getAttribute("fullName");
    String username = (String) session.getAttribute("username");

    Customer customer =
            (Customer) request.getAttribute("customer");

    String error = (String) request.getAttribute("error");
%>

<div class="dashboard-heading">
    <div>
        <h1>Customer Dashboard</h1>
        <p>Manage your products, warranties and service requests.</p>
    </div>
</div>

<div class="welcome-card">
    <h2>Welcome, <%= fullName != null ? fullName : username %>!</h2>
    <p>Your ServiceTrack customer portal is ready.</p>
</div>

<% if (error != null) { %>
<div class="alert-error">
    <%= error %>
</div>
<% } %>

<div class="card">
    <h3>Customer Profile</h3>

    <% if (customer == null) { %>

    <p>Your customer profile has not been created yet.</p>

    <% } else { %>

    <div class="profile-details">
        <div>
            <strong>Address</strong>
            <p><%= customer.getAddress() != null ? customer.getAddress() : "Not provided" %></p>
        </div>

        <div>
            <strong>City</strong>
            <p><%= customer.getCity() != null ? customer.getCity() : "Not provided" %></p>
        </div>

        <div>
            <strong>State</strong>
            <p><%= customer.getState() != null ? customer.getState() : "Not provided" %></p>
        </div>

        <div>
            <strong>Pincode</strong>
            <p><%= customer.getPincode() != null ? customer.getPincode() : "Not provided" %></p>
        </div>
    </div>

    <% } %>

    <a class="btn"
       href="<%= request.getContextPath() %>/customer/profile">
        View Profile
    </a>
</div>

<h2 class="section-heading">Customer Services</h2>

<div class="service-grid">

    <div class="service-card">
        <div class="service-icon">▣</div>
        <h3>My Products</h3>
        <p>View and manage your registered products.</p>
        <a class="btn"
           href="<%= request.getContextPath() %>/customer/products">
            View Products
        </a>
    </div>

    <div class="service-card">
        <div class="service-icon">✓</div>
        <h3>My Warranty</h3>
        <p>Check warranty details and product coverage.</p>
        <a class="btn"
           href="<%= request.getContextPath() %>/customer/warranty">
            Check Warranty
        </a>
    </div>

    <div class="service-card">
        <div class="service-icon">＋</div>
        <h3>Service Request</h3>
        <p>Submit a repair or service request for your product.</p>
        <a class="btn"
           href="<%= request.getContextPath() %>/customer/service-request/new">
            Raise Request
        </a>
    </div>

    <div class="service-card">
        <div class="service-icon">◷</div>
        <h3>My Profile</h3>
        <p>View your personal and contact information.</p>
        <a class="btn"
           href="<%= request.getContextPath() %>/customer/profile">
            View Details
        </a>
    </div>

</div>