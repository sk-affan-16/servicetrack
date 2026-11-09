
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Repair" %>

<%
    String error = (String) request.getAttribute("error");
    Long ticketId = (Long) request.getAttribute("ticketId");

    List<Repair> repairs =
            (List<Repair>) request.getAttribute("repairs");
%>

<div class="st-repair-page">

    <% if (error != null) { %>
    <div class="alert alert-error">
        <%= error %>
    </div>
    <% } %>

    <!-- PAGE HEADING -->
    <div class="heading st-repair-heading">
        <div>
            <h1>Repair Workspace</h1>
            <p class="subtitle">
                Ticket #<%= ticketId %> &middot;
                Record diagnosis, repair notes and update repair status.
            </p>
        </div>

        <a class="btn secondary"
           href="<%= request.getContextPath() %>/technician/ticket?ticketId=<%= ticketId %>">
            Ticket Details
        </a>
    </div>

    <!-- CREATE DIAGNOSIS -->
    <section class="card st-repair-card">

        <h2>Create Diagnosis</h2>

        <p class="subtitle">
            Create the first repair record for this assigned ticket.
        </p>

        <form method="post"
              action="<%= request.getContextPath() %>/technician/repair"
              class="st-repair-form">

            <input type="hidden" name="action" value="create">

            <input type="hidden"
                   name="ticketId"
                   value="<%= ticketId %>">

            <div class="st-form-group">
                <label for="diagnosis">Diagnosis *</label>

                <textarea
                        id="diagnosis"
                        name="diagnosis"
                        maxlength="2000"
                        required
                        placeholder="Describe the fault found..."></textarea>
            </div>

            <div class="st-form-group">
                <label for="notes">Repair Notes</label>

                <textarea
                        id="notes"
                        name="repairNotes"
                        maxlength="4000"
                        placeholder="Add initial repair notes..."></textarea>
            </div>

            <button class="btn" type="submit">
                Create Repair Record
            </button>

        </form>

    </section>

    <!-- REPAIR HISTORY -->
    <section class="card st-repair-card">

        <h2>Repair History &amp; Status</h2>

        <p class="subtitle">
            Update the latest repair record as work progresses.
            Mark COMPLETED only after the repair is finished.
        </p>

        <% if (repairs == null || repairs.isEmpty()) { %>

        <div class="empty">
            No repair record exists yet.
            Create a diagnosis above to begin.
        </div>

        <% } else { %>

        <% for (Repair repair : repairs) { %>

        <div class="repair-item st-repair-item">

            <div class="st-repair-item-heading">

                <div>
                    <h3>
                        Repair #<%= repair.getRepairId() %>
                    </h3>

                    <span class="badge <%= "COMPLETED".equals(repair.getRepairStatus()) ? "completed" : "repair" %>">
                                <%= repair.getRepairStatus().replace('_', ' ') %>
                            </span>
                </div>

            </div>

            <!-- CURRENT DIAGNOSIS -->
            <div class="st-repair-info">

                <label>Current Diagnosis</label>

                <div class="st-repair-value">
                    <%= repair.getDiagnosis() == null
                            ? "&mdash;"
                            : repair.getDiagnosis() %>
                </div>

            </div>

            <!-- CURRENT NOTES -->
            <div class="st-repair-info">

                <label>Current Repair Notes</label>

                <div class="st-repair-value">
                    <%= repair.getRepairNotes() == null
                            ? "&mdash;"
                            : repair.getRepairNotes() %>
                </div>

            </div>

            <!-- UPDATE REPAIR -->
            <form method="post"
                  action="<%= request.getContextPath() %>/technician/repair"
                  class="st-repair-form">

                <input type="hidden" name="action" value="update">

                <input type="hidden"
                       name="ticketId"
                       value="<%= ticketId %>">

                <input type="hidden"
                       name="repairId"
                       value="<%= repair.getRepairId() %>">

                <div class="st-form-group">

                    <label>Diagnosis</label>

                    <textarea
                            name="diagnosis"
                            maxlength="2000"><%= repair.getDiagnosis() == null ? "" : repair.getDiagnosis() %></textarea>

                </div>

                <div class="st-form-group">

                    <label>Repair Notes</label>

                    <textarea
                            name="repairNotes"
                            maxlength="4000"><%= repair.getRepairNotes() == null ? "" : repair.getRepairNotes() %></textarea>

                </div>

                <div class="st-form-group">

                    <label>Update Repair Status</label>

                    <select name="repairStatus" required>

                        <%
                            String[] states = {
                                    "DIAGNOSING",
                                    "IN_REPAIR",
                                    "WAITING_FOR_PART",
                                    "READY",
                                    "COMPLETED"
                            };

                            for (String state : states) {
                        %>

                        <option value="<%= state %>"
                                <%= state.equals(repair.getRepairStatus())
                                        ? "selected"
                                        : "" %>>

                            <%= state.replace('_', ' ') %>

                        </option>

                        <% } %>

                    </select>

                </div>

                <button class="btn" type="submit">
                    Save Repair Update
                </button>

            </form>

        </div>

        <% } %>

        <% } %>

    </section>

    <a class="btn secondary"
       href="<%= request.getContextPath() %>/technician/tickets">
        &larr; Back to Assigned Jobs
    </a>

</div>
