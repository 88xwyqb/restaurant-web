package com.restaurant.servlet;
import com.restaurant.model.*;
import com.restaurant.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
/**
 * 用户订单操作
 * GET  /user/order          查看我的订单列表
 * POST action=checkout      结算下单
 * POST action=pay           支付订单
 * POST action=cancel        取消订单
 */
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user   = (User) req.getSession().getAttribute("loginUser");
        List<Order> orders = orderService.getUserOrders(user.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/pages/user/orders.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user   = (User) req.getSession().getAttribute("loginUser");
        String action = req.getParameter("action");

        switch (action == null ? "" : action) {
            case "checkout" -> doCheckout(req, resp, user);
            case "pay"      -> doPay(req, resp, user);
            case "cancel"   -> doCancel(req, resp, user);
            default         -> resp.sendRedirect(req.getContextPath() + "/user/order");
        }
    }
    /** 提交订单 */
    private void doCheckout(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException, ServletException {
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/user/menu");
            return;
        }
        String address = req.getParameter("address");
        String remark  = req.getParameter("remark");
        if (address == null || address.trim().isEmpty()) {
            address = user.getAddress();
        }
        if (address == null || address.trim().isEmpty()) {
            req.setAttribute("error", "请填写收货地址");
            req.getRequestDispatcher("/pages/user/cart.jsp").forward(req, resp);
            return;
        }
        try {
            Order order = orderService.placeOrder(user, cart, address.trim(), remark);
            req.getSession().setAttribute("cart", null);        // 清空购物车
            resp.sendRedirect(req.getContextPath()
                    + "/user/order?msg=placed&orderId=" + order.getId());
        } catch (Exception e) {
            req.setAttribute("error", "下单失败：" + e.getMessage());
            req.getRequestDispatcher("/pages/user/cart.jsp").forward(req, resp);
        }
    }
    /** 支付订单 */
    private void doPay(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        orderService.payOrder(orderId, user.getId());
        resp.sendRedirect(req.getContextPath() + "/user/order?msg=paid");
    }
    /** 取消订单 */
    private void doCancel(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        orderService.cancelOrder(orderId, user.getId());
        resp.sendRedirect(req.getContextPath() + "/user/order?msg=cancelled");
    }
}
