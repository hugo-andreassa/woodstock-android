package com.hyperdrive.woodstock.models;

import java.io.Serializable;
import java.util.Date;

public class BudgetModel implements Serializable {

    private Long id;
    private Integer deadline;
    private String deliveryDay;
    private String creationDate;
    private String paymentMethod;
    private String status;
    private Long clientId;
    private AddressModel address;

    // private List<BudgetItem> items;

    public BudgetModel() {

    }

    public BudgetModel(Long id, Integer deadline, String deliveryDay, String creationDate,
                       String paymentMethod, String status, Long clientId, AddressModel address) {
        this.id = id;
        this.deadline = deadline;
        this.deliveryDay = deliveryDay;
        this.creationDate = creationDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.clientId = clientId;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(String deliveryDay) {
        this.deliveryDay = deliveryDay;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BudgetModel{" +
                "id=" + id +
                ", deadline=" + deadline +
                ", deliveryDay='" + deliveryDay + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", clientId=" + clientId +
                ", address=" + address +
                '}';
    }
}
