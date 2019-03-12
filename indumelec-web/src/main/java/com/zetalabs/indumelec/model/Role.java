package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int id;
    @Column(name = "role")
    private String role;
}
