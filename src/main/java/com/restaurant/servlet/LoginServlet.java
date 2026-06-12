package com.restaurant.servlet;

import com.restaurant.model.User;
import com.restaurant.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    /** GET：显示登录页面 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 已登录直接跳转
        User user = (User) req.getSession().getAttribute("loginUser");
        if (user != null) {
            redirect(req, resp, user);
            return;
        }
        req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
    }

    /** POST：处理登录表单 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
            return;
        }

        // 登录成功，存 Session
        req.getSession().setAttribute("loginUser", user);
        redirect(req, resp, user);
    }

    private void redirect(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        if (user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/admin/order");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/menu");
        }
    }
}
