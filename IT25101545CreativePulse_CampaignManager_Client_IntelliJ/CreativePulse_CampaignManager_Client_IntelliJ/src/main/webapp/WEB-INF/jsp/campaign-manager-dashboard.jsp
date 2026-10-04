<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html><head><title>Campaign Manager Dashboard</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"></head>
<body>
<div class="layout">
<aside class="sidebar">
<div class="brand">Creative<span>Pulse</span></div>
<div class="role">CAMPAIGN MANAGER</div>
<a class="active" href="${pageContext.request.contextPath}/campaign-manager">Overview</a>
<a href="${pageContext.request.contextPath}/campaign-manager/campaigns">Campaign Management</a>
<a href="${pageContext.request.contextPath}/campaign-manager/tasks">Employee Task Management</a>
<a href="${pageContext.request.contextPath}/clients">Client Management</a>
<div class="separator"></div>
<a href="#">Advertisement Management</a><a href="#">Billing & Payment</a><a href="#">Report Management</a>
</aside>
<main class="content">
<header><div><span class="eyebrow">WORKSPACE</span><h1>Campaign Manager Dashboard</h1><p>Manage campaigns, assignments, tasks and client relationships.</p></div><div class="avatar">CM</div></header>
<section class="cards">
<div class="card"><span>CAMPAIGNS</span><strong>${campaignCount}</strong><small>Total campaigns</small></div>
<div class="card"><span>ACTIVE</span><strong>${activeCampaigns}</strong><small>Running campaigns</small></div>
<div class="card"><span>TASKS</span><strong>${taskCount}</strong><small>Employee tasks</small></div>
<div class="card"><span>CLIENTS</span><strong>${clientCount}</strong><small>Registered clients</small></div>
</section>
<section class="hero">
<div><span class="eyebrow">CAMPAIGN OPERATIONS</span><h2>Keep every campaign moving.</h2><p>Track campaign progress separately from employee task assignments.</p></div>
<div class="hero-actions"><a class="btn" href="${pageContext.request.contextPath}/campaign-manager/campaigns/new">+ New Campaign</a><a class="btn secondary" href="${pageContext.request.contextPath}/campaign-manager/tasks/new">+ Assign Task</a></div>
</section>
</main></div></body></html>
