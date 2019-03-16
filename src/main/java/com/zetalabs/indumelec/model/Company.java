package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "tax_id", length = 15)
    private String taxId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "contact", length = 100)
    private String contact;

    @Column(name = "cellphone", length = 30)
    private String cellphone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "position", length = 100)
    private String position;
}