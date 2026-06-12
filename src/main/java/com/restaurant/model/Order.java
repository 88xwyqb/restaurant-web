package com.restaurant.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    public static final int STATUS_PENDING  = 0;  // 待支付
    public static final int STATUS_PAID     = 1;  // 已支付
    public static final int STATUS_COOKING  = 2;  // 制作中
    public static final int STATUS_DONE     = 3;  // 已完成
    public static final int STATUS_CANCEL   = 4;  // 已取消

    private int        id;
    private int        userId;
    private String     username;        // JOIN填充
    private BigDecimal totalPrice;
    private int        status;
    private String     address;
    private String     remark;
    private LocalDateTime createdAt;
    private List<OrderItem> items;      // 订单详情

    public Order() {}

    // Getters & Setters
    public int        getId()            { return id; }
    public void       setId(int id)      { this.id = id; }
    public int        getUserId()        { return userId; }
    public void       setUserId(int u)   { this.userId = u; }
    public String     getUsername()      { return username; }
    public void       setUsername(String u) { this.username = u; }
    public BigDecimal getTotalPrice()    { return totalPrice; }
    public void       setTotalPrice(BigDecimal t) { this.totalPrice = t; }
    public int        getStatus()        { return status; }
    public void       setStatus(int s)   { this.status = s; }
    public String     getAddress()       { return address; }
    public void       setAddress(String a) { this.address = a; }
    public String     getRemark()        { return remark; }
    public void       setRemark(String r) { this.remark = r; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public void       setCreatedAt(LocalDateTime t) { this.createdAt = t; }
    public List<OrderItem> getItems()    { return items; }
    public void       setItems(List<OrderItem> items) { this.items = items; }

    public String getStatusText() {
        return switch (status) {
            case STATUS_PENDING -> "待支付";
            case STATUS_PAID    -> "已支付";
            case STATUS_COOKING -> "制作中";
            case STATUS_DONE    -> "已完成";
            case STATUS_CANCEL  -> "已取消";
            default             -> "未知";
        };
    }
}
