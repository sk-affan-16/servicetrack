
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Ticket" %>
<%@ page import="com.servicetrack.model.SparePart" %>

<%!
    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    List<Ticket> assignedTickets =
            (List<Ticket>) request.getAttribute("assignedTickets");

    List<SparePart> availableParts =
            (List<SparePart>) request.getAttribute("availableParts");

    Long selectedTicketId =
            (Long) request.getAttribute("selectedTicketId");

    String error =
            (String) request.getAttribute("error");

    String success =
            (String) session.getAttribute("success");

    if (success != null) {
        session.removeAttribute("success");
    }

    if (assignedTickets == null) {
        assignedTickets = java.util.Collections.emptyList();
    }

    if (availableParts == null) {
        availableParts = java.util.Collections.emptyList();
    }
%>

<div class="dashboard-content">

    <div class="page-heading">
        <div>
            <h1>Spare Parts Usage</h1>
            <p>Select your assigned repair ticket and the spare part used.</p>
        </div>
    </div>

    <% if (error != null) { %>

    <div class="alert alert-error">
        <strong>Error:</strong>
        <%= escapeHtml(error) %>
    </div>

    <% } %>

    <% if (success != null) { %>

    <div class="alert alert-success">
        <strong>Success:</strong>
        <%= escapeHtml(success) %>
    </div>

    <% } %>

    <div class="service-card">

        <h2>Record Spare Part Usage</h2>

        <p>Select the ticket and spare part from the available options.</p>

        <form method="post"
              action="<%= request.getContextPath() %>/technician/parts-usage">

            <div class="form-group">

                <label for="ticketId">
                    Select Assigned Ticket
                </label>

                <select id="ticketId"
                        name="ticketId"
                        required>

                    <option value="">
                        -- Select Your Ticket --
                    </option>

                    <% for (Ticket ticket : assignedTickets) { %>

                    <option
                            value="<%= ticket.getTicketId() %>"
                            <%= selectedTicketId != null
                                    && selectedTicketId.equals(ticket.getTicketId())
                                    ? "selected" : "" %>>

                        <%= escapeHtml(ticket.getTicketNumber()) %>
                        - Ticket #<%= ticket.getTicketId() %>

                    </option>

                    <% } %>

                </select>

                <% if (assignedTickets.isEmpty()) { %>

                <p>No tickets are currently assigned to you.</p>

                <% } %>

            </div>

            <div class="form-group">

                <label for="sparePartId">
                    Select Spare Part
                </label>

                <select id="sparePartId"
                        name="sparePartId"
                        required>

                    <option value="">
                        -- Select Spare Part --
                    </option>

                    <% for (SparePart part : availableParts) { %>

                    <option value="<%= part.getSparePartId() %>">

                        <%= escapeHtml(part.getPartName()) %>
                        -
                        <%= escapeHtml(part.getPartNumber()) %>
                        (Stock: <%= part.getQuantity() %>)

                    </option>

                    <% } %>

                </select>

                <% if (availableParts.isEmpty()) { %>

                <p>No spare parts are currently available in stock.</p>

                <% } %>

            </div>

            <div class="form-group">

                <label for="quantity">
                    Quantity Used
                </label>

                <input type="number"
                       id="quantity"
                       name="quantity"
                       min="1"
                       placeholder="Enter quantity used"
                       required>

            </div>

            <button type="submit"
                    class="btn"
                    <%= assignedTickets.isEmpty()
                            || availableParts.isEmpty()
                            ? "disabled" : "" %>>

                Use Spare Part

            </button>

        </form>

    </div>

    <div style="margin-top: 20px;">

        <a class="btn"
           href="<%= request.getContextPath() %>/technician/">

            Back to Dashboard

        </a>

    </div>

</div>
