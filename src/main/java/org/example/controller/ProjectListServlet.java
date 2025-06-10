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
import java.util.List;

@WebServlet("/projects")
public class ProjectListServlet extends HttpServlet {
    private ProjectDao projectDao;

    @Override
    public void init() {
        projectDao = new ProjectDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sortBy = req.getParameter("sort");
        String order = req.getParameter("order");

        try {
            List<Project> projects = projectDao.getAllProjects(sortBy, order);
            for (Project project : projects) {
                project.setCalculatedCost(projectDao.getProjectCost(project.getId()));
            }
            req.setAttribute("projects", projects);
            req.setAttribute("sort", sortBy);
            req.setAttribute("order", order);
            req.setAttribute("nextOrder", "asc".equalsIgnoreCase(order) ? "desc" : "asc");
            req.getRequestDispatcher("/WEB-INF/views/project-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching projects", e);
        }
    }
} 