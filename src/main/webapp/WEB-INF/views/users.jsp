<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CreativePulse</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app">
<aside class="sidebar">
  <div class="brand">Creative<span>Pulse</span></div>
  <div class="small">Advertising Agency Management</div>
  <nav>
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/users">User Management</a>
    <a href="${pageContext.request.contextPath}/tasks">Employee Task Management</a>
    <a href="${pageContext.request.contextPath}/campaigns">Campaign Management</a>
    <a href="${pageContext.request.contextPath}/advertisements">Advertisement Management</a>
    <a href="${pageContext.request.contextPath}/billing">Billing & Payment</a>
    <a href="${pageContext.request.contextPath}/reports">Report Management</a>
    <a href="${pageContext.request.contextPath}/profile">Profile</a>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </nav>
</aside>
<main class="main">
<h1>User Management</h1><div class="toolbar"><button class="primary">Add User</button><button>View</button><button>Edit</button><button>Delete</button></div>
<div class="panel"><table><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Status</th></tr>
<tr><td>1</td><td>System Administrator</td><td>admin@creativepulse.local</td><td>ADMIN</td><td><span class="badge active">Active</span></td></tr></table></div></main></div></body></html>