package com.zetalabs.indumelec.utils;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
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

        Document document = new Document(pdfDocument);
        document.add(this.table);
    }
}
