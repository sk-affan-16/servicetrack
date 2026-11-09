
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Billing Management - ServiceTrack</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f3f5f8;
            margin: 0;
            padding: 30px;
        }

        .container {
            max-width: 1100px;
            margin: auto;
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        h1 { color: #1769aa; }

        .form-section {
            background: #f5f8fc;
            padding: 20px;
            border-radius: 8px;
            margin: 25px 0;
        }

        input, select, button {
            padding: 10px;
            margin: 7px 5px 7px 0;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        select { min-width: 260px; }

        button {
            background: #1769aa;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover { background: #10558c; }
        button:disabled { background: #999; cursor: not-allowed; }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th { background: #1769aa; color: white; }
        .paid { color: green; font-weight: bold; }
        .unpaid { color: #c0392b; font-weight: bold; }

        .message {
            padding: 12px;
            margin: 12px 0;
            background: #eaf5ea;
            border-radius: 5px;
        }

        .error { background: #fdeaea; }

        .back {
            display: inline-block;
            margin-top: 20px;
            color: #1769aa;
            text-decoration: none;
        }
    </style>
</head>

<body>
<div class="container">

    <h1>Billing Management</h1>
    <p>Generate bills and manage customer payments.</p>

    <c:if test="${param.success == '1'}">
        <div class="message">
            Bill generated or payment updated successfully!
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="message error">
            Billing operation failed:
            <c:choose>
                <c:when test="${param.error == 'invalid_input'}">
                    Please enter valid details.
                </c:when>
                <c:when test="${param.error == 'invalid_request'}">
                    The selected ticket is not eligible or the request is invalid.
                </c:when>
                <c:otherwise>
                    A database error occurred. Please check the server log.
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <div class="form-section">
        <h2>Generate New Bill</h2>

        <c:choose>
            <c:when test="${empty eligibleTickets}">
                <p>No eligible completed tickets available for billing.</p>
            </c:when>

            <c:otherwise>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin/billing">

                    <input type="hidden" name="action" value="generate">

                    <label for="ticketId">Select Completed Ticket:</label>

                    <select id="ticketId" name="ticketId" required>
                        <option value="">-- Select Ticket --</option>

                        <c:forEach var="ticket" items="${eligibleTickets}">
                            <option value="${ticket.ticketId}">
                                <c:out value="${ticket.ticketNumber}"/>
                                (ID: <c:out value="${ticket.ticketId}"/>)
                            </option>
                        </c:forEach>
                    </select>

                    <br>

                    <label for="labourCharge">Labour Charge (&#8377;):</label>

                    <input type="number"
                           id="labourCharge"
                           name="labourCharge"
                           min="0"
                           max="99999999.99"
                           step="0.01"
                           placeholder="Enter labour charge"
                           required>

                    <button type="submit">Generate Bill</button>
                </form>
            </c:otherwise>
        </c:choose>

        <p>Parts cost is calculated automatically. Tax is currently zero.</p>
    </div>

    <h2>All Bills</h2>

    <c:choose>
        <c:when test="${empty bills}">
            <p>No bills have been generated yet.</p>
        </c:when>

        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Bill ID</th>
                    <th>Ticket ID</th>
                    <th>Parts Cost</th>
                    <th>Labour</th>
                    <th>Tax</th>
                    <th>Total</th>
                    <th>Payment Status</th>
                    <th>Generated At</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="bill" items="${bills}">
                    <tr>
                        <td><c:out value="${bill.billId}"/></td>
                        <td><c:out value="${bill.ticketId}"/></td>
                        <td>&#8377;<c:out value="${bill.partsCost}"/></td>
                        <td>&#8377;<c:out value="${bill.labourCharge}"/></td>
                        <td>&#8377;<c:out value="${bill.taxAmount}"/></td>
                        <td><strong>&#8377;<c:out value="${bill.totalAmount}"/></strong></td>

                        <td>
                            <c:choose>
                                <c:when test="${bill.paymentStatus == 'PAID'}">
                                    <span class="paid">PAID</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="unpaid">
                                        <c:out value="${bill.paymentStatus}"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td><c:out value="${bill.generatedAt}"/></td>

                        <td>
                            <c:if test="${bill.paymentStatus == 'UNPAID'}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/billing">

                                    <input type="hidden"
                                           name="action"
                                           value="markPaid">

                                    <input type="hidden"
                                           name="billId"
                                           value="${bill.billId}">

                                    <button type="submit">Mark Paid</button>
                                </form>
                            </c:if>

                            <c:if test="${bill.paymentStatus == 'PAID'}">
                                Payment Complete
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <a class="back"
       href="${pageContext.request.contextPath}/admin/dashboard">
        &#8592; Back to Admin Dashboard
    </a>

</div>
</body>
</html>
