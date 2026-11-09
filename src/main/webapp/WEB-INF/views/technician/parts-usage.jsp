<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Parts Usage</title>
</head>

<body>

<h2>Spare Parts Usage</h2>

<%
    String error = (String) request.getAttribute("error");

    String success =
            (String) session.getAttribute("success");

    if (success != null) {
        session.removeAttribute("success");
    }
%>

<% if (error != null) { %>

<p style="color:red;">
    <strong>Error:</strong>
    <%= error %>
</p>

<% } %>

<% if (success != null) { %>

<p style="color:green;">
    <strong>Success:</strong>
    <%= success %>
</p>

<% } %>

<hr>

<h3>Use Spare Part</h3>

<form method="post"
      action="<%= request.getContextPath() %>/technician/parts-usage">

    <label for="ticketId">Ticket ID:</label><br>

    <input type="number"
           id="ticketId"
           name="ticketId"
           min="1"
           required>

    <br><br>

    <label for="sparePartId">Spare Part ID:</label><br>

    <input type="number"
           id="sparePartId"
           name="sparePartId"
           min="1"
           required>

    <br><br>

    <label for="quantity">Quantity Used:</label><br>

    <input type="number"
           id="quantity"
           name="quantity"
           min="1"
           required>

    <br><br>

    <button type="submit">
        Use Spare Part
    </button>

</form>

<hr>

<p>
    Enter the ticket ID for the repair, the spare-part ID,
    and the quantity used during the repair.
</p>

</body>
</html>