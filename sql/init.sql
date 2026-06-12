-- 在线点餐系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS restaurant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurant_db;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(64)  NOT NULL         COMMENT 'SHA-256密码',
    role        TINYINT      NOT NULL DEFAULT 0 COMMENT '0=普通用户 1=管理员',
    phone       VARCHAR(20)           COMMENT '手机号',
    address     VARCHAR(200)          COMMENT '收货地址',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 菜品分类表
CREATE TABLE IF NOT EXISTS categories (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称'
) ENGINE=InnoDB;

-- 菜品表
CREATE TABLE IF NOT EXISTS dishes (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    category_id  INT            NOT NULL,
    name         VARCHAR(100)   NOT NULL COMMENT '菜品名称',
    price        DECIMAL(8,2)   NOT NULL COMMENT '单价',
    description  VARCHAR(300)            COMMENT '描述',
    status       TINYINT        NOT NULL DEFAULT 1 COMMENT '1=上架 0=下架',
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT            NOT NULL,
    total_price  DECIMAL(10,2)  NOT NULL COMMENT '订单总价',
    status       TINYINT        NOT NULL DEFAULT 0
                 COMMENT '0=待支付 1=已支付 2=制作中 3=已完成 4=已取消',
    address      VARCHAR(200)            COMMENT '配送地址',
    remark       VARCHAR(300)            COMMENT '备注',
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_items (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    order_id  INT           NOT NULL,
    dish_id   INT           NOT NULL,
    dish_name VARCHAR(100)  NOT NULL COMMENT '下单时菜品名快照',
    price     DECIMAL(8,2)  NOT NULL COMMENT '下单时单价快照',
    quantity  INT           NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id)  REFERENCES dishes(id)
) ENGINE=InnoDB;

-- 初始数据
INSERT IGNORE INTO users(username, password, role) VALUES
    ('admin', SHA2('admin123', 256), 1),
    ('test',  SHA2('test123',  256), 0);

INSERT IGNORE INTO categories(name) VALUES ('主食'),('热菜'),('凉菜'),('汤品'),('饮品');

INSERT IGNORE INTO dishes(category_id, name, price, description) VALUES
    (1, '扬州炒饭',   12.00, '经典炒饭，粒粒分明'),
    (1, '牛肉炒面',   14.00, '嫩滑牛肉配劲道面条'),
    (2, '宫保鸡丁',   22.00, '花生米配鸡肉，微辣下饭'),
    (2, '鱼香肉丝',   20.00, '酸甜咸鲜，家常风味'),
    (2, '麻婆豆腐',   16.00, '麻辣鲜香，豆腐嫩滑'),
    (2, '红烧排骨',   35.00, '软烂入味，老少皆宜'),
    (3, '拍黄瓜',      8.00, '清爽开胃'),
    (3, '凉拌木耳',   10.00, '爽脆可口'),
    (4, '番茄蛋花汤', 10.00, '酸甜开胃，营养丰富'),
    (4, '紫菜虾皮汤',  8.00, '鲜美清淡'),
    (5, '可乐',        4.00, '冰镇可口可乐'),
    (5, '柠檬水',      6.00, '现榨柠檬，清爽解腻');
