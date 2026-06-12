package com.restaurant.dao;

import com.restaurant.db.DBPool;
import com.restaurant.model.User;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {

    /** 用户名密码登录，返回null表示失败 */
    public User login(String username, String passwordHash) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("登录查询失败", e);
        } finally {
            DBPool.release(conn);
        }
    }

    /** 注册新用户，返回false表示用户名已存在 */
    public boolean register(User user) {
        // 检查用户名是否重复
        if (findByUsername(user.getUsername()) != null) return false;

        String sql = "INSERT INTO users(username,password,role,phone,address) VALUES(?,?,?,?,?)";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt   (3, user.getRole());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) user.setId(keys.getInt(1));
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("注册失败", e);
        } finally {
            DBPool.release(conn);
        }
    }

    /** 更新用户地址/手机 */
    public void updateProfile(User user) {
        String sql = "UPDATE users SET phone=?, address=? WHERE id=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPhone());
            ps.setString(2, user.getAddress());
            ps.setInt   (3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新用户信息失败", e);
        } finally {
            DBPool.release(conn);
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapUser(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getInt("role"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }
}
