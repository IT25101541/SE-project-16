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
<h1>Advertisement Management</h1><div class="toolbar"><button class="primary">Add Advertisement</button><button>View</button><button>Edit</button><button>Delete</button></div>
<div class="panel"><table><tr><th>ID</th><th>Title</th><th>Campaign</th><th>Status</th><th>Version</th></tr>
<tr><td>1</td><td>Summer Banner</td><td>Summer Launch</td><td><span class="badge">Pending Client Review</span></td><td>v1</td></tr></table></div></main></div></body></html>