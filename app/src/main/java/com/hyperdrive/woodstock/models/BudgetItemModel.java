package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class BudgetItemModel implements Serializable {

    private Long id;
    private String description;
    private Double price;
    private Integer quantity;
    private String environment;
    private String status;
    private Long budgetId;

    public BudgetItemModel() {

    }

    public BudgetItemModel(Long id, String description, Double price, Integer quantity,
                           String environment, String status, Long budgetId) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.environment = environment;
        this.status = status;
        this.budgetId = budgetId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }
}
