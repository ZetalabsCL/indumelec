package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ExcelGenerator {
    public byte[] getReport(List<Quote> quoteList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cotizaciones");
        setHeaders(workbook, sheet);

        AtomicInteger rows = new AtomicInteger(1);
        quoteList.forEach(q -> {
            setQuote(workbook, sheet, rows.getAndIncrement(), q);
        });

        for (int i=0; i<8; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        // Closing the workbook
        workbook.close();
        return out.toByteArray();
    }

    protected void setHeaders(Workbook workbook, Sheet sheet){
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        setCell(headerRow, 0, "# Cotizacion", headerCellStyle);
        setCell(headerRow, 1, "# OT", headerCellStyle);
        setCell(headerRow, 2, "Empresa", headerCellStyle);
        setCell(headerRow, 2, "Contacto", headerCellStyle);
        setCell(headerRow, 3, "Estado", headerCellStyle);
        setCell(headerRow, 4, "Fecha Ingreso", headerCellStyle);
        setCell(headerRow, 5, "Fecha Entrega", headerCellStyle);
        setCell(headerRow, 6, "Prioridad", headerCellStyle);
        setCell(headerRow, 7, "Detalle", headerCellStyle);
    }

    protected void setCell(Row row, int index, String description, CellStyle cellStyle){
        Cell cell = row.createCell(index);
        cell.setCellValue(description);
        cell.setCellStyle(cellStyle);
    }

    protected void setQuote(Workbook workbook, Sheet sheet, int index, Quote quote){
        Font dataFont = workbook.createFont();
        dataFont.setBold(false);
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setFont(dataFont);

        CellStyle dataCellStyleMultiLine = workbook.createCellStyle();
        dataCellStyleMultiLine.setWrapText(true);
        dataCellStyleMultiLine.setFont(dataFont);

        Row dataRow = sheet.createRow(index);
        setCell(dataRow, 0, quote.getQuoteCode(), dataCellStyle);
        setCell(dataRow, 1, quote.getWorkOrder(), dataCellStyle);
        setCell(dataRow, 2, quote.getCompany().getName(), dataCellStyle);
        setCell(dataRow, 2, quote.getContact(), dataCellStyle);
        setCell(dataRow, 3, quote.getStatus().getDescription(), dataCellStyle);
        setCell(dataRow, 4, quote.getEntryDate().format(IndumelecFormatter.dateFormat), dataCellStyle);
        setCell(dataRow, 5, quote.getDeliveryDate().format(IndumelecFormatter.dateFormat), dataCellStyle);
        setCell(dataRow, 6, quote.getPriorityType().getDescription(), dataCellStyle);
        setCell(dataRow, 7, getQuoteDetails(quote), dataCellStyleMultiLine);
    }

    protected String getQuoteDetails(Quote quote){
        AtomicReference<String> result = new AtomicReference<>(StringUtils.EMPTY);

        if (CollectionUtils.isNotEmpty(quote.getQuoteDetails())) {
            quote.getQuoteDetails().forEach(qd -> {
                result.set(StringUtils.join(result, qd.getDescription(), " - ", qd.getMeasure(), " - ", qd.getQuantity(), "\n\r"));
            });
        }

        return result.get();
    }
}
