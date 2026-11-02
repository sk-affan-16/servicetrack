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

    <br>

    <h3>Update Ticket Status</h3>

    <form method="post"
          action="<%= request.getContextPath() %>/technician/ticket/status">

        <input type="hidden"
               name="ticketId"
               value="<%= ticket.getTicketId() %>">

        <label for="status">Status:</label>

        <select id="status"
                name="status"
                required>

            <option value="ASSIGNED"
                <%= ticket.getStatus().name().equals("ASSIGNED")
                        ? "selected"
                        : "" %>>
                ASSIGNED
            </option>

            <option value="DIAGNOSING"
                <%= ticket.getStatus().name().equals("DIAGNOSING")
                        ? "selected"
                        : "" %>>
                DIAGNOSING
            </option>

            <option value="IN_REPAIR"
                <%= ticket.getStatus().name().equals("IN_REPAIR")
                        ? "selected"
                        : "" %>>
                IN_REPAIR
            </option>

            <option value="WAITING_FOR_PART"
                <%= ticket.getStatus().name().equals("WAITING_FOR_PART")
                        ? "selected"
                        : "" %>>
                WAITING_FOR_PART
            </option>

            <option value="READY"
                <%= ticket.getStatus().name().equals("READY")
                        ? "selected"
                        : "" %>>
                READY
            </option>

            <option value="COMPLETED"
                <%= ticket.getStatus().name().equals("COMPLETED")
                        ? "selected"
                        : "" %>>
                COMPLETED
            </option>

            <option value="CANCELLED"
                <%= ticket.getStatus().name().equals("CANCELLED")
                        ? "selected"
                        : "" %>>
                CANCELLED
            </option>

        </select>

        <br><br>

        <button type="submit">
            Update Status
        </button>

    </form>

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