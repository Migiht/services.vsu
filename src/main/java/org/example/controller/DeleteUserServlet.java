package org.example.controller;

import org.example.dao.UserDao;
import org.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteUser")
@AuthRequired(roles = {"ADMIN"})
public class DeleteUserServlet extends AuthBaseServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = Long.parseLong(req.getParameter("userId"));

            // Prevent a user from deleting themselves
            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
            if (currentUser != null && currentUser.getId() == userId) {
                // Optionally add an error message
                resp.sendRedirect(req.getContextPath() + "/users?error=selfdelete");
                return;
            }

            userDao.deleteUser(userId);
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while deleting user.", e);
        }
    }
} 