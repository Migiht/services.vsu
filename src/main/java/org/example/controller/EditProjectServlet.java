package org.example.controller;

import org.example.dao.ProjectDao;
import org.example.model.Project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/editProject")
@AuthRequired(roles = {"MANAGER"})
public class EditProjectServlet extends AuthBaseServlet {
    private ProjectDao projectDao;

    @Override
    public void init() {
        projectDao = new ProjectDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long projectId = Long.parseLong(req.getParameter("projectId"));
            Project project = projectDao.getProjectById(projectId);
            if (project != null) {
                req.setAttribute("project", project);
                req.getRequestDispatcher("/WEB-INF/views/edit-project.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching project for edit.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long projectId = Long.parseLong(req.getParameter("projectId"));
            String name = req.getParameter("name");
            String customer = req.getParameter("customer");
            LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
            String endDateParam = req.getParameter("endDate");
            LocalDate endDate = (endDateParam == null || endDateParam.isEmpty()) ? null : LocalDate.parse(endDateParam);

            if (endDate != null && endDate.isBefore(startDate)) {
                // Redirect back to edit page with error
                resp.sendRedirect(req.getContextPath() + "/editProject?projectId=" + projectId + "&error=date");
                return;
            }

            Project project = new Project(name, customer, startDate, endDate);
            project.setId(projectId);

            projectDao.updateProject(project);
            resp.sendRedirect(req.getContextPath() + "/projects");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while updating project.", e);
        }
    }
} 