package com.example.cs202pz.Helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UIHelper {
    public static final String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    public static final String dateFormat = "dd.MM.yyyy";
    public static final String timeFormat = "HH:mm:ss";
    public static final String numberFormat = "#,##0.00;-#,##0.00";

    public static String dateTimePresenter(Date value) {
        return (value != null) ? new SimpleDateFormat(dateTimeFormat, Locale.US).format(value) : "";
    }

    public static String datePresenter(Date value) {
        return (value != null) ? new SimpleDateFormat(dateFormat, Locale.US).format(value) : "";
    }

    public static String timePresenter(Date value) {
        return (value != null) ? new SimpleDateFormat(timeFormat, Locale.US).format(value) : "";
    }

    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decimalPresenter(Double value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String cs = "sr-latn-cs";
        Locale locale = new Locale(cs);

        DecimalFormat decimalFormat = new DecimalFormat(numberFormat, DecimalFormatSymbols.getInstance(locale));
        return decimalFormat.format(value);
    }
}
