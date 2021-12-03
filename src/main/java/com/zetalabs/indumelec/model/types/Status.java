package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum Status {
    NEW("Nueva"),
    REVIEW("En Revision"),
    APPROVED("Aprobada"),
    REJECTED("Rechazada"),
    PROJECT("Proyecto"),
    CUT("Corte - Plegado"),
    PRODUCTION("Produccion"),
    BUILD("Armado"),
    DELIVERY("Despacho"),
    COMPLETED("Completada"),
    RETURNED("Regresada"),
    UPDATE_DELIVERY("Fecha Entrega Actualizada"),
    UPDATE_PRIORITY("Prioridad Actualizada"),
    COMMENTED("Comentario Agregado");

    private String description;

    Status(String description){
        this.description=description;
    }
}
