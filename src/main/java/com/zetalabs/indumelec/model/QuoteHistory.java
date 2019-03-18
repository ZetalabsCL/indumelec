package com.zetalabs.indumelec.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zetalabs.indumelec.model.types.Status;
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
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}