package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
}
