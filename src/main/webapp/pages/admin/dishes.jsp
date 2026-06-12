<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>菜品管理 — 后台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="brand">⚙️ 管理后台</div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/order">订单管理</a>
        <a href="${pageContext.request.contextPath}/admin/dish" class="active">菜品管理</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('确定退出？')">退出 (${sessionScope.loginUser.username})</a>
    </div>
</nav>

<div class="container">

    <%-- 新增菜品 --%>
    <div class="card">
        <div class="card-title">➕ 新增菜品</div>
        <form method="post" action="${pageContext.request.contextPath}/admin/dish"
              style="display:grid;grid-template-columns:1fr 1fr;gap:16px">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>菜品名称 *</label>
                <input class="form-control" type="text" name="name"
                       placeholder="请输入菜品名称" required>
            </div>
            <div class="form-group">
                <label>单价（元）*</label>
                <input class="form-control" type="number" name="price"
                       placeholder="如：18.00" step="0.01" min="0.01" required>
            </div>
            <div class="form-group">
                <label>所属分类 *</label>
                <select class="form-control" name="categoryId" required>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id}">${cat.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>描述（选填）</label>
                <input class="form-control" type="text" name="description"
                       placeholder="简短描述，如口味特点">
            </div>
            <div style="grid-column:1/-1">
                <button class="btn btn-primary" type="submit">添加菜品</button>
            </div>
        </form>
    </div>

    <%-- 菜品列表 --%>
    <div class="card">
        <div class="card-title">📋 菜品列表（共 ${dishes.size()} 个）</div>
        <table class="table">
            <thead>
                <tr>
                    <th>编号</th>
                    <th>菜品名称</th>
                    <th>分类</th>
                    <th>单价</th>
                    <th>描述</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="dish" items="${dishes}">
                <tr>
                    <td>#${dish.id}</td>
                    <td><strong>${dish.name}</strong></td>
                    <td>${dish.categoryName}</td>
                    <td style="color:#c0392b;font-weight:bold">
                        ¥<fmt:formatNumber value="${dish.price}" pattern="0.00"/>
                    </td>
                    <td style="color:#999;font-size:13px">
                        <c:choose>
                            <c:when test="${not empty dish.description}">${dish.description}</c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${dish.status == 1}">
                                <span class="badge badge-success">上架中</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-secondary">已下架</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <%-- 上架/下架 --%>
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/dish"
                              style="display:inline">
                            <input type="hidden" name="action" value="toggle">
                            <input type="hidden" name="id"     value="${dish.id}">
                            <button class="btn btn-sm ${dish.status==1?'btn-warning':'btn-success'}"
                                    type="submit">
                                ${dish.status == 1 ? '下架' : '上架'}
                            </button>
                        </form>

                        <%-- 修改按钮（展开内联表单） --%>
                        <button class="btn btn-secondary btn-sm"
                                onclick="toggleEdit(${dish.id})">修改</button>

                        <%-- 内联编辑表单（默认隐藏） --%>
                        <div id="edit-${dish.id}"
                             style="display:none;margin-top:10px;background:#f9f9f9;
                                    border-radius:8px;padding:14px">
                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/dish"
                                  style="display:grid;grid-template-columns:1fr 1fr;gap:10px">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id"     value="${dish.id}">
                                <div class="form-group" style="margin:0">
                                    <label style="font-size:12px">名称</label>
                                    <input class="form-control" type="text" name="name"
                                           value="${dish.name}" required>
                                </div>
                                <div class="form-group" style="margin:0">
                                    <label style="font-size:12px">单价</label>
                                    <input class="form-control" type="number" name="price"
                                           value="${dish.price}" step="0.01" min="0.01" required>
                                </div>
                                <div class="form-group" style="margin:0">
                                    <label style="font-size:12px">分类</label>
                                    <select class="form-control" name="categoryId">
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.id}"
                                                ${cat.id == dish.categoryId ? 'selected' : ''}>
                                                ${cat.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group" style="margin:0">
                                    <label style="font-size:12px">描述</label>
                                    <input class="form-control" type="text" name="description"
                                           value="${dish.description}">
                                </div>
                                <div style="grid-column:1/-1;display:flex;gap:8px">
                                    <button class="btn btn-primary btn-sm" type="submit">保存</button>
                                    <button class="btn btn-secondary btn-sm" type="button"
                                            onclick="toggleEdit(${dish.id})">取消</button>
                                </div>
                            </form>
                        </div>
                    </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
function toggleEdit(id) {
    const el = document.getElementById('edit-' + id);
    el.style.display = el.style.display === 'none' ? 'block' : 'none';
}
</script>
</body>
</html>
