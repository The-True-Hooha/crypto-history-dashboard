package com.github.TheTrueHooha.cryptohistorydashboard.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utility {
    //converts the time stamp to date
    public static String convertTimeStampToDate(Long timeStamp){

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        final String formattedDate = Instant.ofEpochSecond(timeStamp).atZone(ZoneId.of("UTC +1")).format(dateTimeFormatter);
        return formattedDate;
    }

    public static double round(double value, int places){
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
