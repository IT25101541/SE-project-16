<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>CreativePulse Login</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body class="login-body"><div class="login-card">
<div class="brand">Creative<span>Pulse</span></div><h2>Sign in</h2>
<p class="muted">Advertising Agency Management System</p>
<form method="post" action="${pageContext.request.contextPath}/login">
<label>Username</label><input name="username" required>
<label>Password</label><input type="password" name="password" required>
<button class="primary" type="submit">Login</button>
</form>
<p class="error">${error}</p>
<p><a href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a></p>
<p class="hint">Demo: <b>admin</b> / <b>admin123</b></p>
</div></body></html>