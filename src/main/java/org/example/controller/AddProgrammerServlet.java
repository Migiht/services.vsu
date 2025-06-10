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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/addProgrammer")
public class AddProgrammerServlet extends HttpServlet {
    private ProgrammerDao programmerDao;
    private ProjectDao projectDao;

    @Override
    public void init() {
        programmerDao = new ProgrammerDao();
        projectDao = new ProjectDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long projectId = Long.parseLong(req.getParameter("projectId"));

        try {
            Project project = projectDao.getProjectById(projectId);
            if (project == null) {
                throw new ServletException("Project not found.");
            }

            LocalDate programmerStartDate = LocalDate.parse(req.getParameter("startDate"));
            String endDateParam = req.getParameter("endDate");
            LocalDate programmerEndDate = (endDateParam == null || endDateParam.isEmpty()) ? null : LocalDate.parse(endDateParam);

            
            boolean isInvalid = false;
            
            if (programmerStartDate.isBefore(project.getStartDate())) {
                isInvalid = true;
            }
            
            if (project.getEndDate() != null && programmerEndDate != null && programmerEndDate.isAfter(project.getEndDate())) {
                isInvalid = true;
            }
            
            if (programmerEndDate != null && programmerEndDate.isBefore(programmerStartDate)) {
                isInvalid = true;
            }
            
            if (isInvalid) {
                
                String errorUrl = String.format("%s/add-programmer.jsp?projectId=%d&error=date",
                                      req.getContextPath(), projectId);
                resp.sendRedirect(errorUrl);
                return;
            }

            Programmer programmer = new Programmer();
            programmer.setProjectId(projectId);
            programmer.setFirstName(req.getParameter("firstName"));
            programmer.setLastName(req.getParameter("lastName"));
            programmer.setPatronymic(req.getParameter("patronymic"));
            programmer.setPosition(req.getParameter("position"));
            programmer.setStartDate(programmerStartDate);
            programmer.setEndDate(programmerEndDate);
            programmer.setHourlyRate(new BigDecimal(req.getParameter("hourlyRate")));
            programmer.setFullTime(Boolean.parseBoolean(req.getParameter("fullTime")));

            programmerDao.addProgrammer(programmer);

            String successUrl = String.format("%s/programmers?projectId=%d", req.getContextPath(), projectId);
            resp.sendRedirect(successUrl);

        } catch (SQLException e) {
            throw new ServletException("Database error while adding programmer", e);
        }
    }
} 
