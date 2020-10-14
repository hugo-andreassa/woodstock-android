package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class AddressModel implements Serializable {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String number;
    private String comp;
    private String cep;

    public AddressModel() {

    }

    public AddressModel(Long id, String street, String city, String state,
                        String number, String comp, String cep) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.number = number;
        this.comp = comp;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
