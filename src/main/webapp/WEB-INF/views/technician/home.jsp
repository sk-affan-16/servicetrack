<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Technician Dashboard - ServiceTrack</title>
</head>

<body>

<h1>ServiceTrack</h1>

<h2>Technician Dashboard</h2>

<p>
    Welcome,
    <strong>
        <%= session.getAttribute("fullName") %>
    </strong>
</p>

<hr>

<h3>Technician Operations</h3>

<ul>
    <li>
        <a href="<%= request.getContextPath() %>/technician/repair?ticketId=1">
            Repair Management
        </a>
    </li>

    <li>
        <a href="<%= request.getContextPath() %>/technician/parts-usage?ticketId=1">
            Parts Usage
        </a>
    </li>
</ul>

<hr>

<p>
    <a href="<%= request.getContextPath() %>/logout">
        Logout
    </a>
</p>

</body>

</html>