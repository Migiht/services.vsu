package org.example.controller;

import org.example.dao.UserDao;
import org.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = Long.parseLong(req.getParameter("userId"));
            User user = userDao.getUserById(userId);
            if (user != null) {
                req.setAttribute("userToEdit", user);
                req.getRequestDispatcher("/edit-user.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching user for edit.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = Long.parseLong(req.getParameter("userId"));
            String username = req.getParameter("username");
            String role = req.getParameter("role");

            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            user.setRole(role);
            
            // Note: we don't set the password, so the updateUser method in DAO shouldn't touch it.
            // My current updateUser in DAO only changes username and role, which is correct.

            userDao.updateUser(user);
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while updating user.", e);
        }
    }
} 