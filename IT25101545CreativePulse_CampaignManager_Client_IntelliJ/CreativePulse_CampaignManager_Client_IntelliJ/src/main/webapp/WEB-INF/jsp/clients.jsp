<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html><head><title>Client Management</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"></head>
<body><div class="layout"><aside class="sidebar"><div class="brand">Creative<span>Pulse</span></div><div class="role">CLIENT / CAMPAIGN WORKSPACE</div><a href="${pageContext.request.contextPath}/campaign-manager">Campaign Manager</a><a href="${pageContext.request.contextPath}/campaign-manager/campaigns">Campaign Management</a><a href="${pageContext.request.contextPath}/campaign-manager/tasks">Employee Task Management</a><a class="active" href="#">Client Management</a></aside>
<main class="content"><header><div><span class="eyebrow">CLIENT MANAGEMENT</span><h1>Clients</h1><p>Manage client records used by campaigns and client-facing workflows.</p></div><a class="btn" href="${pageContext.request.contextPath}/clients/new">+ Add Client</a></header>
<div class="mini-cards"><div>Registered Clients <b>${total}</b></div></div>
<div class="table-card"><table><thead><tr><th>Client</th><th>Company</th><th>Email</th><th>Phone</th><th>Status</th><th>Actions</th></tr></thead><tbody>
<c:forEach var="c" items="${clients}"><tr><td><b>${c.name}</b></td><td>${c.company}</td><td>${c.email}</td><td>${c.phone}</td><td><span class="badge">${c.status}</span></td><td class="actions"><a href="${pageContext.request.contextPath}/clients/edit/${c.id}">Edit</a><a href="${pageContext.request.contextPath}/clients/delete/${c.id}" onclick="return confirm('Delete this client?')">Delete</a></td></tr></c:forEach>
</tbody></table></div></main></div></body></html>
