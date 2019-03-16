package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
