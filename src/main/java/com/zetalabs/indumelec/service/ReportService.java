package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.PriorityType;
import com.zetalabs.indumelec.repository.QuoteRepository;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ReportService {
    private final QuoteRepository quoteRepository;
    private final ExcelGenerator excelGenerator;

    public ReportService(QuoteRepository quoteRepository,
                         ExcelGenerator excelGenerator){
        this.quoteRepository = quoteRepository;
        this.excelGenerator = excelGenerator;
    }

    public byte[] getReport(String from, String to, String priority) {
        LocalDate deliveryFrom = LocalDate.parse(from, IndumelecFormatter.dateFormat);
        LocalDate deliveryTo = LocalDate.parse(to, IndumelecFormatter.dateFormat);
        List<Quote> quoteList = null;

        if (StringUtils.isNotEmpty(priority) && !"ALL".equals(priority)) {
            quoteList = quoteRepository.getQuotesByDeliveryDateBetweenAndPriorityType(deliveryFrom, deliveryTo,
                    PriorityType.valueOf(priority));
        } else {
            quoteList = quoteRepository.getQuotesByDeliveryDateBetween(deliveryFrom, deliveryTo);
        }

        byte[] xls = null;

        try {
            xls = excelGenerator.getReport(quoteList);
        } catch (IOException ex ) {
            log.error("Error generating pdf", ex);
        }

        return xls;
    }
}
