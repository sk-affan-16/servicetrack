<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.ServiceRequest" %>
<%@ page import="com.servicetrack.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Service Requests</title>
</head>

<body>

<h2>My Service Requests</h2>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>
    <p style="color: red;">
        <%= error %>
    </p>
<%
    }
%>

<%
    List<ServiceRequest> serviceRequests =
            (List<ServiceRequest>) request.getAttribute("serviceRequests");

    List<Product> products =
            (List<Product>) request.getAttribute("products");
%>

<%
    if (serviceRequests == null || serviceRequests.isEmpty()) {
%>

    <p>You have no service requests.</p>

<%
    } else {
%>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Service Request ID</th>
            <th>Product ID</th>
            <th>Complaint</th>
            <th>Created At</th>
            <th>Action</th>
        </tr>

<%
        for (ServiceRequest serviceRequest : serviceRequests) {
%>

        <tr>
            <td>
                <%= serviceRequest.getServiceRequestId() %>
            </td>

            <td>
                <%= serviceRequest.getProductId() %>
            </td>

            <td>
                <%= serviceRequest.getComplaintDescription() %>
            </td>

            <td>
                <%= serviceRequest.getCreatedAt() %>
            </td>

            <td>
                <a href="<%= request.getContextPath() %>/customer/service-request/details?serviceRequestId=<%= serviceRequest.getServiceRequestId() %>">
                    View Details
                </a>
            </td>
        </tr>

<%
        }
%>

    </table>

<%
    }
%>

<br>

<a href="<%= request.getContextPath() %>/customer/service-request/new">
    Raise New Service Request
</a>

<br><br>

<a href="<%= request.getContextPath() %>/customer">
    Back to Customer Dashboard
</a>

</body>
</html>