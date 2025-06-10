package org.example.dao;

import org.example.model.Programmer;
import org.example.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammerDao {

    public void addProgrammer(Programmer programmer) throws SQLException {
        String sql = "INSERT INTO programmers (project_id, last_name, first_name, patronymic, position, start_date, end_date, hourly_rate, full_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setLong(1, programmer.getProjectId());
                pstmt.setString(2, programmer.getLastName());
                pstmt.setString(3, programmer.getFirstName());
                pstmt.setString(4, programmer.getPatronymic());
                pstmt.setString(5, programmer.getPosition());
                pstmt.setDate(6, Date.valueOf(programmer.getStartDate()));
                
                if (programmer.getEndDate() != null) {
                    pstmt.setDate(7, Date.valueOf(programmer.getEndDate()));
                } else {
                    pstmt.setNull(7, Types.DATE);
                }
                
                pstmt.setBigDecimal(8, programmer.getHourlyRate());
                pstmt.setBoolean(9, programmer.isFullTime());
                
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        programmer.setId(generatedKeys.getLong(1));
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { /* log */ } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { /* log */ } }
        }
    }

    public Programmer getProgrammerById(long id) throws SQLException {
        String sql = "SELECT * FROM programmers WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractProgrammerFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Programmer> getProgrammersByProjectId(long projectId, String sortBy, String order) throws SQLException {
        // Whitelist columns to prevent SQL injection
        List<String> allowedSorts = List.of("id", "last_name", "first_name", "position", "start_date", "end_date", "hourly_rate");
        if (sortBy == null || !allowedSorts.contains(sortBy.toLowerCase())) {
            sortBy = "last_name"; // Default sort column
        }
        if (order == null || !"desc".equalsIgnoreCase(order)) {
            order = "asc"; // Default sort order
        }
        
        List<Programmer> programmers = new ArrayList<>();
        String sql = "SELECT * FROM programmers WHERE project_id = ? ORDER BY " + sortBy + " " + order;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    programmers.add(extractProgrammerFromResultSet(rs));
                }
            }
        }
        return programmers;
    }

    public List<Programmer> getAllProgrammers() throws SQLException {
        List<Programmer> programmers = new ArrayList<>();
        String sql = "SELECT * FROM programmers";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                programmers.add(extractProgrammerFromResultSet(rs));
            }
        }
        return programmers;
    }

    public void updateProgrammer(Programmer programmer) throws SQLException {
        String sql = "UPDATE programmers SET project_id = ?, last_name = ?, first_name = ?, patronymic = ?, position = ?, start_date = ?, end_date = ?, hourly_rate = ?, full_time = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setLong(1, programmer.getProjectId());
                pstmt.setString(2, programmer.getLastName());
                pstmt.setString(3, programmer.getFirstName());
                pstmt.setString(4, programmer.getPatronymic());
                pstmt.setString(5, programmer.getPosition());
                pstmt.setDate(6, Date.valueOf(programmer.getStartDate()));
                if (programmer.getEndDate() != null) {
                    pstmt.setDate(7, Date.valueOf(programmer.getEndDate()));
                } else {
                    pstmt.setNull(7, Types.DATE);
                }
                pstmt.setBigDecimal(8, programmer.getHourlyRate());
                pstmt.setBoolean(9, programmer.isFullTime());
                pstmt.setLong(10, programmer.getId());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { /* log */ } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { /* log */ } }
        }
    }

    public void deleteProgrammer(long id) throws SQLException {
        String sql = "DELETE FROM programmers WHERE id = ?";
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
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { /* log */ } }
            throw e;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { /* log */ } }
        }
    }

    public BigDecimal getProgrammerSalary(long programmerId) throws SQLException {
        String sql = "{CALL GetProgrammerSalary(?, ?)}";
        try (Connection conn = DatabaseUtil.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setLong(1, programmerId);
            cstmt.registerOutParameter(2, Types.DECIMAL);
            cstmt.execute();
            return cstmt.getBigDecimal(2);
        }
    }

    private Programmer extractProgrammerFromResultSet(ResultSet rs) throws SQLException {
        Programmer p = new Programmer();
        p.setId(rs.getLong("id"));
        p.setProjectId(rs.getLong("project_id"));
        p.setLastName(rs.getString("last_name"));
        p.setFirstName(rs.getString("first_name"));
        p.setPatronymic(rs.getString("patronymic"));
        p.setPosition(rs.getString("position"));
        p.setStartDate(rs.getDate("start_date").toLocalDate());
        
        Date endDate = rs.getDate("end_date");
        p.setEndDate(endDate == null ? null : endDate.toLocalDate());

        p.setHourlyRate(rs.getBigDecimal("hourly_rate"));
        p.setFullTime(rs.getBoolean("full_time"));
        return p;
    }
} 