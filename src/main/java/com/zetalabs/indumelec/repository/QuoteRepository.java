package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT DISTINCT q FROM Quote q INNER JOIN q.quoteHistories qh WHERE qh.status not in (?1)")
    List<Quote> getQuotesNotIn(List<Integer> statusList);
}
