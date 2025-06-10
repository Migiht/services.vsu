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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = userDao.getUserByUsernameAndPassword(username, password);

            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/projects");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?error=true");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
} 