package org.example.controller;

import org.example.dao.ProjectDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteProject")
public class DeleteProjectServlet extends HttpServlet {
    private ProjectDao projectDao;

    @Override
    public void init() {
        projectDao = new ProjectDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long projectId = Long.parseLong(req.getParameter("projectId"));
            // Note: Deleting a project might fail if programmers reference it and DB has foreign key constraints.
            // The current schema (`ON DELETE SET NULL`) handles this by setting programmer's project_id to NULL.
            projectDao.deleteProject(projectId);
            resp.sendRedirect(req.getContextPath() + "/projects");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while deleting project.", e);
        }
    }
} 