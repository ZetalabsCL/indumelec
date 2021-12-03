package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum PriorityType {
    NORMAL("Normal"),
    EXPRESS("Express"),
    URGENT("Urgente");

    String description;

    PriorityType(String description){
        this.description = description;
    }
}