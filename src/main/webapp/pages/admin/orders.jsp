<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单管理 — 后台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="brand">⚙️ 管理后台</div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/order" class="active">订单管理</a>
        <a href="${pageContext.request.contextPath}/admin/dish">菜品管理</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('确定退出？')">退出 (${sessionScope.loginUser.username})</a>
    </div>
</nav>

<div class="container">

    <%-- 营业统计卡片 --%>
    <div class="stat-grid">
        <div class="stat-card blue">
            <div class="stat-val">${totalCount}</div>
            <div class="stat-label">总订单数</div>
        </div>
        <div class="stat-card green">
            <div class="stat-val">${doneCount}</div>
            <div class="stat-label">已完成</div>
        </div>
        <div class="stat-card gray">
            <div class="stat-val">${cancelCount}</div>
            <div class="stat-label">已取消</div>
        </div>
        <div class="stat-card red">
            <div class="stat-val">
                ¥<fmt:formatNumber value="${revenue}" pattern="0.00"/>
            </div>
            <div class="stat-label">累计营业额</div>
        </div>
    </div>

    <%-- 订单列表 --%>
    <div class="card">
        <div class="card-title">📦 全部订单</div>

        <c:choose>
            <c:when test="${empty orders}">
                <div style="text-align:center;padding:40px;color:#999">暂无订单</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${orders}">
                <div style="border:1px solid #f0f0f0;border-radius:8px;
                            margin-bottom:16px;overflow:hidden">

                    <%-- 订单头部 --%>
                    <div style="background:#fafafa;padding:12px 18px;
                                display:flex;align-items:center;justify-content:space-between;
                                border-bottom:1px solid #f0f0f0;flex-wrap:wrap;gap:8px">
                        <div style="font-size:13px;color:#666">
                            订单号：<strong style="color:#333">#${order.id}</strong>
                            &nbsp;｜&nbsp;
                            用户：<strong>${order.username}</strong>
                            &nbsp;｜&nbsp;
                            ${order.createdAt}
                        </div>
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

                    <%-- 菜品明细 --%>
                    <div style="padding:12px 18px">
                        <div style="font-size:13px;color:#555;margin-bottom:8px">
                            <c:forEach var="item" items="${order.items}" varStatus="s">
                                ${item.dishName}×${item.quantity}<c:if test="${!s.last}">、</c:if>
                            </c:forEach>
                        </div>

                        <div style="display:flex;align-items:center;
                                    justify-content:space-between;flex-wrap:wrap;gap:8px">
                            <div style="font-size:13px;color:#888">
                                📍 ${order.address}
                                <c:if test="${not empty order.remark}">
                                    &nbsp;｜&nbsp;备注：${order.remark}
                                </c:if>
                            </div>
                            <div style="display:flex;align-items:center;gap:14px">
                                <span style="font-size:16px">
                                    合计：<strong style="color:#c0392b">
                                        ¥<fmt:formatNumber value="${order.totalPrice}" pattern="0.00"/>
                                    </strong>
                                </span>
                                <%-- 推进状态按钮（已支付或制作中才显示） --%>
                                <c:if test="${order.status == 1 or order.status == 2}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/order">
                                        <input type="hidden" name="action"  value="advance">
                                        <input type="hidden" name="orderId" value="${order.id}">
                                        <button class="btn btn-primary btn-sm" type="submit">
                                            <c:choose>
                                                <c:when test="${order.status == 1}">开始制作</c:when>
                                                <c:when test="${order.status == 2}">完成订单</c:when>
                                            </c:choose>
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
