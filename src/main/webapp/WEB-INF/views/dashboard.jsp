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
<h1>Dashboard</h1><p class="muted">CreativePulse overview</p>
<div class="cards">
<div class="card"><b>Users</b><strong>24</strong></div><div class="card"><b>Active Campaigns</b><strong>12</strong></div>
<div class="card"><b>Open Tasks</b><strong>18</strong></div><div class="card"><b>Pending Payments</b><strong>5</strong></div></div>
<div class="grid"><div class="panel"><h2>All Modules</h2><p>Six separate management sections are available from the sidebar.</p></div>
<div class="panel"><h2>Quick Links</h2><a class="button" href="${pageContext.request.contextPath}/tasks">Open Tasks</a>
<a class="button" href="${pageContext.request.contextPath}/campaigns">Open Campaigns</a></div></div></main></div></body></html>