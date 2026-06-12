package com.restaurant.servlet;
import com.restaurant.model.Category;
import com.restaurant.model.Dish;
import com.restaurant.service.DishService;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
/**
 * 管理员菜品管理
 * GET              查看所有菜品
 * POST action=add  新增菜品
 * POST action=edit 修改菜品
 * POST action=toggle 上架/下架
 */
public class AdminDishServlet extends HttpServlet {

    private final DishService dishService = new DishService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Dish>     dishes     = dishService.getAll();
        List<Category> categories = dishService.getAllCategories();
        req.setAttribute("dishes",     dishes);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/pages/admin/dishes.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "add" -> {
                int    catId = Integer.parseInt(req.getParameter("categoryId"));
                String name  = req.getParameter("name").trim();
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                String desc  = req.getParameter("description");
                dishService.addDish(catId, name, price, desc);
            }
            case "edit" -> {
                int    id    = Integer.parseInt(req.getParameter("id"));
                int    catId = Integer.parseInt(req.getParameter("categoryId"));
                String name  = req.getParameter("name").trim();
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                String desc  = req.getParameter("description");
                dishService.updateDish(id, catId, name, price, desc);
            }
            case "toggle" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                dishService.toggleStatus(id);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dish");
    }
}
