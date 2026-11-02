<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.User" %>
<%@ page import="com.servicetrack.model.Customer" %>

<%
    User user = (User) request.getAttribute("user");
    Customer customer = (Customer) request.getAttribute("customer");

    String error = (String) request.getAttribute("error");
    String updated = request.getParameter("updated");

    String contextPath = request.getContextPath();

    String fullName = user != null && user.getFullName() != null
            ? user.getFullName() : "";

    String email = user != null && user.getEmail() != null
            ? user.getEmail() : "";

    String phone = user != null && user.getPhone() != null
            ? user.getPhone() : "";

    String address = customer != null && customer.getAddress() != null
            ? customer.getAddress() : "";

    String city = customer != null && customer.getCity() != null
            ? customer.getCity() : "";

    String state = customer != null && customer.getState() != null
            ? customer.getState() : "";

    String pincode = customer != null && customer.getPincode() != null
            ? customer.getPincode() : "";
%>

<div class="page-heading">
    <div>
        <h1>My Profile</h1>
        <p>Manage your personal and contact information.</p>
    </div>
</div>

<% if ("user".equals(updated)) { %>
<div class="profile-alert profile-success">
    Personal information updated successfully.
</div>
<% } %>

<% if ("customer".equals(updated)) { %>
<div class="profile-alert profile-success">
    Customer information updated successfully.
</div>
<% } %>

<% if (error != null) { %>
<div class="profile-alert profile-error">
    <strong>Error:</strong> <%= error %>
</div>
<% } %>

<div class="profile-grid">

    <!-- Personal Information -->
    <section class="profile-card">
        <div class="profile-card-heading">
            <div class="profile-icon">01</div>
            <div>
                <h2>Personal Information</h2>
                <p>Update your account details.</p>
            </div>
        </div>

        <form method="post"
              action="<%= contextPath %>/customer/profile">

            <input type="hidden" name="formType" value="user">

            <div class="profile-field">
                <label for="fullName">Full Name</label>
                <input type="text"
                       id="fullName"
                       name="fullName"
                       value="<%= fullName %>"
                       maxlength="100"
                       required>
            </div>

            <div class="profile-field">
                <label for="email">Email Address</label>
                <input type="email"
                       id="email"
                       name="email"
                       value="<%= email %>"
                       maxlength="150"
                       required>
            </div>

            <div class="profile-field">
                <label for="phone">Phone Number</label>
                <input type="text"
                       id="phone"
                       name="phone"
                       value="<%= phone %>"
                       maxlength="20">
            </div>

            <button type="submit" class="profile-button">
                Update Personal Information
            </button>
        </form>
    </section>

    <!-- Customer Information -->
    <section class="profile-card">
        <div class="profile-card-heading">
            <div class="profile-icon">02</div>
            <div>
                <h2>Customer Information</h2>
                <p>Update your address details.</p>
            </div>
        </div>

        <form method="post"
              action="<%= contextPath %>/customer/profile">

            <input type="hidden" name="formType" value="customer">

            <div class="profile-field">
                <label for="address">Address</label>
                <textarea id="address"
                          name="address"
                          rows="3"
                          maxlength="255"><%= address %></textarea>
            </div>

            <div class="profile-field">
                <label for="city">City</label>
                <input type="text"
                       id="city"
                       name="city"
                       value="<%= city %>"
                       maxlength="100">
            </div>

            <div class="profile-field">
                <label for="state">State</label>
                <input type="text"
                       id="state"
                       name="state"
                       value="<%= state %>"
                       maxlength="100">
            </div>

            <div class="profile-field">
                <label for="pincode">Pincode</label>
                <input type="text"
                       id="pincode"
                       name="pincode"
                       value="<%= pincode %>"
                       maxlength="10">
            </div>

            <button type="submit" class="profile-button">
                Update Customer Information
            </button>
        </form>
    </section>

</div>

<div class="profile-back">
    <a href="<%= contextPath %>/customer/">
        ← Back to Customer Dashboard
    </a>
</div>