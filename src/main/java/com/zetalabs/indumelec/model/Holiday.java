package com.zetalabs.indumelec.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "holiday")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Long holidayId;

    @Column(name = "name")
    private String name;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "type")
    private String type;

    @Column(name = "optional")
    private Integer optional;
}
