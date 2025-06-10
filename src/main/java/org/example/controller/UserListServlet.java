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
import java.util.List;

@WebServlet("/users")
@AuthRequired(roles = {"ADMIN"})
public class UserListServlet extends AuthBaseServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = userDao.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/views/user-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error fetching user list", e);
        }
    }
} 