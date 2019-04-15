package com.zetalabs.indumelec.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PdfGenerator {
    private String defaultFont = StandardFonts.TIMES_ROMAN;
    private int defaultFontSize = 12;

    public byte[] getQuotePdf(Quote quote) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);

//        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TableHeaderEventHandler(getHeaderTbl()));
        Document doc = new Document(pdfDoc, PageSize.LETTER);
        doc.setTopMargin(60);
        doc.setBottomMargin(60);

        doc.add(getTitleTbl(quote));
        doc.add(new Paragraph());
        doc.add(getCurrentDatePg());
        doc.add(new Paragraph());
        doc.add(getRefPg(quote));
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(getTextPg());
        doc.add(new Paragraph());
        doc.add(getDetailsTbl(quote));
        doc.add(new Paragraph());
        doc.add(new Paragraph());
        doc.add(getParagraphBold("Condiciones Comerciales"));
        doc.add(new Paragraph());
        doc.add(getParagraph("1.- Valores Neto NO incluyen I.V.A y Costo de envio"));
        doc.add(new Paragraph());
        doc.add(getParagraph("2.- Plazo entrega en Santiago: " + IndumelecFormatter.dateFormat.format(quote.getDeliveryDate())));
        doc.add(new Paragraph());
        doc.add(getParagraph("3.- Forma de pago: " + quote.getPaymentType().getDescription()));
        doc.close();

        return out.toByteArray();
    }

    private Table getTitleTbl(Quote quote) throws IOException{
        Table titleTbl = new Table(new float[] { 100, 400 });

        Cell cellTxt = new Cell(1, 1).add(new Paragraph(getBoldText("Señores")));
        cellTxt.setBorder(Border.NO_BORDER);
        titleTbl.addCell(cellTxt);

        cellTxt = new Cell(1, 1).add(new Paragraph(getBoldText("Cotizacion " + quote.getQuoteCode())));
        cellTxt.setBorder(Border.NO_BORDER);
        cellTxt.setTextAlignment(TextAlignment.CENTER);
        titleTbl.addCell(cellTxt);

        cellTxt = new Cell(1, 2).add(new Paragraph(getBoldText(quote.getCompany().getName())));
        cellTxt.setBorder(Border.NO_BORDER);
        titleTbl.addCell(cellTxt);

        cellTxt = new Cell(1, 2).add(new Paragraph(getBoldText(quote.getCompany().getContact())));
        cellTxt.setBorder(Border.NO_BORDER);
        titleTbl.addCell(cellTxt);

        cellTxt = new Cell(1, 2).add(new Paragraph(getBoldText("Presente", StandardFonts.TIMES_ROMAN, 12, true)));
        cellTxt.setBorder(Border.NO_BORDER);
        titleTbl.addCell(cellTxt);

        return titleTbl;
    }

    private Paragraph getCurrentDatePg() throws IOException{
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy");
        String currentDate = "Santiago, " + LocalDate.now().format(dateTimeFormatter);

        Paragraph paragraph = getParagraph(currentDate);
        paragraph.setTextAlignment(TextAlignment.RIGHT);

        return paragraph;
    }

    private Paragraph getRefPg(Quote quote) throws IOException{
        Paragraph paragraph = new Paragraph(getBoldText("Ref: " + quote.getReference(), StandardFonts.TIMES_ROMAN, 12, true));
        paragraph.setTextAlignment(TextAlignment.RIGHT);

        return paragraph;
    }

    private Paragraph getTextPg() throws IOException{
        Paragraph paragraph = new Paragraph(getNormalText("De acuerdo a lo solicitado, sirvace encontrar nuestra oferta por la fabricacion de los productos que se detallan a continuacion, segun antecedetes proporcionados por usted.", StandardFonts.TIMES_ROMAN, 12, false));
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);

        return paragraph;
    }

    private Table getDetailsTbl(Quote quote) throws IOException {
        Table detailsTbl = new Table(new float[] { 80, 200, 80, 80, 80 });
        detailsTbl.setSkipFirstHeader(false);


        detailsTbl.addCell(getHeaderCell("Cantidad"));
        detailsTbl.addCell(getHeaderCell("Detalle"));
        detailsTbl.addCell(getHeaderCell("Medidas"));
        detailsTbl.addCell(getHeaderCell("Valor Unitario"));
        detailsTbl.addCell(getHeaderCell("Valor Total"));

        for (QuoteDetail detail : quote.getQuoteDetails()){
            BigDecimal total = detail.getQuantity().multiply(detail.getPrice());
            detailsTbl.addCell(getRowCell(IndumelecFormatter.numberFormatNoDecimalsForQuantity.format(detail.getQuantity())));
            detailsTbl.addCell(getRowCell(detail.getDescription()));
            detailsTbl.addCell(getRowCell(detail.getMeasure()));

            Cell cell = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForMoney.format(detail.getPrice()));
            cell.setTextAlignment(TextAlignment.RIGHT);
            detailsTbl.addCell(cell);

            cell = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForMoney.format(total));
            cell.setTextAlignment(TextAlignment.RIGHT);
            detailsTbl.addCell(cell);
        }

        Cell cellTxt = getRowCell("Valor Total Neto",1,3);
        cellTxt.setTextAlignment(TextAlignment.RIGHT);
        cellTxt.setBorder(Border.NO_BORDER);
        detailsTbl.addCell(cellTxt);

        cellTxt = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForMoney.format(quote.getAmount()),1,2);
        cellTxt.setTextAlignment(TextAlignment.RIGHT);
        detailsTbl.addCell(cellTxt);

        return detailsTbl;
    }

    private Table getHeaderTbl() throws MalformedURLException {
        Table headerTbl = new Table(new float[] { 200 });
        Image imgLogo = new Image(ImageDataFactory.create("/logo_tws.png"));
        imgLogo.setWidth(imgLogo.getImageWidth() * 0.8f);
        imgLogo.setHeight(imgLogo.getImageHeight() * 0.8f);

        Cell cellLogo = new Cell(1, 1).add(imgLogo);
        cellLogo.setBorder(Border.NO_BORDER);
        headerTbl.addCell(cellLogo);

        return headerTbl;
    }

    private Paragraph getParagraph(String text) throws IOException{
        Paragraph paragraph = new Paragraph(getNormalText(text));
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);

        return paragraph;
    }

    private Paragraph getParagraphBold(String text) throws IOException{
        Paragraph paragraph = new Paragraph(getBoldText(text));
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);

        return paragraph;
    }

    private Cell getHeaderCell(String text) throws IOException{
        Cell cellTxt = new Cell().add(new Paragraph(getBoldText(text)));
        cellTxt.setPaddingLeft(10);

        return cellTxt;
    }

    private Cell getRowCell(String text) throws IOException{
        Cell cellTxt = new Cell().add(new Paragraph(getNormalText(text)));
        cellTxt.setPaddingLeft(10);

        return cellTxt;
    }

    private Cell getRowCell(String text, int colspan, int rowspan) throws IOException{
        Cell cellTxt = new Cell(colspan, rowspan).add(new Paragraph(getNormalText(text)));
        cellTxt.setPaddingLeft(10);

        return cellTxt;
    }

    private Text getBoldText(String content) throws IOException{
        return getBoldText(content, defaultFont, defaultFontSize, false);
    }

    private Text getNormalText(String content) throws IOException{
        return getNormalText(content, defaultFont, defaultFontSize, false);
    }

    private Text getBoldText(String content, String fontName, float fontSize, boolean underline) throws IOException{
        Text txt = new Text(content);
        txt.setFontColor(ColorConstants.BLACK);
        txt.setFont(PdfFontFactory.createFont(fontName));
        txt.setFontSize(fontSize);
        txt.setBold();

        if (underline) {
            txt.setUnderline();
        }

        return txt;
    }

    private Text getNormalText(String content, String fontName, float fontSize, boolean underline) throws IOException {
        Text txt = new Text(content);
        txt.setFontColor(ColorConstants.BLACK);
        txt.setFont(PdfFontFactory.createFont(fontName));
        txt.setFontSize(fontSize);

        if (underline) {
            txt.setUnderline();
        }

        return txt;
    }
}
