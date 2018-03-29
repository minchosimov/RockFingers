package com.mincho.rockfingers.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDisplayFormat {

    private static final String DEFAULT = "dd/MM/yyyy";


    // cannot be instantiated
    private DateDisplayFormat() {
    }

    public static String getFormattedDate(Calendar calendar) {
        return getFormattedDate(DEFAULT, calendar.getTime());
    }

    /*   public static String getFormattedDate(Date date){
           return getFormattedDate(DEFAULT, date);
       }

       public static String getFormattedDate(String format, Calendar calendar){
           return getFormattedDate(format, calendar.getTime());
       }
   */
    private static String getFormattedDate(String format, Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(d);
    }

}