package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.model.QuoteHistory;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.repository.CompanyRepository;
import com.zetalabs.indumelec.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Quote> getQuoteList(){
        return quoteRepository.getInProgressQuotes();
    }

    public List<Quote> getQuoteListByStatus(Status status){
        return quoteRepository.getQuotesByStatusEquals(status);
    }

    @Transactional
    public void saveQuote(User user, Quote quote){
        quote.setUser(user);
        quote.setEntryDate(LocalDateTime.now());
        quote.setStatus(Status.REVIEW);

        Company company = companyRepository.findByTaxId(quote.getCompany().getTaxId());

        if (company == null){
            companyRepository.save(quote.getCompany());
        } else {
            quote.setCompany(company);
        }

        quote.setLastUpdate(LocalDateTime.now());
        quote.setQuoteHistories(getInitialQuoteHistory(user));
        quote.setAmount(getQuoteAmount(quote.getQuoteDetailsList()));
        quote.setQuoteDetails(getQuoteDetails(quote.getQuoteDetailsList()));

        quoteRepository.save(quote);
    }

    private BigDecimal getQuoteAmount(List<QuoteDetail> quoteDetailList){
        BigDecimal quoteAmount = BigDecimal.ZERO;

        for (QuoteDetail detail : quoteDetailList) {
            quoteAmount = quoteAmount.add(detail.getPrice());
        }

        return quoteAmount;
    }

    private Set<QuoteDetail> getQuoteDetails(List<QuoteDetail> quoteDetailList){
        Set<QuoteDetail> quoteDetails = new HashSet<>();
        int count = 1;

        for (QuoteDetail quoteDetail : quoteDetailList){
            quoteDetail.setOrderId(count++);
            quoteDetails.add(quoteDetail);
        }

        return quoteDetails;
    }

    private Set<QuoteHistory> getInitialQuoteHistory(User loggedUser){
        Set<QuoteHistory> quoteHistories = new HashSet<>();

        quoteHistories.add(getQuoteHistory(loggedUser, Status.NEW, "Cotizacion Ingresada"));

        quoteHistories.add(getQuoteHistory(loggedUser, Status.REVIEW, "Cotizacion Enviada a Revision", 5));

        return quoteHistories;
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description){
        return getQuoteHistory(loggedUser, status, description, 0);
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description, long seconds){
        QuoteHistory quoteHistory = new QuoteHistory();
        quoteHistory.setDescription(description);
        quoteHistory.setEntryDate(LocalDateTime.now().plusSeconds(seconds));
        quoteHistory.setStatus(status);
        quoteHistory.setUser(loggedUser);

        return quoteHistory;
    }

    public void approveQuote(User loggedUser, Long quoteId){
        Quote quote = quoteRepository.getOne(quoteId);

        quote.setStatus(Status.CUT);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.APPROVED, "Cotizacion Aprobada por Cliente"));
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.CUT, "Cotizacion Enviada a Corte", 5));

        quoteRepository.save(quote);
    }

    public void rejectQuote(User loggedUser, Long quoteId){
        Quote quote = quoteRepository.getOne(quoteId);

        quote.setStatus(Status.REJECTED);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.REJECTED, "Cotizacion Rechazada por Cliente"));

        quoteRepository.save(quote);
    }
}