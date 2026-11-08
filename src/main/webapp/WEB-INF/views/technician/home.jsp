
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="dashboard-content">

    <div class="page-heading">
        <h1>Technician Dashboard</h1>
        <p>Welcome back, <strong>${sessionScope.fullName}</strong></p>
    </div>

    <div class="dashboard-grid">

        <div class="dashboard-card">
            <h3>Repair Management</h3>
            <p>View and manage your assigned repair jobs.</p>
            <a href="${pageContext.request.contextPath}/technician/tickets">
                View Assigned Jobs
            </a>
        </div>

        <div class="dashboard-card">
            <h3>Parts Usage</h3>
            <p>Select an assigned ticket to manage its spare parts usage.</p>
            <a href="${pageContext.request.contextPath}/technician/tickets">
                Select Assigned Ticket
            </a>
        </div>

    </div>

</div>
