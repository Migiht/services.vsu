package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project {
    private long id;
    private String name;
    private String customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal calculatedCost; 

    public Project() {
    }

    public Project(String name, String customer, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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

    public BigDecimal getCalculatedCost() {
        return calculatedCost;
    }

    public void setCalculatedCost(BigDecimal calculatedCost) {
        this.calculatedCost = calculatedCost;
    }
} 
