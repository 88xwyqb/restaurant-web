package com.restaurant.servlet;
import com.restaurant.model.Cart;
import com.restaurant.model.Dish;
import com.restaurant.service.DishService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 购物车操作
 * action=add    添加菜品
 * action=remove 移除菜品
 * action=clear  清空
 * GET           显示购物车页面
 */
public class CartServlet extends HttpServlet {
    private final DishService dishService = new DishService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/pages/user/cart.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        Cart cart = getOrCreateCart(req);

        switch (action == null ? "" : action) {
            case "add" -> {
                int dishId = Integer.parseInt(req.getParameter("dishId"));
                int qty    = Integer.parseInt(req.getParameter("qty"));
                Dish dish  = dishService.getById(dishId);
                if (dish != null && dish.isOnSale()) {
                    cart.add(dish, qty);
                }
                // 加完跳回菜单
                String referer = req.getHeader("Referer");
                resp.sendRedirect(referer != null ? referer
                        : req.getContextPath() + "/user/menu");
                return;
            }
            case "remove" -> {
                int dishId = Integer.parseInt(req.getParameter("dishId"));
                cart.remove(dishId);
            }
            case "clear" -> cart.clear();
        }

        resp.sendRedirect(req.getContextPath() + "/user/cart");
    }
    private Cart getOrCreateCart(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
