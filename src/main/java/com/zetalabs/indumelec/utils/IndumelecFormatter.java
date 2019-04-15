package com.zetalabs.indumelec.utils;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class IndumelecFormatter {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DecimalFormat numberFormat = new DecimalFormat("#,###.00");
    public static DecimalFormat numberFormatNoDecimalsForMoney = new DecimalFormat("$ #,###");
    public static DecimalFormat numberFormatNoDecimalsForQuantity = new DecimalFormat("#,###");
}
