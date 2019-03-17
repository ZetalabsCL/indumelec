package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.model.vo.ChartInfo;
import com.zetalabs.indumelec.model.vo.Statistic;
import com.zetalabs.indumelec.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

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
        LocalDateTime currentWeek = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startCurrentWeek = currentWeek.with(DayOfWeek.MONDAY);
        LocalDateTime previousWeek = currentWeek.minusDays(7);
        LocalDateTime previousStartWeek = startCurrentWeek.minusDays(7);

        Double amountCurrentWeek = quoteRepository.getQuoteAmountByPeriod(startCurrentWeek, currentWeek);
        Double amountPreviousWeek = quoteRepository.getQuoteAmountByPeriod(previousStartWeek, previousWeek);
        Double todayAmount = quoteRepository.getQuoteAmountByEntryDate(currentWeek);

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
        LocalDateTime currentWeek = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startCurrentWeek = currentWeek.with(DayOfWeek.MONDAY);
        LocalDateTime previousWeek = currentWeek.minusDays(7);
        LocalDateTime previousStartWeek = startCurrentWeek.minusDays(7);

        Long quantityCurrentWeek = quoteRepository.countQuoteByEntryDateBetween(startCurrentWeek, currentWeek);
        Long quantityPreviousWeek = quoteRepository.countQuoteByEntryDateBetween(previousStartWeek, previousWeek);
        Long todayQuantity = quoteRepository.countQuoteByEntryDateEquals(currentWeek);

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
}
