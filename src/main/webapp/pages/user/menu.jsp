<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>菜单 — 在线点餐系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- 导航栏 --%>
<nav class="navbar">
    <div class="brand">🍜 在线点餐系统</div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/user/menu" class="active">菜单</a>
        <a href="${pageContext.request.contextPath}/user/cart">
            购物车
            <c:if test="${not empty sessionScope.cart}">
                <span class="cart-badge">${sessionScope.cart.items.size()}</span>
            </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/user/order">我的订单</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('确定退出？')">退出 (${sessionScope.loginUser.username})</a>
    </div>
</nav>

<div class="container">

    <%-- 搜索栏 --%>
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/user/menu">
        <input type="text" name="kw" placeholder="搜索菜品名称..."
               value="${keyword}" >
        <button class="btn btn-primary" type="submit">搜索</button>
        <c:if test="${not empty keyword}">
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/user/menu">清除</a>
        </c:if>
    </form>

    <%-- 分类筛选 --%>
    <c:if test="${empty keyword}">
    <div class="cat-bar">
        <a href="${pageContext.request.contextPath}/user/menu">
            <button class="cat-btn ${empty selectedCat ? 'active' : ''}">全部</button>
        </a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/user/menu?cat=${cat.id}">
                <button class="cat-btn ${selectedCat == cat.id ? 'active' : ''}">
                    ${cat.name}
                </button>
            </a>
        </c:forEach>
    </div>
    </c:if>

    <%-- 搜索结果提示 --%>
    <c:if test="${not empty keyword}">
        <div class="alert alert-info">
            搜索 "<strong>${keyword}</strong>" 共找到 ${dishes.size()} 个结果
        </div>
    </c:if>

    <%-- 菜品网格 --%>
    <c:choose>
        <c:when test="${empty dishes}">
            <div class="card" style="text-align:center;padding:50px;color:#999">
                暂无菜品
            </div>
        </c:when>
        <c:otherwise>
        <div class="dish-grid">
            <c:forEach var="dish" items="${dishes}">
            <div class="dish-card">
                <div class="dish-img-placeholder">🍽️</div>
                <div class="dish-body">
                    <div class="dish-name">${dish.name}</div>
                    <div class="dish-desc">
                        <c:choose>
                            <c:when test="${not empty dish.description}">${dish.description}</c:when>
                            <c:otherwise>${dish.categoryName}</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="dish-footer">
                        <span class="dish-price"><fmt:formatNumber value="${dish.price}" pattern="0.00"/></span>
                        <form method="post"
                              action="${pageContext.request.contextPath}/user/cart"
                              style="display:flex;gap:6px;align-items:center">
                            <input type="hidden" name="action"  value="add">
                            <input type="hidden" name="dishId"  value="${dish.id}">
                            <input type="number"  name="qty" value="1" min="1" max="99"
                                   style="width:52px;padding:4px 6px;border:1px solid #ddd;
                                          border-radius:4px;font-size:13px;">
                            <button class="btn btn-primary btn-sm" type="submit">加入</button>
                        </form>
                    </div>
                </div>
            </div>
            </c:forEach>
        </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>