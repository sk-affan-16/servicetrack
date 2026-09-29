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

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ticket Tracking - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Service Request Tracking</h2>

<%
    if (error != null) {
%>

    <p style="color: red;">
        <%= error %>
    </p>

<%
    } else if (ticket != null
            && serviceRequest != null
            && product != null
            && customer != null
            && user != null) {
%>

    <h3>Ticket Details</h3>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Ticket ID</th>
            <td>
                <%= ticket.getTicketId() %>
            </td>
        </tr>

        <tr>
            <th>Ticket Number</th>
            <td>
                <%= ticket.getTicketNumber() %>
            </td>
        </tr>

        <tr>
            <th>Status</th>
            <td>
                <%= ticket.getStatus() %>
            </td>
        </tr>

        <tr>
            <th>Customer Name</th>
            <td>
                <%= user.getFullName() %>
            </td>
        </tr>

        <tr>
            <th>Username</th>
            <td>
                <%= user.getUsername() %>
            </td>
        </tr>

        <tr>
            <th>Email</th>
            <td>
                <%= user.getEmail() %>
            </td>
        </tr>

        <tr>
            <th>Phone</th>
            <td>
                <%= user.getPhone() != null
                        ? user.getPhone()
                        : "Not provided" %>
            </td>
        </tr>

        <tr>
            <th>Product ID</th>
            <td>
                <%= product.getProductId() %>
            </td>
        </tr>

        <tr>
            <th>Brand</th>
            <td>
                <%= product.getBrand() %>
            </td>
        </tr>

        <tr>
            <th>Model</th>
            <td>
                <%= product.getModelNumber() %>
            </td>
        </tr>

        <tr>
            <th>Serial Number</th>
            <td>
                <%= product.getSerialNumber() %>
            </td>
        </tr>

        <tr>
            <th>Complaint</th>
            <td>
                <%= serviceRequest.getComplaintDescription() %>
            </td>
        </tr>

        <tr>
            <th>Created At</th>
            <td>
                <%= serviceRequest.getCreatedAt() %>
            </td>
        </tr>

        <tr>
            <th>Last Updated</th>
            <td>
                <%= serviceRequest.getUpdatedAt() %>
            </td>
        </tr>

    </table>

<%
    } else {
%>

    <p>
        Ticket information is not available.
    </p>

<%
    }
%>

<br>

<p>
    <a href="<%= request.getContextPath() %>/customer/">
        Back to Customer Dashboard
    </a>
</p>

<p>
    <a href="<%= request.getContextPath() %>/logout">
        Logout
    </a>
</p>

</body>
</html>