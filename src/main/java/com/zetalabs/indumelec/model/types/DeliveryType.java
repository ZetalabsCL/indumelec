package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum DeliveryType {
    DEFAULT("Seleccione Entrega"),
    CUSTOMER("En oficinas del cliente"),
    DELIVERY("Despacho"),
    PICKUP("Sobre el camión en nuestras bodegas de Av. Einstein #775, Recoleta"),
    DELIVERY_ONSITE("Despacho a terreno");

    String description;

    DeliveryType(String description){
        this.description = description;
    }
}