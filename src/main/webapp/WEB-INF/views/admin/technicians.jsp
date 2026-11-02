<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Technicians - ServiceTrack</title>
    <style>
        body{font-family:Arial,sans-serif;margin:30px;background:#f4f6f9;color:#222}
        .panel,.technician{background:white;padding:22px;margin:18px 0;border-radius:8px;box-shadow:0 2px 6px #ddd}
        input{display:block;padding:10px;margin:9px 0;width: min(360px,95%);box-sizing:border-box}
        button,.link{padding:10px 15px;background:#1769aa;color:white;border:0;border-radius:5px;text-decoration:none;cursor:pointer}
        table{width:100%;border-collapse:collapse;background:white}
        th,td{padding:12px;border-bottom:1px solid #ddd;text-align:left}
        .message{padding:12px;margin:12px 0;background:#d4edda}
        .error{background:#f8d7da}
        form.inline{display:inline}
    </style>
</head>
<body>
<h1>Manage Technicians</h1>
<p><a class="link" href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a>
<a class="link" href="${pageContext.request.contextPath}/admin/assignments">Manage Assignments</a></p>

<c:if test="${param.success == 'created'}"><div class="message">Technician account created successfully.</div></c:if>
<c:if test="${param.success == 'updated'}"><div class="message">Technician status updated.</div></c:if>
<c:if test="${param.error == 'invalid'}"><div class="message error">Invalid input. Check all fields and try again.</div></c:if>
<c:if test="${param.error == 'duplicate'}"><div class="message error">That username or email is already registered.</div></c:if>

<div class="panel">
<h2>Create Technician Account</h2>
<form method="post" action="${pageContext.request.contextPath}/admin/technicians">
<input type="hidden" name="action" value="create">
<label>Full name</label><input name="fullName" maxlength="100" required>
<label>Email</label><input type="email" name="email" maxlength="150" required>
<label>Phone</label><input name="phone" maxlength="20">
<label>Username</label><input name="username" maxlength="50" required>
<label>Temporary password (minimum 8 characters)</label><input type="password" name="password" minlength="8" required>
<button type="submit">Create Technician</button>
</form>
</div>

<h2>Registered Technicians</h2>
<c:choose>
<c:when test="${empty technicians}"><div class="panel">No technicians registered yet.</div></c:when>
<c:otherwise>
<table>
<thead><tr><th>Name</th><th>Username</th><th>Email</th><th>Phone</th><th>Status</th><th>Action</th></tr></thead>
<tbody>
<c:forEach var="tech" items="${technicians}">
<tr>
<td><c:out value="${tech.fullName}"/></td>
<td><c:out value="${tech.username}"/></td>
<td><c:out value="${tech.email}"/></td>
<td><c:out value="${tech.phone}"/></td>
<td><c:out value="${tech.status}"/></td>
<td>
<form class="inline" method="post" action="${pageContext.request.contextPath}/admin/technicians">
<input type="hidden" name="action" value="status">
<input type="hidden" name="userId" value="${tech.userId}">
<c:choose>
<c:when test="${tech.status == 'ACTIVE'}">
<input type="hidden" name="status" value="INACTIVE"><button type="submit">Deactivate</button>
</c:when>
<c:otherwise>
<input type="hidden" name="status" value="ACTIVE"><button type="submit">Activate</button>
</c:otherwise>
</c:choose>
</form>
</td>
</tr>
</c:forEach>
</tbody></table>
</c:otherwise>
</c:choose>
</body>
</html>
