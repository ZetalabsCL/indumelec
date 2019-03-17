package com.zetalabs.indumelec.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quote_history")
public class QuoteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "quote_id")
    private Long quoteId;

    @Column(name = "user_id")
    private Long userId;
}