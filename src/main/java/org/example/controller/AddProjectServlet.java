package org.example.controller;

import org.example.dao.ProjectDao;
import org.example.model.Project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/addProject")
public class AddProjectServlet extends HttpServlet {
    private ProjectDao projectDao;

    @Override
    public void init() {
        projectDao = new ProjectDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String customer = req.getParameter("customer");
        LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
        String endDateParam = req.getParameter("endDate");
        LocalDate endDate = (endDateParam == null || endDateParam.isEmpty()) ? null : LocalDate.parse(endDateParam);

        if (endDate != null && endDate.isBefore(startDate)) {
            // Handle error - ideally show a message to the user
            resp.sendRedirect(req.getContextPath() + "/add-project.jsp?error=enddate");
            return;
        }

        Project project = new Project(name, customer, startDate, endDate);
        project.setCalculatedCost(BigDecimal.ZERO); // Initial cost

        try {
            projectDao.addProject(project);
            resp.sendRedirect(req.getContextPath() + "/projects");
        } catch (SQLException e) {
            throw new ServletException("Database error while adding project", e);
        }
    }
} 