package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class ClientModel implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpfOrCnpj;
    private Long companyId;

    private AddressModel address;

    // private List<Budgets> budgets;

    public ClientModel() {
    }

    public ClientModel(Long id, String name, String email, String phone,
                       String cpfOrCnpj, Long companyId, AddressModel address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpfOrCnpj = cpfOrCnpj;
        this.companyId = companyId;
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpfOrCnpj() {
        return cpfOrCnpj;
    }

    public void setCpfOrCnpj(String cpfOrCnpj) {
        this.cpfOrCnpj = cpfOrCnpj;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", cpfOrCnpj='" + cpfOrCnpj + '\'' +
                ", companyId=" + companyId +
                ", address=" + address +
                '}';
    }
}
