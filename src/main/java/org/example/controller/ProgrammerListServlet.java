package org.example.controller;

import org.example.dao.ProgrammerDao;
import org.example.dao.ProjectDao;
import org.example.model.Programmer;
import org.example.model.Project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/programmers")
public class ProgrammerListServlet extends HttpServlet {
    private ProgrammerDao programmerDao;
    private ProjectDao projectDao;

    @Override
    public void init() {
        programmerDao = new ProgrammerDao();
        projectDao = new ProjectDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long projectId = Long.parseLong(req.getParameter("projectId"));
        String sortBy = req.getParameter("sort");
        String order = req.getParameter("order");

        try {
            Project project = projectDao.getProjectById(projectId);
            if (project != null) {
                project.setCalculatedCost(projectDao.getProjectCost(projectId));
            }

            List<Programmer> programmers = programmerDao.getProgrammersByProjectId(projectId, sortBy, order);
            for (Programmer programmer : programmers) {
                programmer.setCalculatedSalary(programmerDao.getProgrammerSalary(programmer.getId()));
            }

            req.setAttribute("project", project);
            req.setAttribute("programmers", programmers);
            req.setAttribute("sort", sortBy);
            req.setAttribute("order", order);
            req.setAttribute("nextOrder", "asc".equalsIgnoreCase(order) ? "desc" : "asc");
            req.getRequestDispatcher("/WEB-INF/views/programmer-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching programmers", e);
        }
    }
} 