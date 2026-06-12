package com.restaurant.model;
import java.math.BigDecimal;
import java.util.*;
/**
 * 购物车（会话级，存内存）
 */
public class Cart {
    // dishId -> CartItem
    private final Map<Integer, CartItem> items = new LinkedHashMap<>();
    public void add(Dish dish, int qty) {
        items.merge(dish.getId(),
                new CartItem(dish, qty),
                (old, n) -> { old.addQty(qty); return old; });
    }
    public void remove(int dishId) {
        items.remove(dishId);
    }
    public void clear() {
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }
    public Collection<CartItem> getItems() {
        return items.values();
    }
    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void print() {
        if (isEmpty()) { System.out.println("  购物车为空"); return; }
        System.out.println("  ┌─────────────────────────────────────┐");
        System.out.println("  │              当前购物车              │");
        System.out.println("  ├──────────────────┬──────┬───────────┤");
        System.out.printf("  │ %-16s │ 数量 │   小计    │%n", "菜品名称");
        System.out.println("  ├──────────────────┼──────┼───────────┤");
        for (CartItem ci : items.values()) {
            System.out.printf("  │ %-16s │  %-3d │ ¥%-8.2f│%n",
                    ci.getDish().getName(), ci.getQty(), ci.getSubtotal());
        }
        System.out.println("  ├──────────────────┴──────┴───────────┤");
        System.out.printf("  │ 合计：¥%-29.2f│%n", getTotal());
        System.out.println("  └─────────────────────────────────────┘");
    }
    // ── 内部类 ──
    public static class CartItem {
        private final Dish dish;
        private int qty;

        public CartItem(Dish dish, int qty) { this.dish = dish; this.qty = qty; }
        public Dish getDish()               { return dish; }
        public int  getQty()                { return qty; }
        public void addQty(int n)           { this.qty += n; }
        public BigDecimal getSubtotal()     { return dish.getPrice().multiply(BigDecimal.valueOf(qty)); }
    }
}
