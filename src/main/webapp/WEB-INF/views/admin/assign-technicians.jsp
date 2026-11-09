<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Technician Assignments - ServiceTrack</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f4f6f9;
            color: #222;
        }

        h1 {
            color: #1769aa;
        }

        .top-links {
            margin: 20px 0;
        }

        .top-links a {
            display: inline-block;
            padding: 10px 16px;
            margin-right: 8px;
            background: #1769aa;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .message {
            padding: 12px;
            margin: 15px 0;
            border-radius: 5px;
        }

        .success {
            background: #d4edda;
            color: #155724;
        }

        .error {
            background: #f8d7da;
            color: #721c24;
        }

        .ticket-card {
            background: white;
            padding: 22px;
            margin: 20px 0;
            border-radius: 8px;
            box-shadow: 0 2px 6px #ddd;
        }

        .ticket-card h2 {
            margin-top: 0;
            color: #1769aa;
        }

        .details {
            line-height: 1.8;
        }

        .status {
            font-weight: bold;
        }

        form {
            margin-top: 18px;
            padding-top: 15px;
            border-top: 1px solid #ddd;
        }

        select {
            padding: 9px;
            min-width: 230px;
            margin-right: 10px;
        }

        button {
            padding: 10px 16px;
            background: #198754;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background: #146c43;
        }

        .empty {
            background: white;
            padding: 25px;
            border-radius: 8px;
        }
    </style>
</head>

<body>

<h1>Manage Technician Assignments</h1>

<p>Review service tickets and assign active technicians.</p>

<div class="top-links">
    <a href="${pageContext.request.contextPath}/admin/dashboard">
        Back to Dashboard
    </a>
</div>

<c:if test="${param.success == 'assigned'}">
    <div class="message success">
        Technician assigned successfully!
    </div>
</c:if>

<c:if test="${param.error == 'invalid'}">
    <div class="message error">
        Invalid ticket or technician selection.
    </div>
</c:if>

<c:if test="${param.error == 'assignment'}">
    <div class="message error">
        Assignment failed. Please select an active technician and try again.
    </div>
</c:if>

<c:choose>

    <c:when test="${empty tickets}">
        <div class="empty">
            <h3>No service tickets found.</h3>
            <p>New customer service requests will appear here.</p>
        </div>
    </c:when>

    <c:otherwise>

        <c:forEach var="ticket" items="${tickets}">

            <div class="ticket-card">

                <h2>${ticket.ticketNumber}</h2>

                <div class="details">

                    <strong>Status:</strong>
                    <span class="status">${ticket.status}</span>
                    <br>

                    <strong>Customer:</strong>
                    <c:out value="${ticket.customerName}"/>
                    <br>

                    <strong>Phone:</strong>
                    <c:out value="${ticket.customerPhone}"/>
                    <br>

                    <strong>Product:</strong>
                    <c:out value="${ticket.productName}"/>
                    <br>

                    <strong>Brand:</strong>
                    <c:out value="${ticket.brand}"/>
                    <br>

                    <strong>Model:</strong>
                    <c:out value="${ticket.modelNumber}"/>
                    <br>

                    <strong>Serial Number:</strong>
                    <c:out value="${ticket.serialNumber}"/>
                    <br>

                    <strong>Complaint:</strong>
                    <c:out value="${ticket.complaintDescription}"/>
                    <br>

                    <strong>Currently Assigned:</strong>

                    <c:choose>
                        <c:when test="${not empty ticket.technicianName}">
                            <c:out value="${ticket.technicianName}"/>
                        </c:when>
                        <c:otherwise>
                            Not Assigned
                        </c:otherwise>
                    </c:choose>

                </div>

                <form method="post"
                      action="${pageContext.request.contextPath}/admin/assignments">

                    <input type="hidden"
                           name="ticketId"
                           value="${ticket.ticketId}">

                    <label>
                        <strong>Select Technician:</strong>
                    </label>

                    <select name="technicianId" required>

                        <option value="">
                            -- Select Technician --
                        </option>

                        <c:forEach var="technician"
                                   items="${technicians}">

                            <option value="${technician.userId}"
                                <c:if test="${ticket.technicianId == technician.userId}">
                                    selected
                                </c:if>>
                                <c:out value="${technician.fullName}"/>
                            </option>

                        </c:forEach>

                    </select>

                    <button type="submit">
                        Assign Technician
                    </button>

                </form>

            </div>

        </c:forEach>

    </c:otherwise>

</c:choose>

</body>
</html>
