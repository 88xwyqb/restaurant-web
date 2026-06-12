package com.restaurant.service;
import com.restaurant.dao.OrderDAO;
import com.restaurant.model.*;

import java.util.List;
import java.util.stream.Collectors;
public class OrderService {
    private final OrderDAO dao = new OrderDAO();
    /** 从购物车生成订单 */
    public Order placeOrder(User user, Cart cart, String address, String remark) {
        if (cart.isEmpty()) throw new IllegalStateException("购物车为空");
        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalPrice(cart.getTotal());
        order.setAddress(address != null ? address : user.getAddress());
        order.setRemark(remark);

        List<OrderItem> items = cart.getItems().stream()
                .map(ci -> new OrderItem(
                        ci.getDish().getId(),
                        ci.getDish().getName(),
                        ci.getDish().getPrice(),
                        ci.getQty()))
                .collect(Collectors.toList());
        order.setItems(items);

        int id = dao.createOrder(order);
        cart.clear();
        return dao.findById(id);
    }
    /** 用户支付订单 */
    public boolean payOrder(int orderId, int userId) {
        Order o = dao.findById(orderId);
        if (o == null || o.getUserId() != userId) return false;
        if (o.getStatus() != Order.STATUS_PENDING)  return false;
        dao.updateStatus(orderId, Order.STATUS_PAID);
        return true;
    }
    /** 用户取消订单（仅待支付可取消） */
    public boolean cancelOrder(int orderId, int userId) {
        Order o = dao.findById(orderId);
        if (o == null || o.getUserId() != userId) return false;
        if (o.getStatus() != Order.STATUS_PENDING)  return false;
        dao.updateStatus(orderId, Order.STATUS_CANCEL);
        return true;
    }
    /** 管理员推进订单状态 */
    public boolean advanceStatus(int orderId) {
        Order o = dao.findById(orderId);
        if (o == null) return false;
        int next = switch (o.getStatus()) {
            case Order.STATUS_PAID    -> Order.STATUS_COOKING;
            case Order.STATUS_COOKING -> Order.STATUS_DONE;
            default -> -1;
        };
        if (next == -1) return false;
        dao.updateStatus(orderId, next);
        return true;
    }
    public List<Order> getUserOrders(int userId)  { return dao.findByUser(userId); }
    public List<Order> getAllOrders()              { return dao.findAll(); }
    public Order       getOrderById(int orderId)  { return dao.findById(orderId); }
}
