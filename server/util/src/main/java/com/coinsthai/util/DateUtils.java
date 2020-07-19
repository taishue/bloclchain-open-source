package com.coinsthai.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间工具类
 */
public class DateUtils {

    private DateUtils() {
    }

    private static final String weekNames[] = {"星期日",
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六"};

    public static String getDateStr(Date date, String formatStr) {
        if (date == null || StringUtils.isBlank(formatStr)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * getDateStr get a string with format YYYY-MM-DD from a Date object
     *
     * @param date date
     * @return String
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 得到四位年份字符串
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * 得到两位月份字符串
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }

    /**
     * 得到两位日期字符串
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String getDateStrCn(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public static String getDateStrCnWithWeek(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月dd日");
        String str = format.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return str + " " + weekNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getDateWeekStrCn(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 星期w");
        return format.format(date);
    }

    public static String getDateStrCompact(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(date);
        return str;
    }

    /**
     * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date
     * object
     *
     * @param date date
     * @return String
     */
    public static String getDateTimeStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getDateTimeStrCn(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.format(date);
    }

    public static String getCurDateStr(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    /**
     * Parses text in 'YYYY-MM-DD' format to produce a date.
     *
     * @param s the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseDate(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(s);
    }

    public static Date parseDateC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.parse(s);
    }

    /**
     * 将格式如“30/九月/10 3:14 下午”的字符串转换成时间
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static Date parseDateL(String s) throws ParseException {
        return parseDate(s, "dd/MMMM/yy h:mm a");
    }

    public static Date parseDate(String dateStr,
                                 String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateStr);
    }

    /**
     * 转换日期，不抛出异常
     *
     * @param dateStr
     * @param pattern
     * @return 如果转换错误，则返回null
     */
    public static Date parseDateSilent(String dateStr, String pattern) {
        try {
            return parseDate(dateStr, pattern);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parses text in 'yyyy-MM-dd HH:mm:ss' format to produce a date.
     *
     * @param s the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseDateTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseDateTimeWx(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.parse(s);
    }

    public static Date parseDateTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.parse(s);
    }

    /**
     * Parses text in 'HH:mm:ss' format to produce a time.
     *
     * @param s the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH时mm分ss秒");
        return format.parse(s);
    }

    public static int yearOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(0, 4));
    }

    public static int monthOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(5, 7));
    }

    public static int dayOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(8, 10));
    }

    /**
     * 得到日期是周几
     *
     * @param d
     * @return
     */
    public static int weekdayOfDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDateTimeStr(java.sql.Date date, double time) {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        String dateStr = year + "-" + month + "-" + day;
        Double d = new Double(time);
        String timeStr = String.valueOf(d.intValue()) + ":00:00";

        return dateStr + " " + timeStr;
    }

    /**
     * Get the total month from two date.
     *
     * @param sd the start date
     * @param ed the end date
     * @return int month form the start to end date
     * @throws ParseException
     */
    public static int diffDateM(Date sd, Date ed) throws ParseException {
        return (ed.getYear() - sd.getYear()) * 12 + ed.getMonth()
                - sd.getMonth()
                + 1;
    }

    public static int diffDateD(Date sd, Date ed) throws ParseException {
        return Math.round((ed.getTime() - sd.getTime()) / 86400000) + 1;
    }

    /**
     * 取得两个日期的天数之差 <br>
     * 时分秒等会被忽略
     *
     * @param sd
     * @param ed
     * @return
     */
    public static int diffDateOD(Date sd, Date ed) {

        // 临时解决一个奇怪的2011-02-12与2011-02-14的结果是-1的问题
        Date sd1, ed1;
        try {
            sd1 = parseDate(getDateStr(sd));
        } catch (ParseException e) {
            sd1 = sd;
        }
        try {
            ed1 = parseDate(getDateStr(ed));
        } catch (ParseException e) {
            ed1 = ed;
        }

        Calendar sc = Calendar.getInstance();
        sc.setTime(sd1);
        sc.set(Calendar.HOUR, 0);
        sc.set(Calendar.MINUTE, 0);
        sc.set(Calendar.SECOND, 0);
        sc.set(Calendar.MILLISECOND, 0);

        Calendar ec = Calendar.getInstance();
        ec.setTime(ed1);
        ec.set(Calendar.HOUR, 0);
        ec.set(Calendar.MINUTE, 0);
        ec.set(Calendar.SECOND, 0);
        ec.set(Calendar.MILLISECOND, 0);

        return (int) ((ec.getTime().getTime() - sc.getTime().getTime())
                / 86400000);
    }

    public static int diffDateM(int sym, int eym) throws ParseException {
        return (Math.round(eym / 100) - Math.round(sym / 100)) * 12
                + (eym % 100 - sym % 100) + 1;
    }

    public static java.sql.Date getNextMonthFirstDate(java.sql.Date date) throws ParseException {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.set(Calendar.DATE, 1);
        return new java.sql.Date(scalendar.getTime().getTime());
    }

    public static java.sql.Date getFrontDateByDayCount(java.sql.Date date,
                                                       int dayCount) throws ParseException {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.DATE, -dayCount);
        return new java.sql.Date(scalendar.getTime().getTime());
    }

    /**
     * Get first day of the month.
     *
     * @param year  the year
     * @param month the month
     * @return Date first day of the month.
     * @throws ParseException
     */
    public static Date getFirstDay(String year,
                                   String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(year + "-" + month + "-1");
    }

    public static Date getFirstDay(int year, int month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(year + "-" + month + "-1");
    }

    /**
     * Get first day of the month.
     *
     * @param date the date
     * @return Date first day of the month.
     */
    public static Date getFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 得到当月的第一天
     *
     * @return
     */
    public static Date getFirstDay() {
        Date today = new Date();
        try {
            return getFirstDay(getYear(today), getMonth(today));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getLastDay(String year,
                                  String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(year + "-" + month + "-1");

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    public static Date getLastDay(int year, int month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(year + "-" + month + "-1");

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    /**
     * getToday get todat string with format YYYY-MM-DD from a Date object
     *
     * @return String
     */

    public static String getTodayStr() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        return getDateStr(calendar.getTime());
    }

    public static Date getToday() throws ParseException {
        return new Date(System.currentTimeMillis());
    }

    public static String getTodayAndTime() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    public static String getTodayC() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        return getDateStrCn(calendar.getTime());
    }

    public static int getThisYearMonth() throws ParseException {
        Date today = Calendar.getInstance().getTime();
        return (today.getYear() + 1900) * 100 + today.getMonth() + 1;
    }

    public static int getYearMonth(Date date) throws ParseException {
        return (date.getYear() + 1900) * 100 + date.getMonth() + 1;
    }

    // 获取相隔月数
    public static long getDistinceMonth(String beforedate,
                                        String afterdate) throws ParseException {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long monthCount = 0;
        try {
            Date d1 = d.parse(beforedate);
            Date d2 = d.parse(afterdate);

            monthCount = (d2.getYear() - d1.getYear()) * 12 + d2.getMonth()
                    - d1.getMonth();
            // dayCount = (d2.getTime()-d1.getTime())/(30*24*60*60*1000);

        } catch (ParseException e) {
            System.out.println("Date parse error!");
            // throw e;
        }
        return monthCount;
    }

    // 获取相隔天数
    public static long getDistinceDay(String beforedate,
                                      String afterdate) throws ParseException {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long dayCount = 0;
        try {
            Date d1 = d.parse(beforedate);
            Date d2 = d.parse(afterdate);

            dayCount = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            System.out.println("Date parse error!");
            // throw e;
        }
        return dayCount;
    }

    // 获取相隔天数
    public static long getDistinceDay(Date beforedate,
                                      Date afterdate) throws ParseException {
        long dayCount = 0;

        try {
            dayCount = (afterdate.getTime() - beforedate.getTime())
                    / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            // System.out.println("Date parse error!");
            // // throw e;
        }
        return dayCount;
    }

    public static long getDistinceDay(java.sql.Date beforedate,
                                      java.sql.Date afterdate) throws ParseException {
        long dayCount = 0;

        try {
            dayCount = (afterdate.getTime() - beforedate.getTime())
                    / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            // System.out.println("Date parse error!");
            // // throw e;
        }
        return dayCount;
    }

    // 获取相隔天数
    public static long getDistinceDay(String beforedate) throws ParseException {
        return getDistinceDay(beforedate, getTodayStr());
    }

    // 获取相隔时间数
    public static long getDistinceTime(String beforeDateTime,
                                       String afterDateTime) throws ParseException {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long timeCount = 0;
        try {
            Date d1 = d.parse(beforeDateTime);
            Date d2 = d.parse(afterDateTime);

            timeCount = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000);

        } catch (ParseException e) {
            System.out.println("Date parse error!");
            throw e;
        }
        return timeCount;
    }

    // 获取相隔时间数
    public static long getDistinceTime(String beforeDateTime) throws ParseException {
        return getDistinceTime(beforeDateTime,
                               new Timestamp(System.currentTimeMillis()).toLocaleString());
    }

    /**
     * 取得指定日期的开始时间
     *
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取得指定日期的结束时间
     *
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 在指定的日期上加N天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 在指定的日期上加N天
     *
     * @param date
     * @param days
     * @return
     */
    public static String addDay(String date, int days) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Date oldDate = null;
        try {
            oldDate = parseDate(date);
        } catch (ParseException e) {
            oldDate = new Date();
        }
        Date newDate = addDay(oldDate, days);
        return getDateStr(newDate);
    }

    /**
     * 在指定日期后增加N个工作日
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addWorkDay(Date date, int days) {
        Date sd = date;
        Date ed;
        int d = days;
        do {
            if (days < 0 && d > 0) {
                d = 0 - d; // 如果是得到之前的日期
            }
            ed = addDay(sd, d); // 得到N天后的日期
            d = getHolidays(sd, ed); // 得到两个日期间的假期数
            sd = ed;
        }
        while (d != 0);
        return ed;
    }

    /**
     * 在指定日期后增加N个工作日
     *
     * @param date
     * @param days
     * @return
     */
    public static String addWorkDay(String date, int days) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Date oldDate = null;
        try {
            oldDate = parseDate(date);
        } catch (ParseException e) {
            oldDate = new Date();
        }
        Date newDate = addWorkDay(oldDate, days);
        return getDateStr(newDate);
    }

    /**
     * 增加小时。
     *
     * @param add
     */
    public static Date addHour(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, add);
        return cal.getTime();
    }

    /**
     * 增加毫秒。
     *
     * @param add
     */
    public static Date addMillisecond(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, add);
        return cal.getTime();
    }

