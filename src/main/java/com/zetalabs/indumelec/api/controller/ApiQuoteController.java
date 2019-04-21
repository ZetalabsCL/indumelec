package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.api.dto.AbstractWrapper;
import com.zetalabs.indumelec.api.dto.QuoteDetailWrapper;
import com.zetalabs.indumelec.api.dto.QuoteWrapper;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.service.QuoteService;
import com.zetalabs.indumelec.service.UserService;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ApiQuoteController {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserService userService;

    @RequestMapping("/api/quote/reviewList")
    public Map<String, Object> quoteReviewList() {
        List<Quote> quoteList = quoteService.getQuoteListByStatus(Status.REVIEW);

        List<QuoteWrapper> resultList  = quoteList.stream().map(getQuotes).collect(Collectors.toList());

        return getResult(resultList);
    }

    @RequestMapping("/api/quote/pendingList")
    public Map<String, Object> quoteList() {
        List<Quote> quoteList = quoteService.getQuoteList();

        List<QuoteWrapper> resultList  = quoteList.stream().map(getQuotes).collect(Collectors.toList());

        return getResult(resultList);
    }

    @RequestMapping("/api/quote/approve")
    public ResponseEntity approveQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("workOrder") String workOrder, @RequestParam("userId") String userId) {
        User user = userService.getUserByMail(userId);
        quoteService.approveQuote(user, workOrder, quoteId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/reject")
    public ResponseEntity rejectQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId) {
        User user = userService.getUserByMail(userId);

        quoteService.rejectQuote(user, quoteId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/production")
    public ResponseEntity moveQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId, @RequestParam("comments") String comments,
                                    @RequestParam("from") String from, @RequestParam("to") String to) {
        User user = userService.getUserByMail(userId);

        quoteService.moveQuote(user, quoteId, comments, from, to);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/details")
    public Map<String, Object> detailsQuote(@RequestParam("quoteId") Long quoteId) {
        Quote quote = quoteService.getQuoteById(quoteId);

        List<QuoteDetailWrapper> resultList  = quote.getQuoteDetails().stream().map(getQuoteDetails).collect(Collectors.toList());

        return getResult(resultList);
    }

    private Function<Quote, QuoteWrapper> getQuotes = (t) -> {
        QuoteWrapper quoteWrapper = new QuoteWrapper();
        quoteWrapper.setQuoteId(t.getQuoteId());
        quoteWrapper.setQuoteCode(t.getQuoteCode());
        quoteWrapper.setWorkOrder(t.getWorkOrder());
        quoteWrapper.setStatus(t.getStatus().getDescription());
        quoteWrapper.setCompany(t.getCompany().getName());
        quoteWrapper.setEntryDate(t.getEntryDate().format(IndumelecFormatter.dateFormat));
        quoteWrapper.setDeliveryDate(t.getDeliveryDate().format(IndumelecFormatter.dateFormat));
        quoteWrapper.setReference(t.getReference());
        quoteWrapper.setAmount(IndumelecFormatter.numberFormat.format(t.getAmount()));
        Period daysLeft = Period.between(LocalDate.now(), t.getDeliveryDate());
        quoteWrapper.setDaysLeft(daysLeft.getDays());

        return quoteWrapper;
    };

    private Function<QuoteDetail, QuoteDetailWrapper> getQuoteDetails = (t) -> {
        QuoteDetailWrapper quoteDetailWrapper = new QuoteDetailWrapper();
        quoteDetailWrapper.setDescription(t.getDescription());
        quoteDetailWrapper.setMeasure(t.getMeasure());
        quoteDetailWrapper.setQuantity(t.getQuantity());

        return quoteDetailWrapper;
    };

    private Map<String, Object> getResult(List<? extends AbstractWrapper> resultList){
        Map<String, Object> result = new HashMap<>();

        result.put("data", resultList);
        result.put("draw", 1);
        result.put("recordsTotal", resultList.size());
        result.put("recordsFiltered", resultList.size());

        return result;
    }
}
