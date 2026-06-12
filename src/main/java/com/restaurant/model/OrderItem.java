package com.restaurant.model;

import java.math.BigDecimal;

public class OrderItem {
    private int        id;
    private int        orderId;
    private int        dishId;
    private String     dishName;
    private BigDecimal price;
    private int        quantity;

    public OrderItem() {}
    public OrderItem(int dishId, String dishName, BigDecimal price, int quantity) {
        this.dishId   = dishId;
        this.dishName = dishName;
        this.price    = price;
        this.quantity = quantity;
    }

    public int        getId()           { return id; }
    public void       setId(int id)     { this.id = id; }
    public int        getOrderId()      { return orderId; }
    public void       setOrderId(int o) { this.orderId = o; }
    public int        getDishId()       { return dishId; }
    public void       setDishId(int d)  { this.dishId = d; }
    public String     getDishName()     { return dishName; }
    public void       setDishName(String n) { this.dishName = n; }
    public BigDecimal getPrice()        { return price; }
    public void       setPrice(BigDecimal p) { this.price = p; }
    public int        getQuantity()     { return quantity; }
    public void       setQuantity(int q) { this.quantity = q; }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return String.format("  %-16s x%-3d ¥%.2f", dishName, quantity, getSubtotal());
    }
}
