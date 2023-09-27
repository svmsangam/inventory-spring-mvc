package com.inventory.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getMonthInitials(){
        String currentMonth;
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        currentMonth = monthFormat.format(currentDate);
        return currentMonth;

    }

    public static String getMonthFull(){
        String currentMonth;
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        currentMonth = monthFormat.format(currentDate);
        return currentMonth;

    }
}
