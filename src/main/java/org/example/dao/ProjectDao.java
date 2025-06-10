package org.example.dao;

import org.example.model.Project;
import org.example.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProjectDao {

    public void addProject(Project project) throws SQLException {
        String sql = "INSERT INTO projects (name, customer, start_date, end_date) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, project.getName());
                pstmt.setString(2, project.getCustomer());
                pstmt.setDate(3, Date.valueOf(project.getStartDate()));
                if (project.getEndDate() != null) {
                    pstmt.setDate(4, Date.valueOf(project.getEndDate()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getLong(1));
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {  } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) {  } }
        }
    }

    public Project getProjectById(long id) throws SQLException {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractProjectFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Project> getAllProjects(String sortBy, String order) throws SQLException {
        
        List<String> allowedSorts = List.of("id", "name", "customer", "start_date", "end_date");
        if (sortBy == null || !allowedSorts.contains(sortBy.toLowerCase())) {
            sortBy = "id"; 
        }
        if (order == null || !"desc".equalsIgnoreCase(order)) {
            order = "asc"; 
        }
        
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY " + sortBy + " " + order;
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projects.add(extractProjectFromResultSet(rs));
            }
        }
        return projects;
    }

    public void updateProject(Project project) throws SQLException {
        String sql = "UPDATE projects SET name = ?, customer = ?, start_date = ?, end_date = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, project.getName());
                pstmt.setString(2, project.getCustomer());
                pstmt.setDate(3, Date.valueOf(project.getStartDate()));
                if (project.getEndDate() != null) {
                    pstmt.setDate(4, Date.valueOf(project.getEndDate()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }
                pstmt.setLong(5, project.getId());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {  } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) {  } }
        }
    }

    public void deleteProject(long id) throws SQLException {
        String sql = "DELETE FROM projects WHERE id = ?";
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {  } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) {  } }
        }
    }

    public BigDecimal getProjectCost(long projectId) throws SQLException {
        String sql = "{CALL GetProjectCost(?, ?)}";
        try (Connection conn = DatabaseUtil.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setLong(1, projectId);
            cstmt.registerOutParameter(2, Types.DECIMAL);
            cstmt.execute();
            return cstmt.getBigDecimal(2);
        }
    }

    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        project.setCustomer(rs.getString("customer"));
        project.setStartDate(rs.getDate("start_date").toLocalDate());
        Date endDate = rs.getDate("end_date");
        project.setEndDate(endDate == null ? null : endDate.toLocalDate());
        return project;
    }
} 
