package com.restaurant.filter;

import com.restaurant.model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 登录检查过滤器
 * /user/* 需要登录，/admin/* 需要管理员权限
 */
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getSession().getAttribute("loginUser");
        String uri = request.getRequestURI();

        if (user == null) {
            // 未登录，跳转登录页
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // /admin/* 还需要管理员角色
        if (uri.contains("/admin/") && !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/menu");
            return;
        }

        chain.doFilter(req, resp);
    }
}
