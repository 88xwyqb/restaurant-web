package com.restaurant.db;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * 数据库连接池（简易实现）
 */
public class DBPool {

    private static final BlockingQueue<Connection> pool;
    private static String url, username, password;

    static {
        try {
            Properties props = new Properties();
            // 读取配置文件路径，和文件位置完全匹配
            InputStream is = DBPool.class.getClassLoader()
                    .getResourceAsStream("com/restaurant/db.properties");
            // 空指针兜底（防止文件找不到）
            if (is == null) {
                throw new RuntimeException("db.properties 配置文件未找到，请检查文件位置！");
            }
            props.load(is);

            // 加载驱动
            Class.forName(props.getProperty("db.driver"));
            // 读取配置
            url      = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            int size = Integer.parseInt(props.getProperty("db.pool.size", "10"));

            // 初始化连接池
            pool = new ArrayBlockingQueue<>(size);
            for (int i = 0; i < size; i++) {
                pool.offer(DriverManager.getConnection(url, username, password));
            }
            // H2 自动建表 + 初始数据
            initDatabase();
            System.out.println("✅ 数据库连接池初始化成功！");
        } catch (Exception e) {
            e.printStackTrace(); // 这行会在控制台输出最原始的错误，比如 "File Not Found" 或者 "Access Denied"
            throw new ExceptionInInitializerError(e);
        }
    }
    /** 从连接池获取连接（阻塞等待） */
    public static Connection getConnection() {
        try {
            Connection conn = pool.take();
            // 若连接已失效则重新创建
            if (!conn.isValid(2)) {
                conn = DriverManager.getConnection(url, username, password);
            }
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }
    /** 归还连接到连接池 */
    public static void release(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }
    /** 关闭资源（ResultSet, Statement, Connection不归还时用） */
    public static void close(ResultSet rs, Statement st, Connection conn) {
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (st   != null) st.close();   } catch (SQLException ignored) {}
        if (conn != null) release(conn);
    }

    /** 计算 SHA-256（用于初始化密码，避免硬编码哈希） */
    private static String sha256(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /** H2 自动建表 + 初始数据（首次运行时执行） */
    private static void initDatabase() {
        Connection conn = getConnection();
        try (Statement st = conn.createStatement()) {
            // 检查是否已初始化
            ResultSet rs = conn.getMetaData().getTables(null, null, "USERS", null);
            if (rs.next()) { rs.close(); release(conn); return; }
            rs.close();

            System.out.println("🔧 首次运行，初始化数据库...");

            st.execute("CREATE TABLE users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(64) NOT NULL," +
                "role TINYINT NOT NULL DEFAULT 0," +
                "phone VARCHAR(20)," +
                "address VARCHAR(200)," +
                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            st.execute("CREATE TABLE categories (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL UNIQUE)");

            st.execute("CREATE TABLE dishes (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "category_id INT NOT NULL," +
                "name VARCHAR(100) NOT NULL," +
                "price DECIMAL(8,2) NOT NULL," +
                "description VARCHAR(300)," +
                "status TINYINT NOT NULL DEFAULT 1," +
                "FOREIGN KEY (category_id) REFERENCES categories(id))");

            st.execute("CREATE TABLE orders (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "user_id INT NOT NULL," +
                "total_price DECIMAL(10,2) NOT NULL," +
                "status TINYINT NOT NULL DEFAULT 0," +
                "address VARCHAR(200)," +
                "remark VARCHAR(300)," +
                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

            st.execute("CREATE TABLE order_items (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "order_id INT NOT NULL," +
                "dish_id INT NOT NULL," +
                "dish_name VARCHAR(100) NOT NULL," +
                "price DECIMAL(8,2) NOT NULL," +
                "quantity INT NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES orders(id)," +
                "FOREIGN KEY (dish_id) REFERENCES dishes(id))");

            // 种子数据 — 密码通过 sha256() 运行时计算
            st.execute("INSERT INTO users(username, password, role) VALUES " +
                "('admin', '" + sha256("admin123") + "', 1)," +
                "('test',  '" + sha256("test123")  + "', 0)");

            st.execute("INSERT INTO categories(name) VALUES ('主食'),('热菜'),('凉菜'),('汤品'),('饮品')");

            st.execute("INSERT INTO dishes(category_id, name, price, description) VALUES " +
                "(1, '扬州炒饭',   12.00, '经典炒饭，粒粒分明')," +
                "(1, '牛肉炒面',   14.00, '嫩滑牛肉配劲道面条')," +
                "(2, '宫保鸡丁',   22.00, '花生米配鸡肉，微辣下饭')," +
                "(2, '鱼香肉丝',   20.00, '酸甜咸鲜，家常风味')," +
                "(2, '麻婆豆腐',   16.00, '麻辣鲜香，豆腐嫩滑')," +
                "(2, '红烧排骨',   35.00, '软烂入味，老少皆宜')," +
                "(3, '拍黄瓜',      8.00, '清爽开胃')," +
                "(3, '凉拌木耳',   10.00, '爽脆可口')," +
                "(4, '番茄蛋花汤', 10.00, '酸甜开胃，营养丰富')," +
                "(4, '紫菜虾皮汤',  8.00, '鲜美清淡')," +
                "(5, '可乐',        4.00, '冰镇可口可乐')," +
                "(5, '柠檬水',      6.00, '现榨柠檬，清爽解腻')");

            System.out.println("✅ 数据库初始化完成！默认账号请看 README.md");
        } catch (SQLException e) {
            throw new RuntimeException("数据库初始化失败", e);
        } finally {
            release(conn);
        }
    }
}