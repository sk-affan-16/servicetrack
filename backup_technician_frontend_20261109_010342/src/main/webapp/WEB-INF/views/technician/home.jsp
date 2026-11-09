
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="dashboard-content">

    <div class="page-heading">
        <div>
            <h1>Technician Dashboard</h1>
            <p>Welcome back, <strong>${sessionScope.fullName}</strong></p>
        </div>
    </div>

    <h2 class="section-heading">Technician Operations</h2>

    <div class="service-grid">

        <div class="service-card">
            <div class="service-icon">&#128295;</div>
            <h3>Repair Management</h3>
            <p>View assigned repair jobs, perform diagnosis and update repair progress.</p>

            <a class="btn"
               href="${pageContext.request.contextPath}/technician/tickets">
                View Assigned Jobs
            </a>
        </div>

        <div class="service-card">
            <div class="service-icon">&#128230;</div>
            <h3>Parts Usage</h3>
            <p>View and manage spare parts used during assigned repairs.</p>

            <a class="btn"
               href="${pageContext.request.contextPath}/technician/parts-usage">
                Manage Parts Usage
            </a>
        </div>

    </div>

</div>
