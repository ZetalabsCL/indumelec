package com.zetalabs.indumelec.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "tax_id", length = 15)
    private String taxId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
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