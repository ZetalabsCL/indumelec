package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum DeliveryType {
    CUSTOMER("En oficinas del cliente"),
    DELIVERY("Despacho"),
    PICKUP("Retiro por parte del cliente"),
    DELIVERY_ONSITE("Despacho a terreno");

    String description;

    DeliveryType(String description){
        this.description = description;
    }
}