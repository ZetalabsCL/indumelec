package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
    @Query("select qh from QuoteHistory qh where qh.quote.quoteId = ?1 order by qh.entryDate desc")
    List<QuoteHistory> getAllByQuoteId(Long quoteId);
}
