package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.api.dto.AbstractWrapper;
import com.zetalabs.indumelec.api.dto.QuoteDetailWrapper;
import com.zetalabs.indumelec.api.dto.QuoteHistoryWrapper;
import com.zetalabs.indumelec.api.dto.QuoteWrapper;
import com.zetalabs.indumelec.model.*;
import com.zetalabs.indumelec.model.types.*;
import com.zetalabs.indumelec.service.HolidayService;
import com.zetalabs.indumelec.service.QuoteHistoryService;
import com.zetalabs.indumelec.service.QuoteService;
import com.zetalabs.indumelec.service.UserService;
import com.zetalabs.indumelec.utils.FormUtils;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ApiQuoteController {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuoteHistoryService quoteHistoryService;

    @Autowired
    private HolidayService holidayService;

    @RequestMapping("/api/quote/reviewList")
    public Map<String, Object> quoteReviewList() {
        List<Quote> quoteList = quoteService.getQuoteListByStatus(Status.REVIEW, StringUtils.EMPTY);

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
        List<Quote> quoteList = quoteService.getQuoteListByStatus(Status.DELIVERY, StringUtils.EMPTY);

        List<QuoteWrapper> resultList  = quoteList.stream().map(getQuotes).collect(Collectors.toList());

        return getResult(resultList);
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

    @RequestMapping("/api/quote/reprocess")
    public ResponseEntity reprocessQuote(@RequestParam("quoteId") Long quoteId, @RequestParam("userId") String userId, @RequestParam("comments") String comments) {
        User user = userService.getUserByMail(userId);
        quoteService.reprocessQuote(user, quoteId, comments);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/approve")
    public ResponseEntity approveQuote(@RequestParam("frmInfo") String frmInfo, @RequestParam("userId") String userId, @RequestParam("details") String details) throws ParseException {
        User user = userService.getUserByMail(userId);
        Quote quote = getQuote(frmInfo, details);

        quoteService.updateQuote(user, quote);

        quoteService.approveQuote(user, quote.getWorkOrder(), quote.getQuoteId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/reject")
    public ResponseEntity rejectQuote(@RequestParam("frmInfo") String frmInfo, @RequestParam("userId") String userId, @RequestParam("details") String details) throws ParseException {
        User user = userService.getUserByMail(userId);
        Quote quote = getQuote(frmInfo, details);

        quoteService.updateQuote(user, quote);

        quoteService.rejectQuote(user, quote.getQuoteId());

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
        List<QuoteDetailWrapper> details =  quote.getQuoteDetails().stream().map(getQuoteDetails).collect(Collectors.toList());
        quoteWrapper.setDetails(details);

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

            if (quote.getQuoteId()==null) {
                quoteService.saveQuote(user, quote);
            } else {
                quoteService.updateQuote(user, quote);
            }
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/comment")
    public ResponseEntity comment(@RequestParam("userId") String userId, @RequestParam("quoteId") Long quoteId, @RequestParam("comments") String comments) {
        User user = userService.getUserByMail(userId);

        quoteService.addCommentQuote(user, quoteId, comments);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/priority")
    public ResponseEntity priority(@RequestParam("userId") String userId, @RequestParam("quoteId") Long quoteId, @RequestParam("priority") String priority) {
        User user = userService.getUserByMail(userId);

        quoteService.updatePriorityQuote(user, quoteId, priority);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/api/quote/verifyOt")
    public ResponseEntity verifyOt(@RequestParam("workOrderId") String workOrderId) {
        List<Quote> quoteList = quoteService.getQuoteListByWorkOrder(workOrderId);

        ResponseEntity response = new ResponseEntity<>("0", HttpStatus.OK);

        if (CollectionUtils.isNotEmpty(quoteList)) {
            response = new ResponseEntity<>("1", HttpStatus.OK);
        }

        return response;
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
        quoteWrapper.setContact(t.getContact());
        quoteWrapper.setPhone(t.getPhone());
        quoteWrapper.setPriorityType(t.getPriorityType());

        Long businessDays = getBusinessDays(LocalDate.now(), t.getDeliveryDate());
        quoteWrapper.setDaysLeft(businessDays.intValue());

        return quoteWrapper;
    };

    private Function<QuoteDetail, QuoteDetailWrapper> getQuoteDetails = (t) -> {
        QuoteDetailWrapper quoteDetailWrapper = new QuoteDetailWrapper();

        if (t.getDescription()!=null) {
            quoteDetailWrapper.setDescription(t.getDescription().replaceAll("\r\n", "<br/>"));
        }

        quoteDetailWrapper.setMeasure(t.getMeasure());
        quoteDetailWrapper.setQuantity(t.getQuantity());
        quoteDetailWrapper.setPrice(t.getPrice());

        if (t.getNotes()!=null) {
            quoteDetailWrapper.setNotes(t.getNotes().replaceAll("\r\n", "<br/>"));
        }

        return quoteDetailWrapper;
    };

    private Function<QuoteHistory, QuoteHistoryWrapper> getQuoteHistory = (t) -> {
        QuoteHistoryWrapper quoteHistoryWrapper = new QuoteHistoryWrapper();
        quoteHistoryWrapper.setEntryDate(t.getEntryDate().format(IndumelecFormatter.dateFormat));

        if (t.getDescription()!=null) {
            quoteHistoryWrapper.setDescription(t.getDescription().replaceAll("\r\n", "<br/>"));
        }

        if (t.getComments()!=null) {
            quoteHistoryWrapper.setComments(t.getComments().replaceAll("\r\n", "<br/>"));
        }

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

        Map<String, String> map = FormUtils.getMap(new JSONArray(frmInfo));

        company.setTaxId(map.get("taxid"));

        quote.setReference(map.get("reference"));
        quote.setManufacture(map.get("manufacture"));

        if (map.get("paymentType")!=null) {
            quote.setPaymentType(PaymentType.valueOf(map.get("paymentType")));
        }

        quote.setOtherPayment(map.get("otherPayment"));

        if (map.get("deliveryType")!=null) {
            quote.setDeliveryType(DeliveryType.valueOf(map.get("deliveryType")));
        }

        quote.setComments(map.get("comments"));
        quote.setDeliveryDate(LocalDate.parse(map.get("deliveryDate"), IndumelecFormatter.dateFormat));
        quote.setPartialDelivery(map.get("partialDelivery"));
        quote.setDeliveryLocation(map.get("deliveryLocation"));

        if (map.get("invoice")!=null) {
            quote.setInvoice(InvoiceType.valueOf(map.get("invoice")));
        }

        if (map.get("signature")!=null) {
            quote.setSignature(SignatureType.valueOf(map.get("signature")));
        }

        quote.setCompany(company);
        quote.setContact(map.get("contact"));
        quote.setPhone(map.get("phone"));
        quote.setCellphone(map.get("cellphone"));
        quote.setEmail(map.get("email"));
        quote.setQuoteDetails(getDetails(new JSONArray(details)));
        quote.setEntryDate(LocalDateTime.now());

        Long businessDays = getBusinessDays(quote.getEntryDate().toLocalDate(), quote.getDeliveryDate());
        quote.setBusinessDays(businessDays.intValue());

        BigDecimal total = quote.getQuoteDetails()
                .stream()
                .map(a -> a.getQuantity().multiply(a.getPrice()))
                .reduce(BigDecimal::add)
                .get();

        quote.setAmount(total);

        if (map.get("quoteId")!=null){
            quote.setQuoteId(Long.valueOf(map.get("quoteId")));
        }

        quote.setWorkOrder(map.get("workOrder"));
        quote.setQuoteCode(map.get("quoteCode"));

        if (map.get("priorityType")!=null) {
            quote.setPriorityType(PriorityType.valueOf(map.get("priorityType")));
        }

        return quote;
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

            String measure=jsonObject.getString(2);
            measure=measure.replaceAll("(?i)<br */?>","\r\n");
            detail.setMeasure(measure);

            String priceStr = jsonObject.getString(3);
            BigDecimal price = BigDecimal.valueOf(IndumelecFormatter.numberFormatForCurrency.parse(priceStr).longValue());
            detail.setPrice(price);

            String notes=jsonObject.getString(5);
            notes=notes.replaceAll("(?i)<br */?>","\r\n");
            detail.setNotes(notes);

            detail.setOrderId(count++);

            details.add(detail);
        }

        return details;
    }

    private Long getBusinessDays(final LocalDate startDate, final LocalDate endDate) {
        final DayOfWeek startW = startDate.getDayOfWeek();
        final DayOfWeek endW = endDate.getDayOfWeek();

        final long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        final long daysWithoutWeekends = days - 2 * ((days + startW.getValue())/7);

        long result = daysWithoutWeekends + (startW == DayOfWeek.SUNDAY ? 1 : 0) + (endW == DayOfWeek.SUNDAY ? 1 : 0);
        long holidays = getHolidaysCount(startDate, endDate);

        return result - holidays;
    }

    private Integer getHolidaysCount(LocalDate startDate, LocalDate endDate){
        List<Holiday> holidayList = holidayService.getHolidayList(startDate, endDate);

        Integer holidayCount=0;

        for (Holiday holiday: holidayList){
            DayOfWeek dayOfWeek = holiday.getEntryDate().getDayOfWeek();

            if (!DayOfWeek.SATURDAY.equals(dayOfWeek) && (!DayOfWeek.SUNDAY.equals(dayOfWeek))) {
                holidayCount++;
            }
        }

        return holidayCount;
    }
}
