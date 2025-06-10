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

@WebServlet("/editProgrammer")
@AuthRequired(roles = {"MANAGER"})
public class EditProgrammerServlet extends AuthBaseServlet {
    private ProgrammerDao programmerDao;
    private ProjectDao projectDao;

    @Override
    public void init() {
        programmerDao = new ProgrammerDao();
        projectDao = new ProjectDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long programmerId = Long.parseLong(req.getParameter("programmerId"));
            Programmer programmer = programmerDao.getProgrammerById(programmerId);
            if (programmer != null) {
                req.setAttribute("programmer", programmer);
                req.getRequestDispatcher("/WEB-INF/views/edit-programmer.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Programmer not found.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid programmer ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching programmer for edit.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long programmerId = Long.parseLong(req.getParameter("programmerId"));
        long projectId = Long.parseLong(req.getParameter("projectId"));

        try {
            Project project = projectDao.getProjectById(projectId);
            if (project == null) {
                throw new ServletException("Associated project not found.");
            }

            LocalDate programmerStartDate = LocalDate.parse(req.getParameter("startDate"));
            String endDateParam = req.getParameter("endDate");
            LocalDate programmerEndDate = (endDateParam == null || endDateParam.isEmpty()) ? null : LocalDate.parse(endDateParam);

            // Validation logic copied from AddProgrammerServlet
            boolean isInvalid = false;
            if (programmerStartDate.isBefore(project.getStartDate())) isInvalid = true;
            if (project.getEndDate() != null && programmerEndDate != null && programmerEndDate.isAfter(project.getEndDate())) isInvalid = true;
            if (programmerEndDate != null && programmerEndDate.isBefore(programmerStartDate)) isInvalid = true;
            
            if (isInvalid) {
                String errorUrl = String.format("%s/editProgrammer?programmerId=%d&error=date", req.getContextPath(), programmerId);
                resp.sendRedirect(errorUrl);
                return;
            }

            Programmer programmer = new Programmer();
            programmer.setId(programmerId);
            programmer.setProjectId(projectId);
            programmer.setFirstName(req.getParameter("firstName"));
            programmer.setLastName(req.getParameter("lastName"));
            programmer.setPatronymic(req.getParameter("patronymic"));
            programmer.setPosition(req.getParameter("position"));
            programmer.setStartDate(programmerStartDate);
            programmer.setEndDate(programmerEndDate);
            programmer.setHourlyRate(new BigDecimal(req.getParameter("hourlyRate")));
            programmer.setFullTime(Boolean.parseBoolean(req.getParameter("fullTime")));

            programmerDao.updateProgrammer(programmer);
            resp.sendRedirect(req.getContextPath() + "/programmers?projectId=" + projectId);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while updating programmer.", e);
        }
    }
} 