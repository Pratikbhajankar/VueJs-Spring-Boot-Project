package com.inventory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CommonUtils {

  public static ZonedDateTime stringToDate(String date, String format) throws ParseException {
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    final Date dateParse = simpleDateFormat.parse(date);
    return ZonedDateTime.ofInstant(dateParse.toInstant(), ZoneId.systemDefault());
  }

  public static String dateToString(ZonedDateTime date, String format){
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
    return date.format(dateTimeFormatter);
  }

  public static String dateToString(Date date, String format){
    return dateToString(date.toInstant().atZone(ZoneId.systemDefault()),format);
  }
  public static Long dayDiff(ZonedDateTime startDate,ZonedDateTime endDate){
    return ChronoUnit.DAYS.between(startDate,endDate);
  }
}
