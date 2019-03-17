package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum PaymentType {
    SPOT("Contado"),
    CHECK("Cheque"),
    CASH("Efectivo"),
    DAYS("30 Dias");

    String description;

    PaymentType(String description){
        this.description = description;
    }
}