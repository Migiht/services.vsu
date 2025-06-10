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
@AuthRequired(roles = {"ADMIN"})
public class EditUserServlet extends AuthBaseServlet {
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
                req.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(req, resp);
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
            
            // Optional: Prevent editing username to one that already exists
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser != null && existingUser.getId() != userId) {
                req.setAttribute("error", "Username already exists.");
                req.setAttribute("userToEdit", user); // send user back to pre-fill the form
                req.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(req, resp);
                return;
            }

            userDao.updateUser(user);
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while updating user.", e);
        }
    }
} 