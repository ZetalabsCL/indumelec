package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.*;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.model.vo.ChartInfo;
import com.zetalabs.indumelec.model.vo.Statistic;
import com.zetalabs.indumelec.repository.CompanyRepository;
import com.zetalabs.indumelec.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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

    public Statistic getStatistic(){
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate = currentDate.minusDays(30);
        Statistic currentStatistic = getStatisticByDate(currentDate);
        Statistic previousStatistic = getStatisticByDate(previousDate);

        currentStatistic.setReviewedLastMonth(getIncrease(previousStatistic.getInReview(), currentStatistic.getInReview()));
        currentStatistic.setProgressLastMonth(getIncrease(previousStatistic.getInProgress(), currentStatistic.getInProgress()));
        currentStatistic.setClosedLastMonth(getIncrease(previousStatistic.getFinished(), currentStatistic.getFinished()));

        return currentStatistic;
    }

    private Statistic getStatisticByDate(LocalDateTime requestDate){
        Statistic statistic = new Statistic();

        Long total = quoteRepository.countQuoteByEntryDateIsLessThanEqualAndStatusNotIn(requestDate, Arrays.asList(Status.REJECTED));
        Long inReview = quoteRepository.countQuoteByEntryDateIsLessThanEqualAndStatusIn(requestDate, Arrays.asList(Status.REVIEW));
        Long closed = quoteRepository.countQuoteByEntryDateIsLessThanEqualAndStatusIn(requestDate, Arrays.asList(Status.COMPLETED));
        Long inProgress = quoteRepository.countQuoteByEntryDateIsLessThanEqualAndStatusNotIn(requestDate, Arrays.asList(Status.REVIEW,
                Status.REJECTED, Status.COMPLETED));

        statistic.setReviewed(getPorcentaje(inReview, total));
        statistic.setProgress(getPorcentaje(inProgress, total));
        statistic.setClosed(getPorcentaje(closed, total));

        statistic.setTotal(total);
        statistic.setInReview(inReview);
        statistic.setInProgress(inProgress);
        statistic.setFinished(closed);

        return statistic;
    }

    public ChartInfo getSalesProjection(){
        ChartInfo chartInfo = new ChartInfo();
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusDays(8);

        LocalDateTime currentWeek = currentDate.plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime startToday = currentWeek.toLocalDate().atStartOfDay();
        LocalDateTime startCurrentWeek = currentWeek.minusDays(7).toLocalDate().atStartOfDay();
        LocalDateTime previousWeek = previousDate.plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime previousStartWeek = previousWeek.minusDays(7).toLocalDate().atStartOfDay();

        Double amountCurrentWeek = quoteRepository.getQuoteAmountByPeriod(startCurrentWeek, currentWeek);
        Double amountPreviousWeek = quoteRepository.getQuoteAmountByPeriod(previousStartWeek, previousWeek);
        Double todayAmount = quoteRepository.getQuoteAmountByPeriod(startToday, currentWeek);

        if (amountCurrentWeek !=null) {
            chartInfo.setCurrentWeek(amountCurrentWeek);
        } else {
            chartInfo.setCurrentWeek(0.0);
        }

        if (amountPreviousWeek !=null) {
            chartInfo.setPreviousWeek(amountPreviousWeek);
        } else {
            chartInfo.setPreviousWeek(0.0);
        }

        if (todayAmount!=null){
            chartInfo.setToday(todayAmount);
        } else {
            chartInfo.setToday(0.0);
        }

        return chartInfo;
    }

    public ChartInfo getQuotesInformation(){
        ChartInfo chartInfo = new ChartInfo();
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusDays(8);

        LocalDateTime currentWeek = currentDate.plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime startToday = currentWeek.toLocalDate().atStartOfDay();
        LocalDateTime startCurrentWeek = currentWeek.minusDays(7).toLocalDate().atStartOfDay();
        LocalDateTime previousWeek = previousDate.plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime previousStartWeek = previousWeek.minusDays(7).toLocalDate().atStartOfDay();

        Long quantityCurrentWeek = quoteRepository.countQuoteByEntryDateBetween(startCurrentWeek, currentWeek);
        Long quantityPreviousWeek = quoteRepository.countQuoteByEntryDateBetween(previousStartWeek, previousWeek);
        Long todayQuantity = quoteRepository.countQuoteByEntryDateBetween(startToday, currentWeek);

        if (quantityCurrentWeek !=null) {
            chartInfo.setCurrentWeek(Double.valueOf(quantityCurrentWeek));
        }

        if (quantityPreviousWeek !=null) {
            chartInfo.setPreviousWeek(Double.valueOf(quantityPreviousWeek));
        }

        if (todayQuantity!=null){
            chartInfo.setToday(Double.valueOf(todayQuantity));
        }

        return chartInfo;
    }

    private Double getPorcentaje(Long portion, Long total){
        BigDecimal value = BigDecimal.ZERO;
        MathContext m = new MathContext(2);

        if (total!=0){
            value = BigDecimal.valueOf((portion*100)/total);
        }

        return value.round(m).doubleValue();
    }

    private Double getIncrease(Long originalValue, Long newValue){
        BigDecimal value;
        MathContext m = new MathContext(2);
        Long increase = newValue - originalValue;

        if (originalValue == 0){
            value = BigDecimal.valueOf((increase/1)*100);
        } else {
            value = BigDecimal.valueOf((increase/originalValue)*100);
        }

        return value.round(m).doubleValue();
    }

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

        quote.setQuoteHistories(getInitialQuoteHistory(user));
        quote.setQuoteDetails(getQuoteDetails(quote.getQuoteDetailsList()));

        quoteRepository.save(quote);
    }

    private Set<QuoteDetail> getQuoteDetails(List<QuoteDetail> quoteDetailList){
        Set<QuoteDetail> quoteDetails = new HashSet<>();
        int count = 1;

        for (QuoteDetail quoteDetail : quoteDetailList){
            quoteDetail.setOrderId(count++);
        }

        quoteDetails.addAll(quoteDetailList);

        return quoteDetails;
    }

    private Set<QuoteHistory> getInitialQuoteHistory(User user){
        Set<QuoteHistory> quoteHistories = new HashSet<>();

        QuoteHistory quoteHistory = new QuoteHistory();
        quoteHistory.setDescription("Cotizacion Ingresada");
        quoteHistory.setEntryDate(LocalDateTime.now());
        quoteHistory.setStatus(Status.NEW);
        quoteHistory.setUser(user);
        quoteHistories.add(quoteHistory);

        quoteHistory = new QuoteHistory();
        quoteHistory.setDescription("Cotizacion Enviada a Revision");
        quoteHistory.setEntryDate(LocalDateTime.now());
        quoteHistory.setStatus(Status.REVIEW);
        quoteHistory.setUser(user);
        quoteHistories.add(quoteHistory);

        return quoteHistories;
    }
}