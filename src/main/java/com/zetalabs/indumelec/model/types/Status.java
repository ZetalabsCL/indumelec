package com.zetalabs.indumelec.model.types;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Status {
    NEW("Nueva"),
    REVIEW("En Revision"),
    APPROVED("Aprobada"),
    REJECTED("Rechazada"),
    PROJECT("proyecto", "Proyecto"),
    CUT("corte-plegado", "Corte - Plegado"),
    PRODUCTION("produccion", "Produccion"),
    BUILD("armado", "Armado"),
    DELIVERY("despacho", "Despacho"),
    COMPLETED("Completada"),
    RETURNED("Regresada"),
    UPDATE_DELIVERY("Fecha Entrega Actualizada"),
    UPDATE_PRIORITY("Prioridad Actualizada"),
    COMMENTED("Comentario Agregado");

    private String description;
    private String id;

    Status(String description){
        this.id = StringUtils.EMPTY;
        this.description=description;
    }

    Status(String id, String description){
        this.id = id;
        this.description=description;
    }
}
