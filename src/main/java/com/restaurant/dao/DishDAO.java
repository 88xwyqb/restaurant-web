package com.restaurant.dao;

import com.restaurant.db.DBPool;
import com.restaurant.model.Category;
import com.restaurant.model.Dish;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {

    private static final String SELECT_DISH =
        "SELECT d.*, c.name AS cat_name FROM dishes d " +
        "JOIN categories c ON d.category_id = c.id ";

    // ── 菜品查询 ───────────────────────────────────────

    /** 查询所有上架菜品 */
    public List<Dish> findAllOnSale() {
        return query(SELECT_DISH + "WHERE d.status=1 ORDER BY d.category_id, d.id");
    }

    /** 查询所有菜品（管理员用） */
    public List<Dish> findAll() {
        return query(SELECT_DISH + "ORDER BY d.category_id, d.id");
    }

    /** 按分类查询上架菜品 */
    public List<Dish> findByCategoryOnSale(int categoryId) {
        return queryWithParam(SELECT_DISH + "WHERE d.category_id=? AND d.status=1", categoryId);
    }

    /** 按名称模糊搜索上架菜品 */
    public List<Dish> searchByName(String keyword) {
        String sql = SELECT_DISH + "WHERE d.status=1 AND d.name LIKE ?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            return mapList(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    public Dish findById(int id) {
        List<Dish> list = queryWithParam(SELECT_DISH + "WHERE d.id=?", id);
        return list.isEmpty() ? null : list.get(0);
    }

    // ── 菜品管理（管理员）───────────────────────────────

    public void insert(Dish d) {
        String sql = "INSERT INTO dishes(category_id,name,price,description,status) VALUES(?,?,?,?,?)";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, d.getCategoryId());
            ps.setString(2, d.getName());
            ps.setBigDecimal(3, d.getPrice());
            ps.setString(4, d.getDescription());
            ps.setInt   (5, d.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) d.setId(keys.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("新增菜品失败", e);
        } finally {
            DBPool.release(conn);
        }
    }

    public void update(Dish d) {
        String sql = "UPDATE dishes SET category_id=?,name=?,price=?,description=?,status=? WHERE id=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, d.getCategoryId());
            ps.setString(2, d.getName());
            ps.setBigDecimal(3, d.getPrice());
            ps.setString(4, d.getDescription());
            ps.setInt   (5, d.getStatus());
            ps.setInt   (6, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新菜品失败", e);
        } finally {
            DBPool.release(conn);
        }
    }

    /** 上架/下架 */
    public void setStatus(int dishId, int status) {
        String sql = "UPDATE dishes SET status=? WHERE id=?";
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status); ps.setInt(2, dishId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    // ── 分类 ───────────────────────────────────────────

    public List<Category> findAllCategories() {
        String sql = "SELECT * FROM categories ORDER BY id";
        Connection conn = DBPool.getConnection();
        List<Category> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
        return list;
    }

    // ── 私有工具 ───────────────────────────────────────

    private List<Dish> query(String sql) {
        Connection conn = DBPool.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return mapList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    private List<Dish> queryWithParam(String sql, int param) {
        Connection conn = DBPool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            return mapList(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBPool.release(conn);
        }
    }

    private List<Dish> mapList(ResultSet rs) throws SQLException {
        List<Dish> list = new ArrayList<>();
        while (rs.next()) {
            Dish d = new Dish();
            d.setId(rs.getInt("id"));
            d.setCategoryId(rs.getInt("category_id"));
            d.setCategoryName(rs.getString("cat_name"));
            d.setName(rs.getString("name"));
            d.setPrice(rs.getBigDecimal("price"));
            d.setDescription(rs.getString("description"));
            d.setStatus(rs.getInt("status"));
            list.add(d);
        }
        return list;
    }
}
