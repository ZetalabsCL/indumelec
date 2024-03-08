package com.zetalabs.indumelec.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "quote_detail")
public class QuoteDetail implements Serializable, Comparable<QuoteDetail> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "measure")
    @Lob
    private String measure;

    @Column(name = "notes")
    @Lob
    private String notes;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @JsonManagedReference
    private Quote quote;

    @Override
    public int compareTo(QuoteDetail other){
        return this.orderId.compareTo(other.orderId);
    }
}
