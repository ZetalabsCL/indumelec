package com.zetalabs.indumelec.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
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
import com.zetalabs.indumelec.model.types.DeliveryType;
import com.zetalabs.indumelec.model.types.InvoiceType;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import com.zetalabs.indumelec.utils.TableHeaderEventHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Component
public class PdfGenerator {
    private String defaultFont = StandardFonts.TIMES_ROMAN;
    private int defaultFontSize = 10;

    public byte[] getQuotePdf(Quote quote) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TableHeaderEventHandler(getHeaderTbl(quote)));
        Document doc = new Document(pdfDoc, PageSize.LETTER);
        doc.setTopMargin(60);
        doc.setBottomMargin(60);
        doc.setLeftMargin(30);
        doc.setRightMargin(30);

        setSpaces(doc, 2);
        doc.add(getTitleTbl(quote));
        doc.add(getCurrentDatePg());
        doc.add(getRefPg(quote));
        setSpaces(doc, 1);
        doc.add(getTextPg());
        setSpaces(doc);
        doc.add(getDetailsTbl(quote));
        setSpaces(doc, 2);
        setConditions(doc, quote);
        setSpaces(doc, 2);
        setOtTbl(doc, quote);
        setSpaces(doc, 2);
        setTransferTbl(doc, quote);
        setSpaces(doc);
        setSpaces(doc, 4);
        setSignature(doc, quote);
        doc.close();

        return out.toByteArray();
    }

    private Table getHeaderTbl(Quote quote) {
        Table headerTbl = new Table(new float[] { 200 });
        Image imgLogo = null;

        if (InvoiceType.JARA.equals(quote.getInvoice())){
            imgLogo = new Image(ImageDataFactory.create(getClass().getClassLoader().getResource("jara.png")));
            imgLogo.setWidth(imgLogo.getImageWidth() * 0.1f);
            imgLogo.setHeight(imgLogo.getImageHeight() * 0.1f);
        } else {
            imgLogo = new Image(ImageDataFactory.create(getClass().getClassLoader().getResource("indumelec.png")));
            imgLogo.setWidth(imgLogo.getImageWidth() * 0.3f);
            imgLogo.setHeight(imgLogo.getImageHeight() * 0.3f);
        }

        Cell cellLogo = new Cell(1, 1).add(imgLogo);
        cellLogo.setBorder(Border.NO_BORDER);
        headerTbl.addCell(cellLogo);

        return headerTbl;
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

        cellTxt = new Cell(1, 2).add(new Paragraph(getBoldText("Presente", StandardFonts.TIMES_ROMAN, defaultFontSize, true)));
        cellTxt.setBorder(Border.NO_BORDER);
        titleTbl.addCell(cellTxt);

        return titleTbl;
    }

    private Paragraph getCurrentDatePg() throws IOException{
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-CL"));
        String currentDate = "Santiago, " + LocalDate.now().format(dateTimeFormatter);

        Paragraph paragraph = getParagraph(currentDate);
        paragraph.setTextAlignment(TextAlignment.RIGHT);

        return paragraph;
    }

    private Paragraph getRefPg(Quote quote) throws IOException{
        Paragraph paragraph = new Paragraph(getBoldText("Ref: " + quote.getReference(), StandardFonts.TIMES_ROMAN, defaultFontSize, true));
        paragraph.setTextAlignment(TextAlignment.RIGHT);

        return paragraph;
    }

    private Paragraph getTextPg() throws IOException{
        Paragraph paragraph = new Paragraph(getNormalText("De acuerdo a lo solicitado, sirvace encontrar nuestra oferta por la fabricacion de los productos que se detallan a continuacion, segun antecedetes proporcionados por usted.", StandardFonts.TIMES_ROMAN, defaultFontSize, false));
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

            Cell cell = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForCurrency.format(detail.getPrice()));
            cell.setTextAlignment(TextAlignment.RIGHT);
            detailsTbl.addCell(cell);

            cell = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForCurrency.format(total));
            cell.setTextAlignment(TextAlignment.RIGHT);
            detailsTbl.addCell(cell);
        }

        Cell cellTxt = getRowCell("Valor Total Neto",1,3);
        cellTxt.setTextAlignment(TextAlignment.RIGHT);
        cellTxt.setBorder(Border.NO_BORDER);
        detailsTbl.addCell(cellTxt);

        cellTxt = getRowCell(IndumelecFormatter.numberFormatNoDecimalsForCurrency.format(quote.getAmount()),1,2);
        cellTxt.setTextAlignment(TextAlignment.RIGHT);
        detailsTbl.addCell(cellTxt);

        return detailsTbl;
    }

    private void setConditions(Document document, Quote quote) throws IOException{
        document.add(getParagraphBold("Condiciones Comerciales"));
        document.add (new Paragraph());
        document.add(getParagraph("1.- Valores Neto NO incluyen I.V.A y Costo de envio."));
        document.add(new Paragraph());
        document.add(getParagraph("2.- Plazo entrega en Santiago: " + getBusinessDays(quote.getEntryDate().toLocalDate(), quote.getDeliveryDate())+ " dias habiles."));
        document.add(new Paragraph());
        document.add(getParagraph("3.- Forma de pago: " + quote.getPaymentType().getDescription()+"."));
        document.add(new Paragraph());
        document.add(getParagraph("4.- Habrá que cotizar nuevamente por cualquier variación en la cantidad."));
        document.add(new Paragraph());
        document.add(getParagraph("5.- Tolerancia de construcción -+ 3 mm."));
        document.add(new Paragraph());
        document.add(getParagraph("6.- Cotización Valida por 30 días."));
        document.add(new Paragraph());
        document.add(getParagraph("7.- La construcción de los elementos es basada en la información proporcionada por el cliente, cualquier modificación necesaria posterior a la entrega del producto será a cargo del cliente."));
        document.add(new Paragraph());
        document.add(getParagraph("8.- Se invita al cliente a participar directamente en la supervisión de los procesos constructivos para Asegurar su conformidad."));
        document.add(new Paragraph());
        document.add(getParagraph("9.- Los productos desarrollados por Indumelec Ltda. Son únicos y a pedido, no se aceptan devoluciones."));
        document.add(new Paragraph());
        String deliveryText = "10.- Lugar de Entrega: ";

        if (DeliveryType.DEFAULT.equals(quote.getDeliveryType())) {
            deliveryText+= quote.getDeliveryLocation();
        } else {
            deliveryText+= quote.getDeliveryType().getDescription();
        }

        document.add(getParagraph(deliveryText));
    }

    private void setOtTbl(Document document, Quote quote) throws IOException{
        if (InvoiceType.JARA.equals(quote.getInvoice())){
            document.add(getOtJaraTbl());
        } else {
            document.add(getOtIndumelecTbl());
        }
    }

    private Table getOtJaraTbl() throws IOException{
        Table otJaraTbl = new Table(new float[] {300});

        otJaraTbl.addCell(getHeaderCell("Emitir Orden de Compra a: ", Border.NO_BORDER));
        otJaraTbl.addCell(getRowCell("Jara y Jara Cía. Ltda.", Border.NO_BORDER));
        otJaraTbl.addCell(getRowCell("Rut 76.293.536-8", Border.NO_BORDER));
        otJaraTbl.addCell(getRowCell("Dirección Av. Viel N° 1556, Santiago", Border.NO_BORDER));
        otJaraTbl.addCell(getRowCell("Telefonos 226228438 - 226211419 - 226212681", Border.NO_BORDER));

        return otJaraTbl;
    }

    private Table getOtIndumelecTbl() throws IOException{
        Table otIndumelecTbl = new Table(new float[] {300});

        otIndumelecTbl.addCell(getHeaderCell("Emitir Orden de Compra a: ", Border.NO_BORDER));
        otIndumelecTbl.addCell(getRowCell("Indumelec Ltda.", Border.NO_BORDER));
        otIndumelecTbl.addCell(getRowCell("Rut 79.688.110-0", Border.NO_BORDER));
        otIndumelecTbl.addCell(getRowCell("Dirección Einstein Nº 775 Recoleta", Border.NO_BORDER));
        otIndumelecTbl.addCell(getRowCell("Telefonos 226228438 - 226211419 - 226212681", Border.NO_BORDER));

        return otIndumelecTbl;
    }

    private void setTransferTbl(Document document, Quote quote) throws IOException{
        if (InvoiceType.JARA.equals(quote.getInvoice())){
            document.add(getTransferJaraTbl());
        } else {
            document.add(getTransferIndumelecTbl());
        }
    }

    private Table getTransferJaraTbl() throws IOException{
        Table transferJaraTbl = new Table(new float[] {300});

        transferJaraTbl.addCell(getHeaderCell("Datos para Transferencia: ", Border.NO_BORDER));
        transferJaraTbl.addCell(getRowCell("Cuenta Corriente Banco Estado 605344-1", Border.NO_BORDER));
        transferJaraTbl.addCell(getRowCell("Nombre: Jara y Jara y Cia Ltda", Border.NO_BORDER));
        transferJaraTbl.addCell(getRowCell("Rut: 76.293.536-8", Border.NO_BORDER));
        transferJaraTbl.addCell(getRowCell("Correo Electronico: Tchan@indumelec.cl", Border.NO_BORDER));

        return transferJaraTbl;
    }

    private Table getTransferIndumelecTbl() throws IOException{
        Table transferIndumelecTbl = new Table(new float[] {300});

        transferIndumelecTbl.addCell(getHeaderCell("Datos para Transferencia: ", Border.NO_BORDER));
        transferIndumelecTbl.addCell(getRowCell("Cuenta Corriente Banco de Chile N° 176 48030-07", Border.NO_BORDER));
        transferIndumelecTbl.addCell(getRowCell("Nombre: Indumelec Ltda", Border.NO_BORDER));
        transferIndumelecTbl.addCell(getRowCell("Rut: 79.688.110-0", Border.NO_BORDER));
        transferIndumelecTbl.addCell(getRowCell("Correo Electronico: Tchan@indumelec.cl", Border.NO_BORDER));

        return transferIndumelecTbl;
    }

    private void setSignature(Document document, Quote quote) throws IOException{
        document.add(getParagraph("Sin otro particular, lo saluda atentamente a usted,"));
        setSpaces(document, 2);
        document.add(getParagraphBold(quote.getSignature().getDescription(), TextAlignment.CENTER));
    }

    private void setSpaces(Document document, int spaces){
        for (int i=1; i<=spaces; i++){
            document.add(new Paragraph());
        }
    }

    private void setSpaces(Document document){
        setSpaces(document, 1);
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

    private Paragraph getParagraphBold(String text, TextAlignment textAlignment) throws IOException{
        Paragraph paragraph = new Paragraph(getBoldText(text));
        paragraph.setTextAlignment(textAlignment);

        return paragraph;
    }

    private Cell getHeaderCell(String text, Border border) throws IOException{
        Cell cellTxt = getHeaderCell(text);
        cellTxt.setBorder(border);

        return cellTxt;
    }

    private Cell getHeaderCell(String text) throws IOException{
        Cell cellTxt = new Cell().add(new Paragraph(getBoldText(text)));
        cellTxt.setPaddingLeft(10);

        return cellTxt;
    }

    private Cell getRowCell(String text, Border border) throws IOException {
        Cell cellTxt = getRowCell(text);
        cellTxt.setBorder(border);

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

    private long getBusinessDays(final LocalDate start, final LocalDate end) {
        final DayOfWeek startW = start.getDayOfWeek();
        final DayOfWeek endW = end.getDayOfWeek();

        final long days = ChronoUnit.DAYS.between(start, end);
        final long daysWithoutWeekends = days - 2 * ((days + startW.getValue())/7);

        //adjust for starting and ending on a Sunday:
        return daysWithoutWeekends + (startW == DayOfWeek.SUNDAY ? 1 : 0) + (endW == DayOfWeek.SUNDAY ? 1 : 0);
    }
}
