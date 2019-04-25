package com.zetalabs.indumelec.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "quote_document")
public class QuoteDocument implements Serializable, Comparable<QuoteDocument> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @Override
    public int compareTo(QuoteDocument other){
        return this.entryDate.compareTo(other.entryDate);
    }
}
