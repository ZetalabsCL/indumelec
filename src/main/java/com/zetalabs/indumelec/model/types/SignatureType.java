package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum SignatureType {
    CASANOVA("Carlos Jara Casanova"),
    JARA("Patricio Jara"),
    BELMAR("Carlos Jara Belmar");

    String description;

    SignatureType(String description){
        this.description = description;
    }
}