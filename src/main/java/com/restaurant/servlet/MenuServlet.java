package com.restaurant.servlet;

import com.restaurant.model.Category;
import com.restaurant.model.Dish;
import com.restaurant.service.DishService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class MenuServlet extends HttpServlet {
    private final DishService dishService = new DishService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String catParam = req.getParameter("cat");
        String keyword  = req.getParameter("kw");

        List<Dish>     dishes     = null;
        List<Category> categories = dishService.getAllCategories();

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 搜索模式
            dishes = dishService.search(keyword.trim());
            req.setAttribute("keyword", keyword.trim());
        } else if (catParam != null && !catParam.isEmpty()) {
            // 按分类筛选
            try {
                int catId = Integer.parseInt(catParam);
                dishes = dishService.getByCategory(catId);
                req.setAttribute("selectedCat", catId);
            } catch (NumberFormatException e) {
                dishes = dishService.getAllOnSale();
            }
        } else {
            // 全部上架菜品
            dishes = dishService.getAllOnSale();
        }

        req.setAttribute("dishes",     dishes);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/pages/user/menu.jsp").forward(req, resp);
    }
}
