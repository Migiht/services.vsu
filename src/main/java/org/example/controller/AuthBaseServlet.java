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

/**
 * A base servlet that checks for @AuthRequired annotation to enforce authentication and authorization.
 * Servlets that require protection should extend this class.
 */
public abstract class AuthBaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthRequired auth = this.getClass().getAnnotation(AuthRequired.class);

        // This check should not be null for any servlet extending this base class,
        // but as a safeguard, we proceed to super.service() if no annotation is found.
        if (auth == null) {
            super.service(req, resp);
            return;
        }

        User user = (req.getSession(false) != null) ? (User) req.getSession(false).getAttribute("user") : null;

        // 1. Check if user is logged in
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 2. Check if specific roles are required
        Set<String> requiredRoles = Arrays.stream(auth.roles()).collect(Collectors.toSet());
        if (!requiredRoles.isEmpty() && !requiredRoles.contains(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
            return;
        }

        // If all checks pass, proceed with the actual servlet logic
        super.service(req, resp);
    }
} 