<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servicetrack.model.Ticket" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ticket Details</title>
</head>

<body>

<h2>Ticket Details</h2>

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

        if (ticket != null) {
%>

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
            <th>Service Request ID</th>
            <td>
                <%= ticket.getServiceRequestId() %>
            </td>
        </tr>

        <tr>
            <th>Technician ID</th>
            <td>
                <%= ticket.getTechnicianId() %>
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

<%
        } else {
%>

    <p>Ticket details are not available.</p>

<%
        }
    }
%>

<br>

<a href="<%= request.getContextPath() %>/technician/tickets">
    Back to Assigned Tickets
</a>

</body>
</html>