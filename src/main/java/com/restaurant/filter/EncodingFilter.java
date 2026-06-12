package com.restaurant.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 全局UTF-8编码过滤器，解决中文乱码
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        chain.doFilter(req, resp);
    }
}
