package com.hyperdrive.woodstock.models;

import java.io.Serializable;

public class MaterialModel implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private String unit;
    
    private Long companyId;
}
