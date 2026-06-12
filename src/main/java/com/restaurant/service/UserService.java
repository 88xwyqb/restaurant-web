package com.restaurant.service;

import com.restaurant.dao.UserDAO;
import com.restaurant.model.User;
import com.restaurant.util.Util;

public class UserService {
    private final UserDAO dao = new UserDAO();

    /** 登录，返回User或null */
    public User login(String username, String password) {
        return dao.login(username, Util.sha256(password));
    }

    /** 注册，返回false=用户名已存在 */
    public boolean register(String username, String password, String phone, String address) {
        if (username.isBlank() || password.length() < 6) return false;
        User u = new User(username, Util.sha256(password), 0);
        u.setPhone(phone);
        u.setAddress(address);
        return dao.register(u);
    }

    /** 更新个人信息 */
    public void updateProfile(User user, String phone, String address) {
        user.setPhone(phone);
        user.setAddress(address);
        dao.updateProfile(user);
    }
}
