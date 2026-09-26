<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.Ticket" %>
<%@ page import="com.servicetrack.model.ServiceRequest" %>
<%@ page import="com.servicetrack.model.Product" %>
<%@ page import="com.servicetrack.model.Customer" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ticket Tracking</title>
</head>

<body>

<h2>Ticket Tracking</h2>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>

    <p style="color: red;">
        <%= error %>
    </p>

<%
    } else {

        Ticket ticket =
                (Ticket) request.getAttribute("ticket");

        ServiceRequest serviceRequest =
                (ServiceRequest) request.getAttribute("serviceRequest");

        Product product =
                (Product) request.getAttribute("product");

        Customer customer =
                (Customer) request.getAttribute("customer");

        if (ticket != null
                && serviceRequest != null
                && product != null
                && customer != null) {
%>

    <h3>Ticket Information</h3>

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
            <th>Created At</th>
            <td>
                <%= ticket.getCreatedAt() %>
            </td>
        </tr>

        <tr>
            <th>Updated At</th>
            <td>
                <%= ticket.getUpdatedAt() %>
            </td>
        </tr>

    </table>

    <br>

    <h3>Service Request Information</h3>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Service Request ID</th>
            <td>
                <%= serviceRequest.getServiceRequestId() %>
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

    </table>

    <br>

    <h3>Product Information</h3>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Product ID</th>
            <td>
                <%= product.getProductId() %>
            </td>
        </tr>

        <tr>
            <th>Product Name</th>
            <td>
                <%= product.getProductName() %>
            </td>
        </tr>

        <tr>
            <th>Brand</th>
            <td>
                <%= product.getBrand() %>
            </td>
        </tr>

        <tr>
            <th>Model Number</th>
            <td>
                <%= product.getModelNumber() != null
                        ? product.getModelNumber()
                        : "-" %>
            </td>
        </tr>

        <tr>
            <th>Serial Number</th>
            <td>
                <%= product.getSerialNumber() %>
            </td>
        </tr>

        <tr>
            <th>Purchase Date</th>
            <td>
                <%= product.getPurchaseDate() %>
            </td>
        </tr>

    </table>

    <br>

    <h3>Customer Information</h3>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Customer ID</th>
            <td>
                <%= customer.getCustomerId() %>
            </td>
        </tr>

        <tr>
            <th>Customer Name</th>
            <td>
                <%= customer.getFullName() %>
            </td>
        </tr>

    </table>

<%
        } else {
%>

    <p>Ticket tracking information is not available.</p>

<%
        }
    }
%>

<br>

<a href="<%= request.getContextPath() %>/customer/service-requests">
    Back to My Service Requests
</a>

<br><br>

<a href="<%= request.getContextPath() %>/customer">
    Back to Customer Dashboard
</a>

</body>
</html>