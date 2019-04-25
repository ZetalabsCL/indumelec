package com.zetalabs.indumelec.utils;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

public class TableHeaderEventHandler implements IEventHandler {
    private Table table;

    public TableHeaderEventHandler(Table table){
        this.table = table;
    }

    @Override
    public void handleEvent(Event event){
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        PdfPage page = documentEvent.getPage();
        Document document = new Document(pdfDocument);

        if (pdfDocument.getPageNumber(page) == 1)
        {
            document.setTopMargin(10);
            document.add(this.table);
        } else {
            document.setTopMargin(60);
        }
    }
}
