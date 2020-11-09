package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class MaterialModel implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private Integer minimumStock;
    private String lastUpdate;
    private String unit;

    private Long companyId;

    public MaterialModel() {
    }

    public MaterialModel(Long id, String name, String description, Integer stock, Integer minimumStock, String lastUpdate, String unit, Long companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.minimumStock = minimumStock;
        this.lastUpdate = lastUpdate;
        this.unit = unit;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "MaterialModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", minimumStock=" + minimumStock +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", unit='" + unit + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
