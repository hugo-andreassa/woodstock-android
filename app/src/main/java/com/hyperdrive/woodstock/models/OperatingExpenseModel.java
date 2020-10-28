package com.hyperdrive.woodstock.models;

import java.io.Serializable;
import java.util.Date;

public class OperatingExpenseModel implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double value;
    private String creationDate;
    private String type;

    private Long companyId;

    public OperatingExpenseModel() {

    }

    public OperatingExpenseModel(Long id, String name, String description,
                                 Double value, String creationDate, String type, Long companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.creationDate = creationDate;
        this.type = type;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "OperatingExpenseModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", creationDate='" + creationDate + '\'' +
                ", type='" + type + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
