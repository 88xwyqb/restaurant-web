<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 — 在线点餐系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-box">
        <div class="auth-title">🍜 在线点餐系统</div>
        <div class="auth-subtitle">欢迎回来，请登录您的账号</div>

        <%-- 错误提示 --%>
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">${error}</div>
        <% } %>
        <%-- 注册成功提示 --%>
        <% if ("registered".equals(request.getParameter("msg"))) { %>
        <div class="alert alert-success">注册成功，请登录！</div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label>用户名</label>
                <input class="form-control" type="text" name="username"
                       placeholder="请输入用户名" required autofocus>
            </div>
            <div class="form-group">
                <label>密码</label>
                <input class="form-control" type="password" name="password"
                       placeholder="请输入密码" required>
            </div>
            <button class="btn btn-primary" style="width:100%;margin-top:8px" type="submit">
                登 录
            </button>
        </form>

        <div style="text-align:center;margin-top:18px;font-size:14px;color:#999">
            还没有账号？
            <a class="auth-link" href="${pageContext.request.contextPath}/register">立即注册</a>
        </div>
    </div>
</div>
</body>
</html>
