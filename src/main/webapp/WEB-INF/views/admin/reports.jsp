<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>ServiceTrack Reports</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f4f6f9;
        }

        h1, h2 {
            color: #222;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .card {
            background: white;
            padding: 20px;
            border-radius: 8px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }

        th {
            background: #1769aa;
            color: white;
        }

        a {
            display: inline-block;
            margin-top: 25px;
            color: #1769aa;
        }
    </style>
</head>

<body>

<h1>ServiceTrack Reports</h1>

<h2>Revenue Summary</h2>

<div class="cards">

    <div class="card">
        <h3>Total Revenue</h3>
        <h2>₹${summary.totalRevenue}</h2>
    </div>

    <div class="card">
        <h3>Paid Revenue</h3>
        <h2>₹${summary.paidRevenue}</h2>
    </div>

    <div class="card">
        <h3>Unpaid Revenue</h3>
        <h2>₹${summary.unpaidRevenue}</h2>
    </div>

</div>

<h2>Ticket Status Report</h2>

<table>
    <tr>
        <th>Ticket Status</th>
        <th>Total Tickets</th>
    </tr>

    <c:forEach var="entry"
               items="${summary.ticketStatuses}">

        <tr>
            <td>${entry.key}</td>
            <td>${entry.value}</td>
        </tr>

    </c:forEach>
</table>

<h2>Technician Performance Report</h2>

<table>
    <tr>
        <th>Technician ID</th>
        <th>Technician Name</th>
        <th>Assigned Tickets</th>
        <th>Completed Repairs</th>
    </tr>

    <c:forEach var="technician"
               items="${technicians}">

        <tr>
            <td>${technician.technicianId}</td>
            <td>${technician.technicianName}</td>
            <td>${technician.totalTickets}</td>
            <td>${technician.completedRepairs}</td>
        </tr>

    </c:forEach>
</table>

<a href="${pageContext.request.contextPath}/admin/dashboard">
    Back to Dashboard
</a>

</body>
</html>
