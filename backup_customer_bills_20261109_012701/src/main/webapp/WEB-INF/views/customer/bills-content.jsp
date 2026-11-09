<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>My Service Bills</h2>
<p>View bills generated for your completed service tickets.</p>
<c:choose>
<c:when test="${empty bills}"><div class="content-card"><p>No bills available yet. Bills appear here after the service team generates them.</p></div></c:when>
<c:otherwise><div class="table-responsive"><table class="data-table"><thead><tr><th>Bill ID</th><th>Ticket ID</th><th>Parts Cost</th><th>Labour Charge</th><th>Tax</th><th>Total</th><th>Payment Status</th><th>Generated At</th></tr></thead><tbody>
<c:forEach var="bill" items="${bills}"><tr><td>${bill.billId}</td><td>${bill.ticketId}</td><td>₹${bill.partsCost}</td><td>₹${bill.labourCharge}</td><td>₹${bill.taxAmount}</td><td><strong>₹${bill.totalAmount}</strong></td><td><span class="status-badge">${bill.paymentStatus}</span></td><td>${bill.generatedAt}</td></tr></c:forEach>
</tbody></table></div></c:otherwise></c:choose>
