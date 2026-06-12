package com.restaurant.model;

import java.time.LocalDateTime;

public class User {
    private int    id;
    private String username;
    private String password;   // 存储SHA-256哈希
    private int    role;       // 0=用户  1=管理员
    private String phone;
    private String address;
    private LocalDateTime createdAt;

    public User() {}
    public User(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    // Getters & Setters
    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }
    public String getUsername()    { return username; }
    public void   setUsername(String u) { this.username = u; }
    public String getPassword()    { return password; }
    public void   setPassword(String p) { this.password = p; }
    public int    getRole()        { return role; }
    public void   setRole(int r)   { this.role = r; }
    public String getPhone()       { return phone; }
    public void   setPhone(String p) { this.phone = p; }
    public String getAddress()     { return address; }
    public void   setAddress(String a) { this.address = a; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void   setCreatedAt(LocalDateTime t) { this.createdAt = t; }

    public boolean isAdmin() { return role == 1; }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', role=%s}",
                id, username, isAdmin() ? "管理员" : "普通用户");
    }
}
