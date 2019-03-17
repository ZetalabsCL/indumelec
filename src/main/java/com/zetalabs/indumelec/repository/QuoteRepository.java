package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> getQuotesByStatusEquals(Status status);

    Long countQuoteByEntryDateIsLessThanEqualAndStatusIn(LocalDateTime entryDate, List<Status> statusList);

    Long countQuoteByEntryDateIsLessThanEqualAndStatusNotIn(LocalDateTime entryDate, List<Status> statusList);

    Long countQuoteByEntryDateEquals(LocalDateTime entryDate);

    Long countQuoteByEntryDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT q FROM Quote q WHERE q.status not in (3,8)")
    List<Quote> getInProgressQuotes();

    @Query("SELECT sum(q.amount) FROM Quote q WHERE q.entryDate between ?1 and ?2")
    Double getQuoteAmountByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sum(q.amount) FROM Quote q WHERE q.entryDate = ?1")
    Double getQuoteAmountByEntryDate(LocalDateTime entryDate);
}
