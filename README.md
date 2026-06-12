# 🍜 在线点餐系统

基于 **Java Servlet + JSP + H2** 的 B/S 架构在线餐厅点餐系统。  
**零配置启动**：内置嵌入式数据库和 Tomcat，一行命令即可运行。

[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36)](https://maven.apache.org/)
[![Tomcat](https://img.shields.io/badge/Tomcat-9.0-yellow)](https://tomcat.apache.org/)
[![H2](https://img.shields.io/badge/H2-Embedded-blue)](https://h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](./LICENSE)

---

## ✨ 功能

| 角色 | 功能 |
|------|------|
| 🧑 普通用户 | 菜单浏览 · 分类筛选 · 关键词搜索 · 购物车 · 提交订单 · 查看订单 |
| 👑 管理员 | 菜品 CRUD · 上架/下架 · 订单状态流转 · 查看所有订单 |

### 订单状态流转

```
待支付 ──→ 已支付 ──→ 制作中 ──→ 已完成
  │                                   
  └──────────→ 已取消
```

---

## 🖼️ 功能截图

> 💡 启动后运行 `mvn tomcat7:run`，打开 http://localhost:8080 即可体验全部功能。欢迎提交截图 PR！

| 页面 | 说明 |
|------|------|
| 登录页 | 输入用户名密码，支持管理员/普通用户双角色 |
| 菜单浏览 | 分类筛选 + 搜索 + 数量选择 + 一键加入购物车 |
| 购物车 | 查看已选菜品、修改数量、删除、提交订单 |
| 我的订单 | 查看订单列表与状态 |
| 后台 · 菜品管理 | 菜品增删改查、上架/下架 |
| 后台 · 订单管理 | 订单列表、状态管理 |

---

## 🚀 快速启动

### 环境要求
- **JDK 17+**
- **Maven 3.6+**

### 一键启动
```bash
mvn clean tomcat7:run
```

浏览器打开 👉 **http://localhost:8080**

### 体验账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `admin123` |
| 普通用户 | `test` | `test123` |

---

## 🏗️ 技术架构

```
┌─────────────────────────────────────────────────┐
│                    Browser                       │
├─────────────────────────────────────────────────┤
│     JSP / JSTL / HTML / CSS （视图层）            │
├─────────────────────────────────────────────────┤
│              Filter （编码 + 登录拦截）             │
├─────────────────────────────────────────────────┤
│         8 个 Servlet （控制器层）                   │
│   Login · Register · Menu · Cart · Order        │
│          AdminDish · AdminOrder · Logout         │
├─────────────────────────────────────────────────┤
│         3 个 Service （业务逻辑层）                  │
│     UserService · DishService · OrderService    │
├─────────────────────────────────────────────────┤
│         3 个 DAO （数据访问层）                      │
│       UserDAO · DishDAO · OrderDAO              │
├─────────────────────────────────────────────────┤
│         DBPool （连接池 + 自动建表初始化）            │
├─────────────────────────────────────────────────┤
│              H2 Embedded Database                │
│         users · categories · dishes              │
│           orders · order_items                   │
└─────────────────────────────────────────────────┘
```

### 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| 前端 | JSP + JSTL + HTML/CSS | 服务端渲染，响应式布局 |
| 后端 | Java Servlet 4.0 | 8 个 Servlet 覆盖全部业务 |
| 数据库 | **H2 Embedded** | 零安装，文件存储，兼容 MySQL 语法 |
| 安全 | SHA-256 + Filter | 密码哈希存储，登录拦截器 |
| 构建 | Maven + tomcat7-maven-plugin | 一键编译运行 |
| 架构 | **MVC 分层** | Servlet → Service → DAO → DB |

---

## 🗄️ 数据库设计（E-R 图）

```
  ┌──────────┐        ┌──────────────┐
  │   users  │        │  categories  │
  │──────────│        │──────────────│
  │ id       │        │ id           │
  │ username │        │ name         │
  │ password │        └──────┬───────┘
  │ role     │               │
  │ phone    │        ┌──────▼───────┐
  │ address  │        │    dishes    │
  │ created  │        │──────────────│
  └────┬─────┘        │ id           │
       │              │ category_id ─┼──→ categories.id
       │              │ name         │
       │              │ price        │
       │              │ description  │
       │              │ status       │
       │              └──────┬───────┘
       │                     │
  ┌────▼──────┐       ┌──────▼──────────┐
  │   orders  │       │   order_items   │
  │───────────│       │─────────────────│
  │ id        │◄──────│ order_id        │
  │ user_id ──┼──┐    │ dish_id ────────┼──→ dishes.id
  │ total     │  │    │ dish_name       │
  │ status    │  │    │ price           │
  │ address   │  │    │ quantity        │
  │ remark    │  │    └─────────────────┘
  │ created   │  │
  └───────────┘  │
                 └──→ users.id
```

6 张业务表：`users` · `categories` · `dishes` · `orders` · `order_items`  
购物车使用 **Session** 存储，无需持久化。

---

## 📁 项目结构

```
restaurant-web/
├── pom.xml                              # Maven 配置
├── Dockerfile                           # 云端 Docker 部署
├── render.yaml                          # Render 一键部署
├── sql/init.sql                         # MySQL 初始化（参考用）
└── src/main/
    ├── java/com/restaurant/
    │   ├── servlet/                     # 控制器（8 个 Servlet）
    │   │   ├── LoginServlet.java        #    登录页 + 登录验证
    │   │   ├── RegisterServlet.java     #    注册
    │   │   ├── LogoutServlet.java       #    登出
    │   │   ├── MenuServlet.java         #    菜单浏览 + 搜索
    │   │   ├── CartServlet.java         #    购物车增删改
    │   │   ├── OrderServlet.java        #    用户下单 + 订单查看
    │   │   ├── AdminDishServlet.java    #    后台菜品管理
    │   │   └── AdminOrderServlet.java   #    后台订单管理
    │   ├── service/                     # 业务逻辑（3 个 Service）
    │   ├── dao/                         # 数据访问（3 个 DAO）
    │   ├── model/                       # 实体类（6 个 Model）
    │   ├── db/DBPool.java               # 连接池 + 自动建表
    │   ├── filter/                      # 编码 / 登录检查 过滤器
    │   └── util/Util.java               # SHA-256 哈希 + 输入工具
    └── webapp/
        ├── css/style.css                # 全局样式
        ├── index.jsp                    # 入口 → 自动跳转登录
        └── pages/
            ├── login.jsp                # 登录页面
            ├── register.jsp             # 注册页面
            ├── user/
            │   ├── menu.jsp             # 菜单浏览 + 加购
            │   ├── cart.jsp             # 购物车
            │   └── orders.jsp           # 我的订单
            └── admin/
                ├── dishes.jsp           # 菜品管理
                └── orders.jsp           # 订单管理
```

---

## 📐 软件工程文档

本项目配套完整的《软件工程》实验报告（约 20 页），涵盖：

| 阶段 | 产出 |
|------|------|
| 可行性分析 | 经济可行性 · 技术可行性 · 操作可行性 · 社会可行性 |
| 需求分析 | 功能需求（用户/管理员）· 非功能需求 · 数据流分析 |
| 系统设计 | 架构图 · UML 类图 · E-R 图 · 功能结构图 |
| 详细设计 | N-S 流程图 · 顺序图 · 活动图（含泳道） |
| 软件测试 | 16 项黑盒测试用例 + 测试结果 |

---

## 📄 许可

MIT License — 自由使用、修改和分发。
