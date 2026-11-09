<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.servicetrack.model.Repair" %>

<!DOCTYPE html>
<html>
<head>
    <title>Technician Repair</title>
</head>
<body>

<h2>Repair Management</h2>

<%
    String error = (String) request.getAttribute("error");
    Long ticketId = (Long) request.getAttribute("ticketId");

    List<Repair> repairs =
            (List<Repair>) request.getAttribute("repairs");
%>

<% if (error != null) { %>
<p style="color:red;">
    <%= error %>
</p>
<% } %>

<p>
    <strong>Ticket ID:</strong>
    <%= ticketId %>
</p>

<hr>

<h3>Create Repair / Diagnosis</h3>

<form method="post"
      action="<%= request.getContextPath() %>/technician/repair">

    <input type="hidden"
           name="action"
           value="create">

    <input type="hidden"
           name="ticketId"
           value="<%= ticketId %>">

    <label for="diagnosis">Diagnosis:</label><br>
    <textarea id="diagnosis"
              name="diagnosis"
              rows="5"
              cols="60"
              required></textarea>

    <br><br>

    <label for="repairNotes">Repair Notes:</label><br>
    <textarea id="repairNotes"
              name="repairNotes"
              rows="5"
              cols="60"></textarea>

    <br><br>

    <button type="submit">
        Create Repair
    </button>

</form>

<hr>

<h3>Existing Repairs</h3>

<%
    if (repairs == null || repairs.isEmpty()) {
%>

<p>No repair record found for this ticket.</p>

<%
} else {
    for (Repair repair : repairs) {
%>

<div>
    <p>
        <strong>Repair ID:</strong>
        <%= repair.getRepairId() %>
    </p>

    <p>
        <strong>Status:</strong>
        <%= repair.getRepairStatus() %>
    </p>

    <p>
        <strong>Diagnosis:</strong>
        <%= repair.getDiagnosis() %>
    </p>

    <p>
        <strong>Repair Notes:</strong>
        <%= repair.getRepairNotes() %>
    </p>

    <h4>Update Repair</h4>

    <form method="post"
          action="<%= request.getContextPath() %>/technician/repair">

        <input type="hidden"
               name="action"
               value="update">

        <input type="hidden"
               name="ticketId"
               value="<%= ticketId %>">

        <input type="hidden"
               name="repairId"
               value="<%= repair.getRepairId() %>">

        <label>Diagnosis:</label><br>
        <textarea name="diagnosis"
                  rows="4"
                  cols="60"><%= repair.getDiagnosis() == null
                ? ""
                : repair.getDiagnosis() %></textarea>

        <br><br>

        <label>Repair Notes:</label><br>
        <textarea name="repairNotes"
                  rows="4"
                  cols="60"><%= repair.getRepairNotes() == null
                ? ""
                : repair.getRepairNotes() %></textarea>

        <br><br>

        <label>Repair Status:</label><br>

        <select name="repairStatus" required>

            <option value="DIAGNOSING"
                    <%= "DIAGNOSING".equals(
                            repair.getRepairStatus())
                            ? "selected"
                            : "" %>>
                DIAGNOSING
            </option>

            <option value="IN_REPAIR"
                    <%= "IN_REPAIR".equals(
                            repair.getRepairStatus())
                            ? "selected"
                            : "" %>>
                IN_REPAIR
            </option>

            <option value="WAITING_FOR_PART"
                    <%= "WAITING_FOR_PART".equals(
                            repair.getRepairStatus())
                            ? "selected"
                            : "" %>>
                WAITING_FOR_PART
            </option>

            <option value="READY"
                    <%= "READY".equals(
                            repair.getRepairStatus())
                            ? "selected"
                            : "" %>>
                READY
            </option>

            <option value="COMPLETED"
                    <%= "COMPLETED".equals(
                            repair.getRepairStatus())
                            ? "selected"
                            : "" %>>
                COMPLETED
            </option>

        </select>

        <br><br>

        <button type="submit">
            Update Repair
        </button>

    </form>

    <hr>

        <%
        }
    }
%>

</body>
</html>