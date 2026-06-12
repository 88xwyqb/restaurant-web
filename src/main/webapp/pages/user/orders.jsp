<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 — 在线点餐系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="brand">🍜 在线点餐系统</div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/user/menu">菜单</a>
        <a href="${pageContext.request.contextPath}/user/cart">
            购物车
            <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                <span class="cart-badge">${sessionScope.cart.items.size()}</span>
            </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/user/order" class="active">我的订单</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('确定退出？')">退出 (${sessionScope.loginUser.username})</a>
    </div>
</nav>

<div class="container">

    <%-- 操作结果提示 --%>
    <c:choose>
        <c:when test="${param.msg == 'placed'}">
            <div class="alert alert-success">
                🎉 下单成功！订单号：<strong>#${param.orderId}</strong>，请尽快支付。
            </div>
        </c:when>
        <c:when test="${param.msg == 'paid'}">
            <div class="alert alert-success">✅ 支付成功！餐厅已收到您的订单，请耐心等待。</div>
        </c:when>
        <c:when test="${param.msg == 'cancelled'}">
            <div class="alert alert-info">订单已取消。</div>
        </c:when>
    </c:choose>

    <div class="card">
        <div class="card-title">📋 我的订单</div>

        <c:choose>
            <c:when test="${empty orders}">
                <div style="text-align:center;padding:50px 0;color:#999">
                    <div style="font-size:52px;margin-bottom:16px">📋</div>
                    <div style="font-size:16px;margin-bottom:20px">暂无订单记录</div>
                    <a class="btn btn-primary"
                       href="${pageContext.request.contextPath}/user/menu">去点餐</a>
                </div>
            </c:when>
            <c:otherwise>
                <%-- 已闭合的c:forEach标签 --%>
                <c:forEach var="order" items="${orders}">
                    <div style="border:1px solid #f0f0f0;border-radius:8px;margin-bottom:18px;overflow:hidden">

                            <%-- 订单头部 --%>
                        <div style="background:#fafafa;padding:12px 18px;display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #f0f0f0">
                            <div style="font-size:13px;color:#999">
                                订单号：<strong style="color:#333">#${order.id}</strong>
                                &nbsp;&nbsp;
                                下单时间：${order.createdAt}
                            </div>
                                <%-- 状态徽章 --%>
                            <c:choose>
                                <c:when test="${order.status == 0}">
                                    <span class="badge badge-warning">待支付</span>
                                </c:when>
                                <c:when test="${order.status == 1}">
                                    <span class="badge badge-info">已支付</span>
                                </c:when>
                                <c:when test="${order.status == 2}">
                                    <span class="badge badge-primary">制作中</span>
                                </c:when>
                                <c:when test="${order.status == 3}">
                                    <span class="badge badge-success">已完成</span>
                                </c:when>
                                <c:when test="${order.status == 4}">
                                    <span class="badge badge-secondary">已取消</span>
                                </c:when>
                            </c:choose>
                        </div>

                            <%-- 订单明细 --%>
                        <div style="padding:14px 18px">
                            <c:forEach var="item" items="${order.items}">
                                <div style="display:flex;justify-content:space-between;padding:5px 0;font-size:14px;border-bottom:1px dashed #f5f5f5">
                                    <span>${item.dishName} × ${item.quantity}</span>
                                    <span style="color:#c0392b">
                                ¥<fmt:formatNumber value="${item.subtotal}" pattern="0.00"/>
                            </span>
                                </div>
                            </c:forEach>

                                <%-- 地址 & 备注 --%>
                            <div style="margin-top:10px;font-size:13px;color:#888">
                                <span>📍 ${order.address}</span>
                                <c:if test="${not empty order.remark}">
                                    &nbsp;｜ 备注：${order.remark}
                                </c:if>
                            </div>

                                <%-- 订单合计 & 操作按钮 --%>
                            <div style="display:flex;justify-content:space-between;align-items:center;margin-top:14px">
                                <div style="font-size:16px">
                                    合计：<strong style="color:#c0392b;font-size:20px">
                                    ¥<fmt:formatNumber value="${order.totalPrice}" pattern="0.00"/>
                                </strong>
                                </div>
                                <div style="display:flex;gap:8px">
                                    <c:if test="${order.status == 0}">
                                        <form method="post" action="${pageContext.request.contextPath}/user/order">
                                            <input type="hidden" name="action" value="pay">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <button class="btn btn-success btn-sm" type="submit">立即支付</button>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/user/order">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <button class="btn btn-secondary btn-sm" type="submit" onclick="return confirm('确认取消订单？')">取消订单</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach> <%-- 关键：补上闭合标签 --%>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>