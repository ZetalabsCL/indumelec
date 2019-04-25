package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.QuoteHistory;
import com.zetalabs.indumelec.repository.QuoteHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuoteHistoryService {
    @Autowired
    private QuoteHistoryRepository quoteHistoryRepository;

    public List<QuoteHistory> getAllByQuoteId(Long quoteId){
        return quoteHistoryRepository.getAllByQuoteId(quoteId);
    }
}
