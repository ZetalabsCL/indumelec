package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role", length = 100)
    private String role;
}
