package com.restaurant.model;

import java.math.BigDecimal;

public class Dish {
    private int        id;
    private int        categoryId;
    private String     categoryName;   // 查询时JOIN填充
    private String     name;
    private BigDecimal price;
    private String     description;
    private int        status;         // 1=上架  0=下架

    public Dish() {}

    public int        getId()              { return id; }
    public void       setId(int id)        { this.id = id; }
    public int        getCategoryId()      { return categoryId; }
    public void       setCategoryId(int c) { this.categoryId = c; }
    public String     getCategoryName()    { return categoryName; }
    public void       setCategoryName(String c) { this.categoryName = c; }
    public String     getName()            { return name; }
    public void       setName(String n)    { this.name = n; }
    public BigDecimal getPrice()           { return price; }
    public void       setPrice(BigDecimal p) { this.price = p; }
    public String     getDescription()     { return description; }
    public void       setDescription(String d) { this.description = d; }
    public int        getStatus()          { return status; }
    public void       setStatus(int s)     { this.status = s; }

    public boolean isOnSale() { return status == 1; }

    @Override
    public String toString() {
        return String.format("%-4d %-16s ¥%-7.2f [%s] %s",
                id, name, price, categoryName,
                description != null ? description : "");
    }
}
