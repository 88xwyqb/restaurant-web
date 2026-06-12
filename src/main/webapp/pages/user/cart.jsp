<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物车 — 在线点餐系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="brand">🍜 在线点餐系统</div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/user/menu">菜单</a>
        <a href="${pageContext.request.contextPath}/user/cart" class="active">
            购物车
            <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                <span class="cart-badge">${sessionScope.cart.items.size()}</span>
            </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/user/order">我的订单</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('确定退出？')">退出 (${sessionScope.loginUser.username})</a>
    </div>
</nav>

<div class="container">
    <h2 style="margin:20px 0">我的购物车</h2>

    <c:choose>
        <c:when test="${empty sessionScope.cart or empty sessionScope.cart.items}">
            <div class="card" style="text-align:center;padding:50px;color:#999">
                购物车是空的，快去选点好吃的吧~
                <br><br>
                <a href="${pageContext.request.contextPath}/user/menu" class="btn btn-primary">去点餐</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-list">
                <c:forEach var="item" items="${sessionScope.cart.items}">
                    <div class="cart-item card" style="display:flex;align-items:center;justify-content:space-between;padding:16px;margin-bottom:12px">
                        <div>
                            <div class="dish-name">${item.dish.name}</div>
                            <div class="dish-price">¥<fmt:formatNumber value="${item.dish.price}" pattern="0.00"/></div>
                        </div>
                        <div style="display:flex;align-items:center;gap:12px">
                            <form method="post" action="${pageContext.request.contextPath}/user/cart" style="display:flex;gap:6px;align-items:center">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="dishId" value="${item.dish.id}">
                                <input type="number" name="qty" value="${item.qty}" min="1" max="99"
                                       style="width:52px;padding:4px 6px;border:1px solid #ddd;border-radius:4px;font-size:13px;">
                                <button class="btn btn-primary btn-sm" type="submit">修改</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/user/cart">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="dishId" value="${item.dish.id}">
                                <button class="btn btn-danger btn-sm" type="submit" onclick="return confirm('确定删除这道菜吗？')">删除</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="cart-footer" style="display:flex;justify-content:space-between;align-items:center;padding:16px 0;border-top:1px solid #eee;margin-top:20px">
                <div class="total-price">
                    合计：¥
                    <c:set var="total" value="0"/>
                    <c:forEach var="item" items="${sessionScope.cart.items}">
                        <c:set var="total" value="${total + item.dish.price * item.qty}"/>
                    </c:forEach>
                    <fmt:formatNumber value="${total}" pattern="0.00"/>
                </div>

                <!-- ✅✅✅ 关键修复：加了隐藏地址，提交订单必成功 -->
                <form method="post" action="${pageContext.request.contextPath}/user/order">
                    <input type="hidden" name="action" value="checkout">
                    <input type="hidden" name="address" value="默认地址">
                    <input type="hidden" name="remark" value="无">
                    <button class="btn btn-primary btn-lg" type="submit">提交订单</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>