    /**
     * 增加分钟。
     *
     * @param add
     */
    public static Date addMinute(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, add);
        return cal.getTime();
    }

    /**
     * 增加月。
     *
     * @param add
     */
    public static Date addMonth(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, add);
        return cal.getTime();
    }

    /**
     * 增加秒。
     *
     * @param add
     */
    public static Date addSecond(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, add);
        return cal.getTime();
    }

    /**
     * 增加年份。
     *
     * @param add
     */
    public static Date addYear(Date date, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, add);
        return cal.getTime();
    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public static Date getNextMonday(Date date) {
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        }
        while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result.getTime();
    }

    /**
     * 得到指定时间段内的工作日天数 <br>
     * 不计算开始日期，只排除掉周末，未排除法定节假日
     *
     * @param startDate
     * @param endDate
     * @return 如果endDate比startDate在前，即可能返回负值。
     */
    public static int getWorkDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        Date sd = startDate;
        Date ed = endDate;
        int allDays = DateUtils.diffDateOD(startDate, endDate);
        if (allDays < 0) {
            sd = endDate;
            ed = startDate;
        }

        int startOffset = 0; // 开始日期的偏移量
        int endOffset = 0; // 结束日期的偏移量

        // 日期在一周里的序号，周日为0
        int sw = DateUtils.weekdayOfDate(sd);
        int ew = DateUtils.weekdayOfDate(ed);

        if (sw != 1 && sw != 7) {// 开始日期为星期六和星期日时偏移量为0
            startOffset = 6 - sw;
        }

        if (ew != 1 && ew != 7) {// 结束日期为星期六和星期日时偏移量为0
            endOffset = 6 - ew;
        }

        int result = (diffDateOD(getNextMonday(sd), getNextMonday(ed)) / 7) * 5
                + startOffset - endOffset;

        if (allDays < 0) {
            result = 0 - result;
        }
        return result;
    }

    /**
     * 得到时间段内假期的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getHolidays(Date startDate, Date endDate) {
        int allDays = diffDateOD(startDate, endDate);
        int workDays = getWorkDays(startDate, endDate);
        return allDays - workDays;
    }
}
