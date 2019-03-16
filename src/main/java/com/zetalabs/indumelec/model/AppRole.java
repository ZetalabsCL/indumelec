package com.zetalabs.indumelec.model;

public enum AppRole {
    ADMIN("Administrador"),
    USER("Usuario");

    private String description;

    AppRole(String description){
        this.description=description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
