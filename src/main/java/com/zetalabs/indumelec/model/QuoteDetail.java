package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "quote_detail")
public class QuoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "measure", length = 30)
    private String measure;

    @Column(name = "price")
    private Double price;

    @Column(name = "quote_id")
    private Long quoteId;
}
