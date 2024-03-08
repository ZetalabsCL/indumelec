package com.zetalabs.indumelec.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
