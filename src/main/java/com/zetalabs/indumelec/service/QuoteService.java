package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteHistory;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.InvoiceType;
import com.zetalabs.indumelec.model.types.PriorityType;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.repository.CompanyRepository;
import com.zetalabs.indumelec.repository.QuoteRepository;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
@Component
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PdfGenerator pdfGenerator;

    public List<Quote> getQuoteList(){
        return quoteRepository.getInProgressQuotes();
    }

    public List<Quote> getQuoteListByStatus(Status status){
        return quoteRepository.getQuotesByStatusEqualsOrderByDeliveryDate(status);
    }

    public List<Quote> getQuoteListByStatusAndPriorityType(Status status, PriorityType priorityType){
        return quoteRepository.getQuotesByStatusEqualsAndPriorityTypeEqualsOrderByDeliveryDate(status, priorityType);
    }

    @Transactional
    public void saveQuote(User user, Quote quote){
        quote.setUser(user);
        quote.setStatus(Status.REVIEW);

        Company company = companyRepository.findByTaxId(quote.getCompany().getTaxId());

        if (company == null){
            companyRepository.save(quote.getCompany());
        } else {
            quote.setCompany(company);
        }

        quote.setLastUpdate(LocalDateTime.now());
        quote.setQuoteHistories(getInitialQuoteHistory(user));

        quoteRepository.save(quote);

        quote.setQuoteCode(getQuoteCode(quote));
        quote.setPriorityType(PriorityType.NORMAL);
        quoteRepository.save(quote);
    }

    @Transactional
    public void updateQuote(User user, Quote quote){
        Quote dbQuote = quoteRepository.getById(quote.getQuoteId());
        dbQuote.getQuoteDetails().clear();
        dbQuote.setQuoteDetails(quote.getQuoteDetails());
        dbQuote.setAmount(quote.getAmount());
        dbQuote.setPriorityType(quote.getPriorityType());
        dbQuote.getQuoteHistories().add(getQuoteHistory(user, dbQuote.getStatus(), "Cotizacion Actualizada"));

        quoteRepository.save(dbQuote);
    }

    private SortedSet<QuoteHistory> getInitialQuoteHistory(User loggedUser){
        SortedSet<QuoteHistory> quoteHistories = new TreeSet<>();

        quoteHistories.add(getQuoteHistory(loggedUser, Status.NEW, "Cotizacion Ingresada"));

        quoteHistories.add(getQuoteHistory(loggedUser, Status.REVIEW, "Cotizacion Enviada a Revision", 5));

        return quoteHistories;
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description){
        return getQuoteHistory(loggedUser, status, description, null, 0);
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description, long seconds){
        return getQuoteHistory(loggedUser, status, description, null, seconds);
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description, String comments){
        return getQuoteHistory(loggedUser, status, description, comments, 0);
    }

    private QuoteHistory getQuoteHistory(User loggedUser, Status status, String description, String comments, long seconds){
        QuoteHistory quoteHistory = new QuoteHistory();
        quoteHistory.setDescription(description);
        quoteHistory.setEntryDate(LocalDateTime.now().plusSeconds(seconds));
        quoteHistory.setStatus(status);
        quoteHistory.setComments(comments);
        quoteHistory.setUser(loggedUser);

        return quoteHistory;
    }

    public void approveQuote(User loggedUser, String workOrder, Long quoteId){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setStatus(Status.PROJECT);
        quote.setWorkOrder(workOrder);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.APPROVED, "Cotizacion Aprobada por Cliente"));
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.PROJECT, "Cotizacion Enviada a Proyecto", 5));

        quoteRepository.save(quote);
    }

    public void rejectQuote(User loggedUser, Long quoteId){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setStatus(Status.REJECTED);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.REJECTED, "Cotizacion Rechazada por Cliente"));

        quoteRepository.save(quote);
    }

    public void deliveryQuote(User loggedUser, Long quoteId, String comments){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setStatus(Status.COMPLETED);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.COMPLETED, "OT Entregada al cliente", comments));

        quoteRepository.save(quote);
    }

    public void returnQuote(User loggedUser, Long quoteId, String comments){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setStatus(Status.RETURNED);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.RETURNED, "OT devuelta por cliente", comments));

        quoteRepository.save(quote);
    }

    public void reprocessQuote(User loggedUser, Long quoteId, String comments){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setStatus(Status.REVIEW);
        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.RETURNED, "OT devuelta por cliente", comments));
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.REVIEW , "OT enviada nuevamente a revision"));

        quoteRepository.save(quote);
    }

    public void moveQuote(User loggedUser, Long quoteId, String comments, String from, String to){
        Quote quote = quoteRepository.getById(quoteId);
        Status statusFrom = Status.valueOf(from);
        Status statusTo = Status.valueOf(to);

        quote.setStatus(statusTo);
        quote.setLastUpdate(LocalDateTime.now());
        String description = "Orden de trabajo movida desde " + statusFrom.getDescription() + " a " + statusTo.getDescription();
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.valueOf(to), description, comments));

        quoteRepository.save(quote);
    }

    public void addCommentQuote(User loggedUser, Long quoteId, String comments){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setLastUpdate(LocalDateTime.now());
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.COMMENTED, "Nuevo Comentario", comments));

        quoteRepository.save(quote);
    }

    public void updatePriorityQuote(User loggedUser, Long quoteId, String priority){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setLastUpdate(LocalDateTime.now());
        quote.setPriorityType(PriorityType.valueOf(priority));
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.UPDATE_PRIORITY, "Prioridad Actualizada"));

        quoteRepository.save(quote);
    }

    private String getQuoteCode(Quote quote){
        String result="JAR-";
        LocalDate currentDate = LocalDate.now();

        if (InvoiceType.INDUMELEC.equals(quote.getInvoice())){
            result = "IND-";
        }

        result += currentDate.getYear() + "-" + StringUtils.leftPad(quote.getQuoteId().toString(),4, '0');

        return result;
    }

    public Quote getQuoteById(Long quoteId){
        return quoteRepository.getById(quoteId);
    }

    public void updateQuote(User loggedUser, Long quoteId, String deliveryDate, String comments){
        Quote quote = quoteRepository.getById(quoteId);

        quote.setLastUpdate(LocalDateTime.now());
        quote.setDeliveryDate(LocalDate.parse(deliveryDate, IndumelecFormatter.dateFormat));
        quote.getQuoteHistories().add(getQuoteHistory(loggedUser, Status.UPDATE_DELIVERY, "Fecha de entrega actualizada", comments));

        quoteRepository.save(quote);
    }

    public byte[] getQuotePdf(Quote quote){
        byte[] pdf = null;

        try {
            pdf = pdfGenerator.getQuotePdf(quote);
        } catch (IOException ex ) {
            log.error("Error generating pdf", ex);
        }

        return pdf;
    }
}