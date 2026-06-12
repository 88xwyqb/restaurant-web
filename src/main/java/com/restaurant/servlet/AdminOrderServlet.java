package com.restaurant.servlet;

import com.restaurant.model.Order;
import com.restaurant.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 管理员订单管理
 * GET             查看所有订单 + 营业统计
 * POST action=advance 推进订单状态
 */
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Order> orders  = orderService.getAllOrders();

        // 营业统计
        long totalCount    = orders.size();
        long doneCount     = orders.stream().filter(o -> o.getStatus() == Order.STATUS_DONE).count();
        long cancelCount   = orders.stream().filter(o -> o.getStatus() == Order.STATUS_CANCEL).count();
        BigDecimal revenue = orders.stream()
                .filter(o -> o.getStatus() != Order.STATUS_CANCEL)
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        req.setAttribute("orders",      orders);
        req.setAttribute("totalCount",  totalCount);
        req.setAttribute("doneCount",   doneCount);
        req.setAttribute("cancelCount", cancelCount);
        req.setAttribute("revenue",     revenue);
        req.getRequestDispatcher("/pages/admin/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String action = req.getParameter("action");
        if ("advance".equals(action)) {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            orderService.advanceStatus(orderId);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/order");
    }
}
