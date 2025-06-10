package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.User;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 1. Set encoding for all requests
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Allow public access to login, logout, viewing pages and static resources
        Set<String> publicPaths = Set.of("/login", "/login.jsp", "/logout", "/projects", "/programmers");
        if (publicPaths.contains(path) || path.startsWith("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Role-based access control
        String role = user.getRole();
        
        // Manager-specific paths
        Set<String> managerPaths = Set.of(
            "/addProject", "/deleteProject", "/editProject",
            "/addProgrammer", "/deleteProgrammer", "/editProgrammer",
            "/add-project.jsp", "/edit-project.jsp",
            "/add-programmer.jsp", "/edit-programmer.jsp"
        );

        // Admin-specific paths
        Set<String> adminPaths = Set.of(
            "/users", "/addUser", "/editUser", "/deleteUser",
            "/user-list.jsp", "/add-user.jsp", "/edit-user.jsp"
        );

        if (("MANAGER".equals(role) && managerPaths.contains(path)) || 
            ("ADMIN".equals(role) && adminPaths.contains(path))) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
} 