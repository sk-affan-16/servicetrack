<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Ticket" %>
<%@ page import="com.servicetrack.model.SparePart" %>

<%!
    private String esc(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
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

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");

    if (assignedTickets == null) {
        assignedTickets = new java.util.ArrayList<>();
    }

    if (availableParts == null) {
        availableParts = new java.util.ArrayList<>();
    }
%>

<div class="st-parts-page">

    <div class="heading st-parts-heading">
        <div>
            <h1>Spare Parts Usage</h1>
            <p class="subtitle">
                Record the parts consumed for your assigned repair jobs.
            </p>
        </div>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-error">
        <%= esc(error) %>
    </div>
    <% } %>

    <% if (success != null) { %>
    <div class="alert alert-success">
        <%= esc(success) %>
    </div>
    <% } %>

    <div class="card st-parts-card">

        <div class="st-parts-card-header">
            <div>
                <h2>Record Spare Part Usage</h2>
                <p>Select the repair ticket and spare part you used.</p>
            </div>
            <span class="st-parts-icon">&#9881;</span>
        </div>

        <form method="post"
              action="<%= request.getContextPath() %>/technician/parts-usage"
              class="st-parts-form">

            <div class="st-parts-form-group">
                <label for="ticketId">Assigned Ticket</label>

                <select id="ticketId"
                        name="ticketId"
                        required
                        <%= assignedTickets.isEmpty() ? "disabled" : "" %>>

                    <option value="">-- Select ticket --</option>

                    <% for (Ticket t : assignedTickets) { %>
                    <option value="<%= t.getTicketId() %>"
                            <%= selectedTicketId != null
                                    && selectedTicketId.equals(t.getTicketId())
                                    ? "selected" : "" %>>
                        <%= esc(t.getTicketNumber()) %>
                        &middot; #<%= t.getTicketId() %>
                    </option>
                    <% } %>

                </select>

                <% if (assignedTickets.isEmpty()) { %>
                <p class="st-parts-help">
                    No tickets are currently assigned to you.
                </p>
                <% } else { %>
                <p class="st-parts-help">
                    Choose the ticket for which the part was consumed.
                </p>
                <% } %>
            </div>

            <div class="st-parts-form-group">
                <label for="sparePartId">Spare Part</label>

                <select id="sparePartId"
                        name="sparePartId"
                        required
                        <%= availableParts.isEmpty() ? "disabled" : "" %>>

                    <option value="">-- Select spare part --</option>

                    <% for (SparePart p : availableParts) { %>
                    <option value="<%= p.getSparePartId() %>">
                        <%= esc(p.getPartName()) %>
                        &middot;
                        <%= esc(p.getPartNumber()) %>
                        (Stock: <%= p.getQuantity() %>)
                    </option>
                    <% } %>

                </select>

                <% if (availableParts.isEmpty()) { %>
                <p class="st-parts-help">
                    No spare parts are currently available.
                </p>
                <% } else { %>
                <p class="st-parts-help">
                    Only select a part that was used for this repair.
                </p>
                <% } %>
            </div>

            <div class="st-parts-form-group st-parts-quantity">
                <label for="quantity">Quantity Used</label>

                <input type="number"
                       id="quantity"
                       name="quantity"
                       min="1"
                       required
                       placeholder="Enter quantity">
            </div>

            <div class="st-parts-actions">
                <button class="btn st-parts-submit"
                        type="submit"
                        <%= assignedTickets.isEmpty() || availableParts.isEmpty()
                                ? "disabled" : "" %>>
                    Record Parts Usage
                </button>
            </div>

        </form>
    </div>

    <a class="btn secondary st-parts-back"
       href="<%= request.getContextPath() %>/technician/">
        &larr; Back to Dashboard
    </a>

</div>