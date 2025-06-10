package org.example.controller;

import org.example.dao.ProgrammerDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteProgrammer")
public class DeleteProgrammerServlet extends HttpServlet {
    private ProgrammerDao programmerDao;

    @Override
    public void init() {
        programmerDao = new ProgrammerDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long programmerId = Long.parseLong(req.getParameter("programmerId"));
            long projectId = Long.parseLong(req.getParameter("projectId")); // For redirection

            programmerDao.deleteProgrammer(programmerId);
            
            resp.sendRedirect(req.getContextPath() + "/programmers?projectId=" + projectId);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while deleting programmer.", e);
        }
    }
} 