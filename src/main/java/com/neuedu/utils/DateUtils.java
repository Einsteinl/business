package com.neuedu.utils;

import org.joda.time.DateTime;

import java.util.Date;

//joda-time时间格式化
public class DateUtils {
    private static final String FORMAT_DAFAULT="yyyy-MM--dd HH:mm:ss";
    /**
     * 将字符串的日期转出Date
     * @param dateTimeStr
     * @param formatStr
     * @return
     */

  /*  public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.forPattern(formatStr);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.forPattern(FORMAT_DAFAULT);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }*/

    /**
     * 将Date转字符串
     */
    public static String dateToStr(Date date,String formatStr){
        if (date==null){
            return null;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }
    public static String dateToStr(Date date){
        if (date==null){
            return null;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(FORMAT_DAFAULT);
    }
}
