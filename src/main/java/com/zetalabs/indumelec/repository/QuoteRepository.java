package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT q FROM Quote q WHERE q.status not in (3,8)")
    List<Quote> getInProgressQuotes();

    @Query("SELECT count(q) FROM Quote q WHERE q.entryDate <= ?1 and q.status in (?2)")
    Long getCountOfQuotesByStatusIn(LocalDateTime requestDate, List<Integer> statusList);

    @Query("SELECT count(q) FROM Quote q WHERE q.entryDate <= ?1 and q.status not in (?2)")
    Long getCountOfQuotesByStatusNotIn(LocalDateTime requestDate, List<Integer> statusList);

    @Query("SELECT sum(q.amount) FROM Quote q WHERE q.entryDate between ?1 and ?2")
    Double getQuoteAmountByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sum(q.amount) FROM Quote q WHERE q.entryDate = ?1")
    Double getQuoteAmountByEntryDate(LocalDateTime entryDate);

    @Query("SELECT count(q) FROM Quote q WHERE q.entryDate between ?1 and ?2")
    Long getCountOfQuotesByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT count(q) FROM Quote q WHERE q.entryDate = ?1")
    Long getCountOfQuotesByEntryDate(LocalDateTime entryDate);
}
