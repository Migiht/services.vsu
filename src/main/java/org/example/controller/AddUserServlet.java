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

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password"); // In a real app, hash this!
        String role = req.getParameter("role");

        User newUser = new User(username, password, role);

        try {
            // Optional: Check if username already exists
            if (userDao.getUserByUsername(username) != null) {
                // Handle error - user exists
                resp.sendRedirect(req.getContextPath() + "/add-user.jsp?error=exists");
                return;
            }
            userDao.addUser(newUser);
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (SQLException e) {
            throw new ServletException("Database error while adding user.", e);
        }
    }
} 