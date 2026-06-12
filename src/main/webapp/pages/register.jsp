<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 — 在线点餐系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-box">
        <div class="auth-title">🍜 注册新账号</div>
        <div class="auth-subtitle">创建账号，开始点餐</div>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">${error}</div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-group">
                <label>用户名 <span style="color:#c0392b">*</span></label>
                <input class="form-control" type="text" name="username"
                       placeholder="3-20位字母或数字" required minlength="3" maxlength="20">
            </div>
            <div class="form-group">
                <label>密码 <span style="color:#c0392b">*</span></label>
                <input class="form-control" type="password" name="password"
                       placeholder="至少6位" required minlength="6" id="pwd">
            </div>
            <div class="form-group">
                <label>确认密码 <span style="color:#c0392b">*</span></label>
                <input class="form-control" type="password" name="confirm"
                       placeholder="再次输入密码" required id="cpwd">
            </div>
            <div class="form-group">
                <label>手机号</label>
                <input class="form-control" type="tel" name="phone" placeholder="选填">
            </div>
            <div class="form-group">
                <label>默认收货地址</label>
                <input class="form-control" type="text" name="address" placeholder="选填，下单时可修改">
            </div>
            <button class="btn btn-primary" style="width:100%;margin-top:8px" type="submit">
                注 册
            </button>
        </form>

        <div style="text-align:center;margin-top:18px;font-size:14px;color:#999">
            已有账号？
            <a class="auth-link" href="${pageContext.request.contextPath}/login">去登录</a>
        </div>
    </div>
</div>
<script>
    document.querySelector('form').onsubmit = function() {
        if (document.getElementById('pwd').value !==
            document.getElementById('cpwd').value) {
            alert('两次密码不一致'); return false;
        }
    };
</script>
</body>
</html>
