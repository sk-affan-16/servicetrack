<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - ServiceTrack</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f4f6f9;
        }

        h1 {
            color: #222;
        }

        .dashboard {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 6px #ddd;
        }

        .card h3 {
            margin-top: 0;
            color: #555;
        }

        .value {
            font-size: 28px;
            font-weight: bold;
            color: #1769aa;
        }

        .links {
            margin-top: 30px;
        }

        .links a {
            display: inline-block;
            margin: 8px;
            padding: 12px 18px;
            background: #1769aa;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
    </style>
</head>

<body>

<h1>ServiceTrack Admin Dashboard</h1>

<p>Welcome, Administrator!</p>

<div class="dashboard">

    <div class="card">
        <h3>Total Customers</h3>
        <div class="value">${dashboard.totalCustomers}</div>
    </div>

    <div class="card">
        <h3>Total Technicians</h3>
        <div class="value">${dashboard.totalTechnicians}</div>
    </div>

    <div class="card">
        <h3>Total Products</h3>
        <div class="value">${dashboard.totalProducts}</div>
    </div>

    <div class="card">
        <h3>Service Requests</h3>
        <div class="value">${dashboard.totalServiceRequests}</div>
    </div>

    <div class="card">
        <h3>Pending Tickets</h3>
        <div class="value">${dashboard.pendingTickets}</div>
    </div>

    <div class="card">
        <h3>Completed Tickets</h3>
        <div class="value">${dashboard.completedTickets}</div>
    </div>

    <div class="card">
        <h3>Total Billed Revenue</h3>
        <div class="value">₹${dashboard.totalRevenue}</div>
    </div>

    <div class="card">
        <h3>Unpaid Amount</h3>
        <div class="value">₹${dashboard.unpaidAmount}</div>
    </div>

</div>

<div class="links">

    <a href="${pageContext.request.contextPath}/admin/billing">
        Billing Management
    </a>

    <a href="${pageContext.request.contextPath}/admin/inventory">
        Inventory
    </a>

    <a href="${pageContext.request.contextPath}/admin/assignments">
        Manage Assignments
    </a>

    <a href="${pageContext.request.contextPath}/admin/technicians">
        Manage Technicians
    </a>

    <a href="${pageContext.request.contextPath}/admin/warranties">
        Manage Warranty
    </a>

    <a href="${pageContext.request.contextPath}/logout">
        Logout
    </a>

</div>

</body>
</html>