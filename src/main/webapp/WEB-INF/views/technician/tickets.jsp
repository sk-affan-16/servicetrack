
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Ticket" %>

<div class="technician-tickets-page">

    <div class="page-heading">
        <div>
            <h1>My Assigned Tickets</h1>
            <p>View and manage repair jobs assigned to you.</p>
        </div>

        <a class="btn"
           href="<%= request.getContextPath() %>/technician/">
            Back to Dashboard
        </a>
    </div>

    <%
        String error = (String) request.getAttribute("error");

        if (error != null) {
    %>
    <div class="alert-error">
        <%= error %>
    </div>
    <%
        }

        List<Ticket> tickets =
                (List<Ticket>) request.getAttribute("tickets");
    %>

    <div class="technician-ticket-card">

        <div class="technician-ticket-heading">
            <h2>Assigned Jobs</h2>
            <span>
                <%= tickets == null ? 0 : tickets.size() %> Tickets
            </span>
        </div>

        <% if (tickets == null || tickets.isEmpty()) { %>

        <div class="technician-ticket-empty">
            <h3>No Assigned Tickets</h3>
            <p>
                There are currently no repair jobs assigned to you.
            </p>
        </div>

        <% } else { %>

        <div class="technician-ticket-table-wrapper">

            <table class="technician-ticket-table">

                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Ticket Number</th>
                    <th>Service Request</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>

                <% for (Ticket ticket : tickets) { %>

                <tr>
                    <td>
                        <strong><%= ticket.getTicketId() %></strong>
                    </td>

                    <td>
                        <%= ticket.getTicketNumber() %>
                    </td>

                    <td>
                        <%= ticket.getServiceRequestId() %>
                    </td>

                    <td>
                                <span class="technician-ticket-status">
                                    <%= ticket.getStatus() %>
                                </span>
                    </td>

                    <td>
                        <%= ticket.getCreatedAt() %>
                    </td>

                    <td>
                        <div class="technician-ticket-actions">

                            <a href="<%= request.getContextPath() %>/technician/ticket?ticketId=<%= ticket.getTicketId() %>">
                                View
                            </a>

                            <a href="<%= request.getContextPath() %>/technician/repair?ticketId=<%= ticket.getTicketId() %>">
                                Repair
                            </a>

                            <a href="<%= request.getContextPath() %>/technician/parts-usage?ticketId=<%= ticket.getTicketId() %>">
                                Parts
                            </a>

                        </div>
                    </td>
                </tr>

                <% } %>

                </tbody>

            </table>

        </div>

        <% } %>

    </div>

</div>
