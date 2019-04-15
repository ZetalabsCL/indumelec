package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "quote_detail")
public class QuoteDetail implements Serializable, Comparable<QuoteDetail> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "measure", length = 30)
    private String measure;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Override
    public int compareTo(QuoteDetail other){
        return this.orderId.compareTo(other.orderId);
    }
}
