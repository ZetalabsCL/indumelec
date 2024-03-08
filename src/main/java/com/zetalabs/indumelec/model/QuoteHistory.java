package com.zetalabs.indumelec.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zetalabs.indumelec.model.types.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "quote_history")
public class QuoteHistory implements Serializable, Comparable<QuoteHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    @Column(name = "status")
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    @JsonManagedReference
    private Quote quote;

    @Override
    public int compareTo(QuoteHistory other){
        return this.entryDate.compareTo(other.entryDate);
    }
}