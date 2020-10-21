package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class CuttingPlanModel implements Serializable {

    private Long id;
    private Double height;
    private Double width;
    private Integer quantity;
    private String comment;
    private Long budgetItemId;

    public CuttingPlanModel() {

    }

    public CuttingPlanModel(Long id, Double height, Double width, Integer quantity, String comment, Long budgetItemId) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.quantity = quantity;
        this.comment = comment;
        this.budgetItemId = budgetItemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(Long budgetItemId) {
        this.budgetItemId = budgetItemId;
    }
}
