package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum InvoiceType {
    JARA("Jara & Jara"),
    INDUMELEC("Indumelec");

    String description;

    InvoiceType(String description){
        this.description = description;
    }
}