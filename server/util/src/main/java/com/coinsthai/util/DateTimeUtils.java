package com.coinsthai.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtils {
    
    public final static String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    
    public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    
    public final static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    
    public final static String yyyy_MM_dd = "yyyy-MM-dd";
    
    public final static String yyyy_M_d_H_m_s_S = "yyyy-M-d H:m:s:S";
    
    public final static String yyyy_M_d_H_m_s = "yyyy-M-d H:m:s";
    
    public final static String yyyy_M_d_H_m = "yyyy-M-d H:m";
    
    public final static String yyyy_M_d = "yyyy-M-d";
    
    public final static String yyyyMMdd_HH_mm_ss_SSS = "yyyyMMdd HH:mm:ss:SSS";
    
    public final static String yyyyMMdd_HH_mm_ss = "yyyyMMdd HH:mm:ss";
    
    public final static String yyyyMMdd_HH_mm = "yyyyMMdd HH:mm";
    
    public final static String yyyyMMdd = "yyyyMMdd";
    
    public final static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    
    public final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    
    public final static String yyyyMMddHHmm = "yyyyMMddHHmm";
    
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    
    public static Date stringToDate(String dateString,
                                    String style) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(style, Locale.CHINESE);
        return format.parse(dateString);
    }
    
    public static String dateToString(Date date, String style) {
        if (null == date) {
            return "";
        }
        
        SimpleDateFormat format = new SimpleDateFormat(style, Locale.CHINESE);
        return format.format(date);
    }
    
    public static int getIntervalDays(Date startDay, Date endDay) {
        Date startDate = startDay;
        Date endDate = endDay;
        if (startDay.after(endDay)) {
            Date cal = startDay;
            startDate = endDay;
            endDate = cal;
        }
        long sl = startDate.getTime();
        long el = endDate.getTime();
        long ei = el - sl;
        return (int) (ei / (1000 * 60 * 60 * 24));
    }
    
    public static String stringToString(String dateString,
                                        String sourceStyle,
                                        String tagetStyle) throws ParseException {
        Date date = stringToDate(dateString, sourceStyle);
        return dateToString(date, tagetStyle);
    }
    
    public static String getSomeDateToString(String dateString,
                                             int num,
                                             String dateStyle) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(sdf.parse(dateString));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        day.add(Calendar.DATE, num);
        return sdf.format(day.getTime());
    }
    
    public static Date getSomeDate(Date date, int num) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd);
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(sdf.parse(dateToString(date, yyyy_MM_dd)));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        day.add(Calendar.DATE, num);
        return stringToDate(sdf.format(day.getTime()), yyyy_MM_dd);
    }
    
    public static int daysOfTwoDate(Date beginDate, Date endDate) {
        int days = 0;
        
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        while (beginCalendar.before(endCalendar)) {
            days++;
            beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days == 0 ? 1 : days;
    }
    
    public static List getListMonth(Date beginDate, Date endDate) {
        List list = new ArrayList();
        Calendar beginCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        beginCal.setTime(beginDate);
        while (beginCal.before(endCal)) {
            list.add(format(beginCal.getTime(), "yyyy-MM"));
            beginCal.add(Calendar.MONTH, 1);
        }
        if (beginCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH)) {
            list.add(format(beginCal.getTime(), "yyyy-MM"));
        }
        return list;
    }
    
    public static String getSystemTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                             Locale.SIMPLIFIED_CHINESE);
        return df.format(new Date()).toString();
    }
    
    public static String getNowTime() {
        Calendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(date);
    }
    
    public static Calendar getCalendarDate(String dateString,
                                           String dateStyle) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        
        Calendar day = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateString);
            date.setTime(date.getTime() + 8 * 60 * 60 * 1000);
            day.setTime(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }
    
    public static boolean dateCompare(String beforeTime,
                                      String currentTime,
                                      int time) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                                 Locale.SIMPLIFIED_CHINESE);
            boolean temp = false;
            Date begin = df.parse(beforeTime);
            Date end = df.parse(currentTime);
            long between = (end.getTime() - begin.getTime()) / 1000;
            long day = between / (24 * 3600);
            long hour = between % (24 * 3600) / 3600;
            long minute = between % 3600 / 60;
            // long second = between % 60 / 60;
            if ((hour == 0) && (day == 0) && (minute <= time)) {
                temp = true;
            }
            return temp;
        }
        catch (ParseException e) {
            return false;
        }
    }
    
    public static double changeDecimal(int num, double value) {
        BigDecimal b = new BigDecimal(value);
        double v = b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v;
    }
    
    public static String getSpecificDateByDay(Date nativeDate, int calDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nativeDate);
        cal.add(Calendar.DATE, calDay);
        
        Date specificDate = cal.getTime();
        String specificDateStr = dateToString(specificDate, "yyyy-MM-dd");
        
        return specificDateStr;
    }
    
    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);
        lastDate.add(Calendar.MONTH, 1);
        lastDate.add(Calendar.DATE, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    public static String getOneMonthDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    public static String getMondayOFWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0) {
            dayofweek = 7;
        }
        c.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        return sdf.format(c.getTime());
        
    }
    
    public static String getSaturday() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0) {
            dayofweek = 7;
        }
        c.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        return sdf.format(c.getTime());
        
    }
    
    public static int getWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static int getCountWeek() {
        Calendar calendar = Calendar.getInstance();
        int max = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
        return max;
    }
    
    public static Date setFullPassDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    public static Date getFallBack2Hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.HOUR_OF_DAY,
                     calendar.get(Calendar.HOUR_OF_DAY) - 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getTimeHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static long getDiffDays(Date startDate, Date endDate) {
        long difftime = endDate.getTime() - startDate.getTime();
        return difftime / (24L * 60L * 60L * 1000L);
    }
    
    public static Date getStartDateOfCurrentDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getStartYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getStartDateOfNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getStartDateOfNextSevenDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getStartDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getStartDateOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static List<Date> getStaticByDateDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfCurrentDay(date);
        Date nextday = getStartDateOfNextDay(date);
        int step = 2;
        dates.add(startdate);
        for (int i = 1; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.HOUR_OF_DAY, i * step);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }
    
    public static List<Date> getStaticByWeekDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfCurrentDay(date);
        Date nextday = getStartDateOfNextSevenDay(date);
        dates.add(startdate);
        for (int i = 1; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }
    
    public static List<String> getStaticByWeekLabel(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        List<String> dates = new ArrayList<String>();
        Date startdate = getStartDateOfCurrentDay(date);
        // Date nextday = getStartDateOfNextSevenDay(date);
        dates.add(dateFormat.format(startdate));
        for (int i = 1; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(dateFormat.format(calendar.getTime()));
        }
        return dates;
    }
    
    public static List<Date> getStaticByMonthDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfMonth(date);
        Date nextday = getStartDateOfNextMonth(date);
        long daydiff = getDiffDays(startdate, nextday);
        dates.add(startdate);
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }
    
    public static List<Date> getStaticBySE(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<Date>();
        
        long daydiff = getDiffDays(startDate, endDate);
        dates.add(startDate);
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(endDate);
        return dates;
    }
    
    public static List<String> getStaticByMonthLabel(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        List<String> dates = new ArrayList<String>();
        Date startdate = getStartDateOfMonth(date);
        Date nextday = getStartDateOfNextMonth(date);
        long daydiff = getDiffDays(startdate, nextday);
        dates.add(dateFormat.format(startdate));
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(dateFormat.format(calendar.getTime()));
        }
        return dates;
    }
    
    public static String formatDate(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    
}
