package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "quote")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id")
    private Long quoteId;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "manufacture", length = 100)
    private String manufacture;

    @Column(name = "payment_type", length = 100)
    private String paymentType;

    @Column(name = "other_payment", length = 100)
    private String otherPayment;

    @Column(name = "delivery_type", length = 100)
    private String deliveryType;

    @Column(name = "comments", length = 100)
    private String comments;

    @Column(name = "partial_delivery", length = 100)
    private String partialDelivery;

    @Column(name = "delivery_location", length = 100)
    private String deliveryLocation;

    @Column(name = "invoice", length = 100)
    private String invoice;

    @Column(name = "signature", length = 100)
    private String signature;

    @Column(name = "status")
    private Integer status;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private Set<QuoteDetail> quoteDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private Set<QuoteHistory> quoteHistories;
}
