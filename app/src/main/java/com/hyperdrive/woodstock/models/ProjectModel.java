package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class ProjectModel implements Serializable {

    private Long id;
    private String url;
    private String comment;
    private Long budgetItemId;

    public ProjectModel() {

    }

    public ProjectModel(Long id, String url, String comment, Long budgetItemId) {
        this.id = id;
        this.url = url;
        this.comment = comment;
        this.budgetItemId = budgetItemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "ProjectModel{" +
                "id=" + id +
                ", image='" + url + '\'' +
                ", comment='" + comment + '\'' +
                ", budgetItemId=" + budgetItemId +
                '}';
    }
}
