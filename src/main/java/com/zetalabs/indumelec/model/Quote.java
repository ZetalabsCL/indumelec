package com.zetalabs.indumelec.model;

import com.zetalabs.indumelec.model.types.DeliveryType;
import com.zetalabs.indumelec.model.types.InvoiceType;
import com.zetalabs.indumelec.model.types.PaymentType;
import com.zetalabs.indumelec.model.types.SignatureType;
import com.zetalabs.indumelec.model.types.Status;
import lombok.Data;

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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

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

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "manufacture", length = 100)
    private String manufacture;

    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "other_payment", length = 100)
    private String otherPayment;

    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "comments", length = 100)
    private String comments;

    @Column(name = "partial_delivery", length = 100)
    private String partialDelivery;

    @Column(name = "delivery_location", length = 100)
    private String deliveryLocation;

    @Column(name = "invoice")
    private InvoiceType invoice;

    @Column(name = "signature")
    private SignatureType signature;

    @Column(name = "status")
    private Status status;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "work_order", length = 100)
    private String workOrder;

    @Column(name = "quote_code", length = 100)
    private String quoteCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("order_id ASC")
    private SortedSet<QuoteDetail> quoteDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("entry_date ASC")
    private SortedSet<QuoteHistory> quoteHistories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @OrderBy("entry_date ASC")
    private SortedSet<QuoteDocument> quoteDocuments;

    @Transient
    private List<QuoteDetail> quoteDetailsList;

    @Transient
    private QuoteDetail quoteDetail;

    @Transient
    private String deliveryDateStr;
}
