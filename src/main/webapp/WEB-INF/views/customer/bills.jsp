<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>My Bills - ServiceTrack</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #eeeeee;
        }

        .paid {
            color: green;
            font-weight: bold;
        }

        .unpaid {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

<h2>My Service Bills</h2>

<c:choose>
    <c:when test="${empty bills}">
        <p>No bills available yet.</p>
    </c:when>

    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Bill ID</th>
                    <th>Ticket ID</th>
                    <th>Parts Cost</th>
                    <th>Labour Charge</th>
                    <th>Tax</th>
                    <th>Total Amount</th>
                    <th>Payment Status</th>
                    <th>Generated At</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var="bill" items="${bills}">
                    <tr>
                        <td>${bill.billId}</td>
                        <td>${bill.ticketId}</td>
                        <td>${bill.partsCost}</td>
                        <td>${bill.labourCharge}</td>
                        <td>${bill.taxAmount}</td>
                        <td>${bill.totalAmount}</td>

                        <td>
                            <c:choose>
                                <c:when test="${bill.paymentStatus == 'PAID'}">
                                    <span class="paid">PAID</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="unpaid">UNPAID</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>${bill.generatedAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<br>

<a href="${pageContext.request.contextPath}/customer/">
    Back to Dashboard
</a>

</body>
</html>