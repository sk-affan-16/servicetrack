<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="dashboard-heading">
    <div>
        <h1>My Bills</h1>
        <p>View your service charges and payment status.</p>
    </div>
</div>

<div class="card">
    <h2>Service Bills</h2>

    <c:choose>
        <c:when test="${empty bills}">
            <p>No bills are available for your account yet.</p>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="data-table">
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
                            <td><c:out value="${bill.billId}"/></td>
                            <td><c:out value="${bill.ticketId}"/></td>
                            <td>₹<c:out value="${bill.partsCost}"/></td>
                            <td>₹<c:out value="${bill.labourCharge}"/></td>
                            <td>₹<c:out value="${bill.taxAmount}"/></td>
                            <td><strong>₹<c:out value="${bill.totalAmount}"/></strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${bill.paymentStatus eq 'PAID'}">
                                        <span class="status-badge status-completed">PAID</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-pending">UNPAID</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${bill.generatedAt}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>
