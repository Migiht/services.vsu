package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Programmer {
    private long id;
    private long projectId;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal hourlyRate;
    private boolean fullTime;
    private BigDecimal calculatedSalary; // This is a transient field, not in DB

    public Programmer() {
    }

    public Programmer(long projectId, String lastName, String firstName, String patronymic, String position, LocalDate startDate, LocalDate endDate, BigDecimal hourlyRate, boolean fullTime) {
        this.projectId = projectId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourlyRate = hourlyRate;
        this.fullTime = fullTime;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public BigDecimal getCalculatedSalary() {
        return calculatedSalary;
    }

    public void setCalculatedSalary(BigDecimal calculatedSalary) {
        this.calculatedSalary = calculatedSalary;
    }
} 