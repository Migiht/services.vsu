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
@AuthRequired(roles = {"ADMIN"})
public class AddUserServlet extends AuthBaseServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/add-user.jsp").forward(req, resp);
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
                req.setAttribute("error", "Username already exists.");
                req.getRequestDispatcher("/WEB-INF/views/add-user.jsp").forward(req, resp);
                return;
            }
            userDao.addUser(newUser);
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (SQLException e) {
            throw new ServletException("Database error while adding user.", e);
        }
    }
} 