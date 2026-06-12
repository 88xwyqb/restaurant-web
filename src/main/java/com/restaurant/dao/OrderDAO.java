package com.restaurant.dao;

import com.restaurant.db.DBPool;
import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /** 创建订单（含订单项），返回订单ID */
    public int createOrder(Order order) {
        Connection conn = DBPool.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. 插入主订单
            String sql1 = "INSERT INTO orders(user_id,total_price,status,address,remark) VALUES(?,?,?,?,?)";
            int orderId;
            try (PreparedStatement ps = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt   (1, order.getUserId());
                ps.setBigDecimal(2, order.getTotalPrice());
                ps.setInt   (3, Order.STATUS_PENDING);
                ps.setString(4, order.getAddress());
                ps.setString(5, order.getRemark());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                orderId = keys.getInt(1);
                order.setId(orderId);
            }

            // 2. 插入订单详情
            String sql2 = "INSERT INTO order_items(order_id,dish_id,dish_name,price,quantity) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                for (OrderItem item : order.getItems()) {
                    ps.setInt   (1, orderId);
                    ps.setInt   (2, item.getDishId());
                    ps.setString(3, item.getDishName());
                    ps.setBigDecimal(4, item.getPrice());
                    ps.setInt   (5, item.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("创建订单失败", e);
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            DBPool.release(conn);
        }
    }

    /** 查询当前用户的所有订单 */
    public List<Order> findByUser(int userId) {
        String sql = "SELECT o.*, u.username FROM orders o " +
                     "JOIN users u ON o.user_id=u.id " +
                     "WHERE o.user_id=? ORDER BY o.created_at DESC";
        return queryOrders(sql, userId);
    }

    /** 查询所有订单（管理员） */
    public List<Order> findAll() {
        String sql = "SELECT o.*, u.username FROM orders o " +
                     "JOIN users u ON o.user_id=u.id ORDER BY o.created_at DESC";
        Connection conn = DBPool.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<Order> list = mapOrders(rs);
            list.forEach(o -> o.setItems(findItems(o.getId())));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    /** 查询单个订单（含详情） */
    public Order findById(int orderId) {
        String sql = "SELECT o.*, u.username FROM orders o " +
                     "JOIN users u ON o.user_id=u.id WHERE o.id=?";
        List<Order> list = queryOrders(sql, orderId);
        if (list.isEmpty()) return null;
        Order o = list.get(0);
        o.setItems(findItems(orderId));
        return o;
    }

    /** 更新订单状态 */
    public void updateStatus(int orderId, int status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status); ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    // ── 私有工具 ──────────────────────────────────────

    private List<Order> queryOrders(String sql, int param) {
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            List<Order> list = mapOrders(ps.executeQuery());
            list.forEach(o -> o.setItems(findItems(o.getId())));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    private List<Order> mapOrders(ResultSet rs) throws SQLException {
        List<Order> list = new ArrayList<>();
        while (rs.next()) {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setUserId(rs.getInt("user_id"));
            o.setUsername(rs.getString("username"));
            o.setTotalPrice(rs.getBigDecimal("total_price"));
            o.setStatus(rs.getInt("status"));
            o.setAddress(rs.getString("address"));
            o.setRemark(rs.getString("remark"));
            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) o.setCreatedAt(ts.toLocalDateTime());
            list.add(o);
        }
        return list;
    }

    private List<OrderItem> findItems(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id=?";
        Connection conn = DBPool.getConnection();
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(orderId);
                item.setDishId(rs.getInt("dish_id"));
                item.setDishName(rs.getString("dish_name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
        return items;
    }
}
