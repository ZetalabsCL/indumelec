package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.PriorityType;
import com.zetalabs.indumelec.model.types.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> getQuotesByStatusEqualsOrderByDeliveryDate(Status status);

    List<Quote> getQuotesByStatusEqualsAndPriorityTypeEqualsOrderByDeliveryDate(Status status, PriorityType priorityType);

    @Query("SELECT q FROM Quote q WHERE q.status in (4,5,6,7) order by q.deliveryDate")
    List<Quote> getInProgressQuotes();

    List<Quote> getQuotesByWorkOrder(String workOrder);
}
