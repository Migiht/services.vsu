package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class AuthBaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthRequired auth = this.getClass().getAnnotation(AuthRequired.class);

        
        
        if (auth == null) {
            super.service(req, resp);
            return;
        }

        User user = (req.getSession(false) != null) ? (User) req.getSession(false).getAttribute("user") : null;

        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        
        Set<String> requiredRoles = Arrays.stream(auth.roles()).collect(Collectors.toSet());
        if (!requiredRoles.isEmpty() && !requiredRoles.contains(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
            return;
        }

        
        super.service(req, resp);
    }
} 
