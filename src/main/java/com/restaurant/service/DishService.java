package com.restaurant.service;

import com.restaurant.dao.DishDAO;
import com.restaurant.model.Category;
import com.restaurant.model.Dish;

import java.math.BigDecimal;
import java.util.List;

public class DishService {
    private final DishDAO dao = new DishDAO();

    public List<Dish>     getAllOnSale()              { return dao.findAllOnSale(); }
    public List<Dish>     getAll()                    { return dao.findAll(); }
    public List<Dish>     getByCategory(int catId)    { return dao.findByCategoryOnSale(catId); }
    public List<Dish>     search(String kw)           { return dao.searchByName(kw); }
    public Dish           getById(int id)             { return dao.findById(id); }
    public List<Category> getAllCategories()          { return dao.findAllCategories(); }

    /** 管理员：新增菜品 */
    public void addDish(int catId, String name, BigDecimal price, String desc) {
        Dish d = new Dish();
        d.setCategoryId(catId);
        d.setName(name);
        d.setPrice(price);
        d.setDescription(desc);
        d.setStatus(1);
        dao.insert(d);
    }

    /** 管理员：修改菜品信息 */
    public boolean updateDish(int id, int catId, String name, BigDecimal price, String desc) {
        Dish d = dao.findById(id);
        if (d == null) return false;
        d.setCategoryId(catId);
        d.setName(name);
        d.setPrice(price);
        d.setDescription(desc);
        dao.update(d);
        return true;
    }

    /** 管理员：上架/下架 */
    public boolean toggleStatus(int dishId) {
        Dish d = dao.findById(dishId);
        if (d == null) return false;
        dao.setStatus(dishId, d.getStatus() == 1 ? 0 : 1);
        return true;
    }
}
