package com.restaurant.servlet;

import com.restaurant.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirm");
        String phone    = req.getParameter("phone");
        String address  = req.getParameter("address");

        // 前端校验兜底
        if (username == null || username.trim().length() < 3) {
            req.setAttribute("error", "用户名至少3位");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }
        if (password == null || password.length() < 6) {
            req.setAttribute("error", "密码至少6位");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(confirm)) {
            req.setAttribute("error", "两次密码不一致");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        boolean ok = userService.register(username.trim(), password, phone, address);
        if (!ok) {
            req.setAttribute("error", "用户名已存在，请更换");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        // 注册成功跳登录页
        resp.sendRedirect(req.getContextPath() + "/login?msg=registered");
    }
}
