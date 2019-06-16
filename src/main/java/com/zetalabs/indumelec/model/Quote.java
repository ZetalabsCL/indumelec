package com.zetalabs.indumelec.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zetalabs.indumelec.model.types.DeliveryType;
import com.zetalabs.indumelec.model.types.InvoiceType;
import com.zetalabs.indumelec.model.types.PaymentType;
import com.zetalabs.indumelec.model.types.SignatureType;
import com.zetalabs.indumelec.model.types.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;

@Getter
@Setter
@Entity
@Table(name = "quote")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id")
    private Long quoteId;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "manufacture")
    private String manufacture;

    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "other_payment")
    private String otherPayment;

    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "comments")
    private String comments;

    @Column(name = "partial_delivery")
    private String partialDelivery;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "invoice")
    private InvoiceType invoice;

    @Column(name = "signature")
    private SignatureType signature;

    @Column(name = "status")
    private Status status;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "work_order")
    private String workOrder;

    @Column(name = "quote_code")
    private String quoteCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "contact", length = 100)
    private String contact;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "cellphone", length = 30)
    private String cellphone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "business_days", length = 50)
    private Integer businessDays;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("order_id ASC")
    @JsonBackReference
    private SortedSet<QuoteDetail> quoteDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("entry_date DESC")
    @JsonBackReference
    private SortedSet<QuoteHistory> quoteHistories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("entry_date ASC")
    @JsonBackReference
    private SortedSet<QuoteDocument> quoteDocuments;
}
