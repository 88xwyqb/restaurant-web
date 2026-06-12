# 🍜 在线点餐系统

基于 **Java Servlet + JSP + H2** 的 B/S 架构在线餐厅点餐系统。  
**零配置启动**：内置嵌入式数据库和 Tomcat，一行命令即可运行。

---

## ✨ 功能

| 角色 | 功能 |
|------|------|
| 🧑 **普通用户** | 浏览菜单、按分类筛选、搜索菜品、加入购物车、提交订单、查看订单状态 |
| 👑 **管理员** | 菜品管理（上架/下架/增删改查）、订单管理（状态流转：待支付→已支付→制作中→已完成）、查看所有用户订单 |

---

## 🚀 快速启动

### 环境要求
- **JDK 17+**
- **Maven 3.6+**

### 一键启动
```bash
mvn clean tomcat7:run
```

启动后访问：**http://localhost:8080**

### 体验账号
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `admin123` |
| 普通用户 | `test` | `test123` |

---

## 🏗️ 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | JSP + JSTL + HTML/CSS |
| 后端 | Java Servlet |
| 数据库 | **H2**（嵌入式，零安装） |
| 构建 | Maven + Tomcat7 Maven Plugin |
| 架构 | MVC 分层（Servlet → Service → DAO → DB） |

---

## 📁 项目结构

```
restaurant-web/
├── pom.xml
├── sql/init.sql                      # MySQL 初始化脚本（参考用）
└── src/main/
    ├── java/com/restaurant/
    │   ├── servlet/                   # 控制器层（8个Servlet）
    │   │   ├── LoginServlet.java      #   登录
    │   │   ├── RegisterServlet.java   #   注册
    │   │   ├── LogoutServlet.java     #   登出
    │   │   ├── MenuServlet.java       #   菜单浏览
    │   │   ├── CartServlet.java       #   购物车
    │   │   ├── OrderServlet.java      #   用户订单
    │   │   ├── AdminDishServlet.java  #   菜品管理
    │   │   └── AdminOrderServlet.java #   订单管理
    │   ├── service/                   # 业务逻辑层
    │   ├── dao/                       # 数据访问层
    │   ├── model/                     # 实体类（User, Dish, Order, Cart...）
    │   ├── db/DBPool.java             # 数据库连接池 + 自动建表
    │   ├── filter/                    # 编码过滤器、登录检查过滤器
    │   └── util/Util.java             # SHA-256 密码哈希等工具
    └── webapp/
        ├── css/style.css
        ├── index.jsp                  # 入口（自动跳转登录）
        ├── pages/
        │   ├── login.jsp              # 登录页
        │   ├── register.jsp           # 注册页
        │   ├── user/
        │   │   ├── menu.jsp           # 菜单浏览 + 搜索 + 加购
        │   │   ├── cart.jsp           # 购物车
        │   │   └── orders.jsp         # 我的订单
        │   └── admin/
        │       ├── dishes.jsp         # 菜品管理
        │       └── orders.jsp         # 订单管理
        └── WEB-INF/web.xml
```

---

## 📝 数据库设计

6 张业务表：
- **users** — 用户（SHA-256 密码哈希）
- **categories** — 菜品分类
- **dishes** — 菜品
- **orders** — 订单
- **order_items** — 订单详情（单价快照）
- 隐含：购物车使用 **Session** 存储

---

## 📄 许可

MIT License — 自由使用、修改和分发。
