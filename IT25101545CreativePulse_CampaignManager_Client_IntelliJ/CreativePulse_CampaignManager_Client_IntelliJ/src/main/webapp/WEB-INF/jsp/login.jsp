<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html><head><title>CreativePulse Login</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"></head>
<body class="login-page">
<div class="login-card">
<div class="brand">Creative<span>Pulse</span></div>
<h1>Welcome back</h1><p>Advertising Agency Management System</p>
<form action="${pageContext.request.contextPath}/campaign-manager" method="get">
<label>Email</label><input type="email" value="manager@creativepulse.com" required>
<label>Password</label><input type="password" value="password" required>
<label>Role</label>
<select><option>Campaign Manager</option><option>Client</option></select>
<button type="submit">Sign In</button>
</form>
<div class="demo-note">Demo login • Connect Spring Security/BCrypt authentication in the final integrated build.</div>
</div></body></html>
