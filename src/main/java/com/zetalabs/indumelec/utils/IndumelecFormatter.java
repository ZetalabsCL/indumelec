package com.zetalabs.indumelec.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class IndumelecFormatter {
    public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("es-CL"));
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DecimalFormat numberFormat = new DecimalFormat("#,###", symbols);
    public static DecimalFormat numberFormatForCurrency = new DecimalFormat("$ #,###", symbols);
    public static DecimalFormat numberFormatNoDecimalsForCurrency = new DecimalFormat("$ #,###", symbols);
    public static DecimalFormat numberFormatNoDecimalsForQuantity = new DecimalFormat("#,###", symbols);
}
