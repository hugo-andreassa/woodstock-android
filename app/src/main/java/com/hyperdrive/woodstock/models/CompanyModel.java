package com.hyperdrive.woodstock.models;

public class CompanyModel {

    private Long id;
    private String tradingName;
    private String cnpj;
    private String phone;
    private String whatsapp;
    private String email;
    private String site;
    private String logo;
    private AddressModel address;

    public CompanyModel() {
    }

    public CompanyModel(Long id, String tradingName, String cnpj, String phone, String whatsapp,
                        String email, String site, String logo, AddressModel address) {
        this.id = id;
        this.tradingName = tradingName;
        this.cnpj = cnpj;
        this.phone = phone;
        this.whatsapp = whatsapp;
        this.email = email;
        this.site = site;
        this.logo = logo;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }
}
