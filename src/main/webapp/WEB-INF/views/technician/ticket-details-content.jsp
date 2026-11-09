
<%@ page import="com.servicetrack.model.Ticket" %>

<%
    Ticket ticket = (Ticket) request.getAttribute("ticket");
    String error = (String) request.getAttribute("error");
%>

<div class="st-ticket-details">

    <div class="heading">
        <div>
            <h1>Ticket Details</h1>
            <p class="subtitle">
                View service ticket information and open the repair workspace.
            </p>
        </div>
    </div>

    <% if (error != null) { %>

    <div class="alert alert-error">
        <%= error %>
    </div>

    <% } else if (ticket != null) { %>

    <div class="card st-ticket-card">

        <h2>Service Ticket Information</h2>

        <div class="st-ticket-grid">

            <div class="st-ticket-field">
                <label>Ticket ID</label>
                <p>#<%= ticket.getTicketId() %></p>
            </div>

            <div class="st-ticket-field">
                <label>Ticket Number</label>
                <p><%= ticket.getTicketNumber() %></p>
            </div>

            <div class="st-ticket-field">
                <label>Service Request</label>
                <p>#<%= ticket.getServiceRequestId() %></p>
            </div>

            <div class="st-ticket-field">
                <label>Current Status</label>
                <p>
                        <span class="badge repair">
                            <%= ticket.getStatus().name().replace('_', ' ') %>
                        </span>
                </p>
            </div>

        </div>

        <a class="btn"
           href="<%= request.getContextPath() %>/technician/repair?ticketId=<%= ticket.getTicketId() %>">
            Open Repair Workspace
        </a>

    </div>

    <% } %>

    <a class="btn secondary"
       href="<%= request.getContextPath() %>/technician/tickets">
        &larr; Back to Assigned Jobs
    </a>

</div>
