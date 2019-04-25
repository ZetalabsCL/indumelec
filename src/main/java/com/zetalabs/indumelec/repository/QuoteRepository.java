package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> getQuotesByStatusEqualsOrderByDeliveryDate(Status status);

    @Query("SELECT q FROM Quote q WHERE q.status not in (0,1,2,3,8) order by q.deliveryDate")
    List<Quote> getInProgressQuotes();
}
