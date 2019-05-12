package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.api.dto.AbstractWrapper;
import com.zetalabs.indumelec.api.dto.QuoteDetailWrapper;
import com.zetalabs.indumelec.api.dto.QuoteHistoryWrapper;
import com.zetalabs.indumelec.api.dto.QuoteWrapper;
import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.model.QuoteHistory;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.DeliveryType;
import com.zetalabs.indumelec.model.types.InvoiceType;
import com.zetalabs.indumelec.model.types.PaymentType;
import com.zetalabs.indumelec.model.types.SignatureType;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.service.QuoteHistoryService;
import com.zetalabs.indumelec.service.QuoteService;
import com.zetalabs.indumelec.service.UserService;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j
public class ApiQuoteController {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuoteHistoryService quoteHistoryService;

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

    @RequestMapping("/api/quote/deliveryList")
    public Map<String, Object> quoteDeliveryList() {
        List<Quote> quoteList = quoteService.getQuoteListByStatus(Status.DELIVERY);

        List<QuoteWrapper> resultList  = quoteList.stream().map(getQuotes).collect(Collectors.toList());

        return getResult(resultList);
    }

    @RequestMapping("/api/quote/approve")
    public ResponseEntity approveQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("workOrder") String workOrder, @RequestParam("userId") String userId) {
        User user = userService.getUserByMail(userId);
        quoteService.approveQuote(user, workOrder, quoteId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/delivery")
    public ResponseEntity deliveryQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId, @RequestParam("comments") String comments) {
        User user = userService.getUserByMail(userId);

        quoteService.deliveryQuote(user, quoteId, comments);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/return")
    public ResponseEntity returnQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId, @RequestParam("comments") String comments) {
        User user = userService.getUserByMail(userId);
        quoteService.returnQuote(user, quoteId, comments);

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

    @RequestMapping("/api/quote/history")
    public Map<String, Object> historyQuote(@RequestParam("quoteId") Long quoteId) {
        List<QuoteHistory> quoteHistoryList = quoteHistoryService.getAllByQuoteId(quoteId);

        List<QuoteHistoryWrapper> resultList  = quoteHistoryList.stream().map(getQuoteHistory).collect(Collectors.toList());

        return getResult(resultList);
    }

    @RequestMapping("/api/quote/info")
    public QuoteWrapper infoQuote(@RequestParam("quoteId") Long quoteId) {
        Quote quote = quoteService.getQuoteById(quoteId);
        QuoteWrapper quoteWrapper = getQuotes.apply(quote);

        return quoteWrapper;
    }

    @RequestMapping("/api/quote/update")
    public ResponseEntity updateQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId,
                                    @RequestParam("deliveryDate") String deliveryDate, @RequestParam("comments") String comments) {
        User user = userService.getUserByMail(userId);

        quoteService.updateQuote(user, quoteId, deliveryDate, comments);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quote/save")
    public ResponseEntity saveUser(@RequestParam("frmInfo") String frmInfo, @RequestParam("userId") String userId, @RequestParam("details") String details) throws ParseException {
        if (frmInfo!=null){
            User user = userService.getUserByMail(userId);
            Quote quote = getQuote(frmInfo, details);
            quoteService.saveQuote(user, quote);

            log.info("Test");
        }

        return ResponseEntity.ok(HttpStatus.OK);
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
        quoteDetailWrapper.setDescription(t.getDescription().replaceAll("\r\n","<br/>"));
        quoteDetailWrapper.setMeasure(t.getMeasure());
        quoteDetailWrapper.setQuantity(t.getQuantity());

        return quoteDetailWrapper;
    };

    private Function<QuoteHistory, QuoteHistoryWrapper> getQuoteHistory = (t) -> {
        QuoteHistoryWrapper quoteHistoryWrapper = new QuoteHistoryWrapper();
        quoteHistoryWrapper.setEntryDate(t.getEntryDate().format(IndumelecFormatter.dateFormat));
        quoteHistoryWrapper.setComments(t.getComments());
        quoteHistoryWrapper.setDescription(t.getDescription());
        quoteHistoryWrapper.setUser(t.getUser().getName());

        return quoteHistoryWrapper;
    };

    private Map<String, Object> getResult(List<? extends AbstractWrapper> resultList){
        Map<String, Object> result = new HashMap<>();

        result.put("data", resultList);
        result.put("draw", 1);
        result.put("recordsTotal", resultList.size());
        result.put("recordsFiltered", resultList.size());

        return result;
    }

    private Quote getQuote(String frmInfo, String details) throws ParseException{
        Quote quote = new Quote();
        Company company = new Company();

        Map<String, String> map = getMap(new JSONArray(frmInfo));

        company.setTaxId(map.get("taxid"));

        quote.setReference(map.get("reference"));
        quote.setManufacture(map.get("manufacture"));
        quote.setPaymentType(PaymentType.valueOf(map.get("paymentType")));
        quote.setOtherPayment(map.get("otherPayment"));
        quote.setDeliveryType(DeliveryType.valueOf(map.get("deliveryType")));
        quote.setComments(map.get("comments"));
        quote.setDeliveryDate(LocalDate.parse(map.get("deliveryDate"), IndumelecFormatter.dateFormat));
        quote.setPartialDelivery(map.get("partialDelivery"));
        quote.setDeliveryLocation(map.get("deliveryLocation"));
        quote.setInvoice(InvoiceType.valueOf(map.get("invoice")));
        quote.setSignature(SignatureType.valueOf(map.get("signature")));

        quote.setCompany(company);
        quote.setQuoteDetails(getDetails(new JSONArray(details)));

        BigDecimal total = quote.getQuoteDetails()
                .stream()
                .map(a -> a.getQuantity().multiply(a.getPrice()))
                .reduce(BigDecimal::add)
                .get();

        quote.setAmount(total);

        return quote;
    }

    private Map<String, String> getMap(JSONArray array){
        Map<String, String> map = new HashMap<>();

        for (Object obj: array){
            JSONObject jsonObject = (JSONObject) obj;
            map.put(jsonObject.get("name").toString(), jsonObject.get("value").toString());
        }

        return map;
    }

    private SortedSet<QuoteDetail> getDetails(JSONArray array) throws ParseException {
        SortedSet<QuoteDetail> details = new TreeSet<>();
        int count=1;

        for (Object obj : array){
            JSONArray jsonObject = (JSONArray) obj;
            QuoteDetail detail = new QuoteDetail();
            detail.setQuantity(jsonObject.getBigDecimal(0));
            String description=jsonObject.getString(1);
            description=description.replaceAll("(?i)<br */?>","\r\n");
            detail.setDescription(description);
            detail.setMeasure(jsonObject.getString(2));

            String priceStr = jsonObject.getString(3);
            BigDecimal price = BigDecimal.valueOf(IndumelecFormatter.numberFormatForCurrency.parse(priceStr).longValue());
            detail.setPrice(price);
            detail.setOrderId(count++);

            details.add(detail);
        }

        return details;
    }
}
