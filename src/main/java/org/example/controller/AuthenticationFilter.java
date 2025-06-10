package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No init required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 1. Set character encoding for all requests and responses
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 2. Authorization is now handled by the AuthBaseServlet using @AuthRequired annotations.
        // This filter's primary role is now to set encoding.
        // Public access to resources like /projects, /login, static files, etc., is implicitly handled
        // because their corresponding servlets do not extend AuthBaseServlet.
        // Direct access to sensitive JSPs is prevented by placing them in the /WEB-INF/ directory.
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No destroy required
    }
} 