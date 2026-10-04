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
<h1>Billing & Payment Management</h1><div class="toolbar"><button class="primary">Add Invoice</button><button>View</button><button>Edit</button><button>Delete</button><button>Add Payment</button></div>
<div class="panel"><table><tr><th>Invoice</th><th>Campaign</th><th>Amount</th><th>Paid</th><th>Status</th></tr>
<tr><td>INV-0001</td><td>Summer Launch</td><td>Rs. 250,000</td><td>Rs. 100,000</td><td><span class="badge">Partially Paid</span></td></tr></table></div></main></div></body></html>