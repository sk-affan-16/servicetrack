<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Ticket" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Assigned Tickets</title>
</head>

<body>

<h2>My Assigned Tickets</h2>

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
    List<Ticket> tickets =
            (List<Ticket>) request.getAttribute("tickets");
%>

<%
    if (tickets == null || tickets.isEmpty()) {
%>

    <p>No tickets are currently assigned to you.</p>

<%
    } else {
%>

    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Ticket ID</th>
            <th>Ticket Number</th>
            <th>Service Request ID</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Action</th>
        </tr>

<%
        for (Ticket ticket : tickets) {
%>

        <tr>

            <td>
                <%= ticket.getTicketId() %>
            </td>

            <td>
                <%= ticket.getTicketNumber() %>
            </td>

            <td>
                <%= ticket.getServiceRequestId() %>
            </td>

            <td>
                <%= ticket.getStatus() %>
            </td>

            <td>
                <%= ticket.getCreatedAt() %>
            </td>

            <td>
                <a href="<%= request.getContextPath() %>/technician/ticket?ticketId=<%= ticket.getTicketId() %>">
                    View Ticket
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

<a href="<%= request.getContextPath() %>/customer">
    Back
</a>

</body>
</html>