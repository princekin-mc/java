package top.mstudy.utils;

import lombok.extern.slf4j.Slf4j;
import top.mstudy.utils.time.ITimeHandler;
import top.mstudy.utils.time.LocalTimeHandler;
import top.mstudy.utils.time.TimeHandlerConfig;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Calendar.*;

/**
 * @author machao
 * @description: 时间的工具类，支持各种时间格式转换，时间的获取及加减等
 * @date 2022-09-02
 */
@Slf4j
public final class TimeUtils {

    public final static int[] MONTH_DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private static ITimeHandler timeHandler = null;

    private static final ThreadLocal<Map<String, SimpleDateFormat>> sdfMap = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new ConcurrentHashMap<String, SimpleDateFormat>();
        }

        ;
    };

    private static final ThreadLocal<Calendar> calendarLocal = new ThreadLocal<Calendar>() {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }

        ;
    };

    private static final String[] FORMAT_LEN = { null, null, null, null, "yyyy", null, "yyyyMM", "yyyy-MM",    // 0-7
            "yyyyMMdd", null, "yyyy-MM-dd", null, null, "yyyy-MM-dd HH", "yyyyMMddHHmmss", "yyyyMMddHHmmssS", // 8-15
            "yyyy-MM-dd HH:mm", null, null, "yyyy-MM-dd HH:mm:ss", null, "yyyy-MM-dd HH:mm:ss.S" // 16-21
    };

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private static TimeHandlerConfig timeHandlerConfig = SpringContextHolder.getBean(TimeHandlerConfig.class);

    static {
        try {
            Class<?> clz = Class.forName(timeHandlerConfig.getHandler());
            timeHandler = (ITimeHandler) clz.newInstance();
        } catch (ClassNotFoundException e) {
            timeHandler = new LocalTimeHandler();
            log.error(e.getMessage() + " 未找到，强制使用本地时间处理器！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static final String getSysDate(String format, boolean isLocal) throws Exception {
        return format(new Date(currentTimeMillis(isLocal)), format);
    }

    public static final String getSysDate(String format) throws Exception {
        return getSysDate(format, false);
    }

    public static final Timestamp getSysTimestamp() throws Exception {
        return getSysTimestamp(false);
    }

    public static final Timestamp getSysTimestamp(boolean isLocal) throws Exception {
        return new Timestamp(currentTimeMillis(isLocal));
    }

    public static final int getYear(Date date) {
        return get(date, Calendar.YEAR);
    }

    public static final int getMonth(Date date) {
        return get(date, Calendar.MONTH) + 1;
    }

    public static final int getDayOfMonth(Date date) {
        return get(date, Calendar.DATE);
    }

    public static final int getHour(Date date) {
        return get(date, Calendar.HOUR_OF_DAY);
    }

    public static final int getMinute(Date date) {
        return get(date, Calendar.MINUTE);
    }

    public static final int getSecond(Date date) {
        return get(date, Calendar.SECOND);
    }

    private static final int get(Date date, int field) {
        Calendar cal = calendarLocal.get();
        cal.setTime(date);
        int value = cal.get(field);
        return value;
    }

    public static final Calendar getSysCalendar() throws Exception {
        return getSysCalendar(false);
    }

    public static final Calendar getSysCalendar(boolean isLocal) throws Exception {
        Calendar cal = calendarLocal.get();
        cal.setTimeInMillis(currentTimeMillis(isLocal));
        return cal;
    }

    public static final long currentTimeMillis() throws Exception {
        return currentTimeMillis(false);
    }

    public static final long currentTimeMillis(boolean isLocal) throws Exception {
        return timeHandler.currentTimeMillis(isLocal);
    }

    public static final int getDayOfMonth() throws Exception {
        return getSysCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public static final int getMM() throws Exception {
        return getSysCalendar().get(Calendar.MONTH) + 1;
    }

    public static final int getYYYYMM() throws Exception {
        Calendar cal = getSysCalendar();
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static final int getYYYYMMDD() throws Exception {
        Calendar cal = getSysCalendar();
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static final long getYYYYMMDDHHMMSS() throws Exception {
        Calendar cal = getSysCalendar();
        return cal.get(Calendar.YEAR) * 10000000000L + (cal.get(Calendar.MONTH) + 1) * 100000000L
                + cal.get(Calendar.DAY_OF_MONTH) * 1000000L + cal.get(Calendar.HOUR_OF_DAY) * 10000L
                + cal.get(Calendar.MINUTE) * 100L + cal.get(Calendar.SECOND);
    }

    protected static final String getSysDate(boolean isLocal) throws Exception {
        return getSysDate(DATE_FORMAT, isLocal);
    }

    public static final String getSysDate() throws Exception {
        return getSysDate(DATE_FORMAT);
    }

    protected static final String getSysTime(boolean isLocal) throws Exception {
        return getSysDate(TIME_FORMAT, isLocal);
    }

    public static final String getSysTime() throws Exception {
        return getSysDate(TIME_FORMAT);
    }

    public static final String getTimestampFormat(String value) throws Exception {
        String format = FORMAT_LEN[value.length()];
        if (format == null) {
            throw new Exception("无法解析正确的日期格式[" + value + "]");
        }
        return format;
    }

    public static final Timestamp parse(String timeStr) throws Exception {
        return parse(timeStr, null);
    }

    public static final Timestamp parse(String timeStr, String format) throws Exception {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        if (StringUtils.isBlank(format)) {
            format = getTimestampFormat(timeStr);
        }
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        try {
            Date date = sdf.parse(timeStr);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw e;
        }
    }

    public static final String format(Date time) {
        return format(time, TIME_FORMAT);
    }

    public static final String format(Date time, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(time);
    }

    public static final String format(String time, String format) throws Exception {
        if (StringUtils.isBlank(time)) {
            return time;
        }
        if (time != null && format != null && time.length() == format.length()) {
            return time;
        }
        return format(parse(time), format);
    }

    public static final SimpleDateFormat getSimpleDateFormat(final String format) {
        try {
            return new SimpleDateFormat(format);
        } catch (Exception e) {
            throw new RuntimeException("获取日期格式类发生错误！", e);
        }
    }

    public static final int daysBetween(String dateStr1, String dateStr2) throws Exception {
        Date d1 = parse(dateStr1);
        Date d2 = parse(dateStr2);
        return daysBetween(d1, d2);
    }

    public static final int daysBetween(Date date1, Date date2) throws Exception {
        return (int) compareDate(date1, date2, DATE);
    }

    public static final int daysBetween(Calendar cal1, Calendar cal2) throws Exception {
        return (int) compareCalendar(cal1, cal2, DATE);
    }

    public static long compareDate(String date1, String date2, int field) throws Exception {
        return compareDate(parse(date1), parse(date2), field);
    }

    public static long compareDate(Date date1, Date date2, int field) throws Exception {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        return compareCalendar(c1, c2, field);
    }

    public static long compareCalendar(Calendar c1, Calendar c2, int field) throws Exception {
        long t1 = c1.getTimeInMillis();
        long t2 = c2.getTimeInMillis();

        switch (field) {
        case SECOND:
            return t1 / 1000 - t2 / 1000;
        case MINUTE:
            return t1 / 60000 - t2 / 60000;
        case HOUR:
            return t1 / 3600000 - t2 / 3600000;
        case DATE:
            int rawOffset = c1.getTimeZone().getRawOffset();
            return (t1 + rawOffset) / 86400000 - (t2 + rawOffset) / 86400000;
        case MONTH:
            return c1.get(YEAR) * 12 - c2.get(YEAR) * 12 + c1.get(MONTH) - c2.get(MONTH);
        case YEAR:
            return c1.get(YEAR) - c2.get(YEAR);
        default:
            return t1 - t2;
        }
    }

    /**
     * 获取指定日期years年后的日期
     * 日期格式要求为: yyyy-MM-dd
     *
     * @param date  日期字符串
     * @param years 增加的年数
     * @return String
     * @author
     */
    public static final String dateAddYear(String date, int years) throws Exception {
        return dateAddAmount(date, YEAR, years);
    }

    /**
     * 获取指定日期months月后的日期
     * 日期格式要求为: yyyy-MM-dd
     *
     * @param date   日期字符串
     * @param months 增加的月数
     * @return String
     * @author
     */
    public static final String dateAddMonth(String date, int months) throws Exception {
        return dateAddAmount(date, MONTH, months);
    }

    /**
     * 获取指定日期days天后的日期
     * 日期格式要求为: yyyy-MM-dd
     *
     * @param date 日期字符串
     * @param days 增加的天数
     * @return String
     * @author
     */
    public static final String dateAddDay(String date, int days) throws Exception {
        return dateAddAmount(date, DATE, days);
    }

    /**
     * 获取指定时间hours小时后的日期
     * 日期格式要求为: yyyy-MM-dd hh:mm:ss
     *
     * @param date  日期字符串
     * @param hours 增加的小时
     * @return String
     * @author
     */
    public static final String dateAddHour(String date, int hours) throws Exception {
        return dateAddAmount(date, HOUR, hours);
    }

    /**
     * 获取指定时间minutes分钟后的日期
     * 日期格式要求为: yyyy-MM-dd hh:mm:ss
     *
     * @param date
     * @param minutes
     * @return
     * @author
     */
    public static final String dateAddMinute(String date, int minutes) throws Exception {
        return dateAddAmount(date, MINUTE, minutes);
    }

    /**
     * 获取指定时间seconds秒后的日期
     * 日期格式要求为: yyyy-MM-dd hh:mm:ss
     *
     * @param date    日期字符串
     * @param seconds 增加的秒数
     * @return
     * @author
     */
    public static final String dateAddSecond(String date, int seconds) throws Exception {
        return dateAddAmount(date, SECOND, seconds);
    }

    /**
     * 日期字符串
     *
     * @param dateStr
     * @param field
     * @param amount
     * @return
     * @throws Exception
     */
    public static final String dateAddAmount(String dateStr, int field, int amount) throws Exception {
        return dateAddAmount(dateStr, field, amount, getTimestampFormat(dateStr));
    }

    public static final String dateAddAmount(String dateStr, int field, int amount, String format) throws Exception {
        String inFormat = getTimestampFormat(dateStr);//根据传入的日期字符串，得到其日期格式
        Date date = parse(dateStr, inFormat);
        Date retDate = dateAddAmount(date, field, amount);
        return format(retDate, format);
    }

    /**
     * 获取指定日期months月后的日期
     *
     * @param date   日期对象
     * @param months 增加的月数
     * @return Date
     * @author
     */
    public static final Date dateAddMonth(Date date, int months) {
        return dateAddAmount(date, MONTH, months);
    }

    /**
     * 获取指定日期days天后的日期
     *
     * @param date 日期对象
     * @param days 增加的天数
     * @return Date
     * @author
     */
    public static final Date dateAddDay(Date date, int days) {
        return dateAddAmount(date, DATE, days);
    }

    /**
     * 获取指定日期hours小时后的日期
     *
     * @param date  日期对象
     * @param hours 增加的小时数
     * @return Date
     * @author
     */
    public static final Date dateAddHour(Date date, int hours) {
        return dateAddAmount(date, HOUR, hours);
    }

    /**
     * 获取指定日期minutes分钟后的日期
     *
     * @param date    日期对象
     * @param minutes 增加的分钟数
     * @return Date
     * @author
     */
    public static final Date dateAddMinute(Date date, int minutes) {
        return dateAddAmount(date, MINUTE, minutes);
    }

    /**
     * 获取指定时间seconds秒后的日期
     *
     * @param date    日期对象
     * @param seconds 增加的秒数
     * @return
     * @author
     */
    public static final Date dateAddSecond(Date date, int seconds) throws Exception {
        return dateAddAmount(date, SECOND, seconds);
    }

    /**
     * 获取指定日期指定的类型之后的日期
     *
     * @param date   日期对象
     * @param field  指定改变的位置
     *               参见Calendar
     * @param amount
     * @return Date
     * @author
     */
    public static final Date dateAddAmount(Date date, int field, int amount) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(field, amount);
        return cd.getTime();
    }

    /**
     * 获取系统最大日期1900-01-01 00:00:00
     *
     * @return
     */
    public static final String getTheFirstDateTime() {
        return "1900-01-01 00:00:00";
    }

    /**
     * 获取系统最大日期2050-12-31 23:59:59
     *
     * @return
     */
    public static final String getTheLastDateTime() {
        return "2050-12-31 23:59:59";
    }

    /**
     * 获取系统最大月份205012
     *
     * @return
     */
    public static final String getTheLastDate205012() {
        return "205012";
    }

    /**
     * 获取系统最大日期20501231
     *
     * @return
     */
    public static final String getTheLastDate20501231() {
        return "20501231";
    }

    /**
     * 获取系统最大日期2050-12-31
     *
     * @return
     */
    public static final String getTheLastDate() {
        return "2050-12-31";
    }

    /**
     * 获取每天最大时间 23:59:59
     *
     * @return
     */
    public static final String getTheLastTime235959() {
        return " 23:59:59";
    }

    /**
     * 获取每天的初始时间 00:00:00
     *
     * @return
     */
    public static final String getTheFirstTime000000() {
        return " 00:00:00";
    }

    /**
     * 获取某个日期当月的最后一天的日期
     *
     * @param dateStr
     * @return
     */
    public static final String getLastDateOfMonth(String dateStr) {
        return getTheDateOfMonth(dateStr, 31);
    }

    /**
     * 获取某个日期当月的第一天的日期
     *
     * @param dateStr
     * @return
     */
    public static final String getFirstDateOfMonth(String dateStr) {
        return getTheDateOfMonth(dateStr, 1);
    }

    /**
     * 获取某个日期当月的最后一天的日期
     *
     * @param dateStr
     * @return
     */
    private static final String getTheDateOfMonth(String dateStr, int day) {
        int yyyy = Integer.parseInt(dateStr.substring(0, 4));

        boolean hasSep = dateStr.indexOf('-') > 0;
        int mmIndex = hasSep ? 5 : 4;
        int mm = Integer.parseInt(dateStr.substring(mmIndex, mmIndex + 2));

        int maxDay = getLastDay(yyyy * 100 + mm);
        if (day > maxDay) {
            day = maxDay;
        }
        return yyyy + (hasSep ? "-" : "") + (mm > 9 ? mm : "0" + mm) + (hasSep ? '-' : "") + (day > 9 ?
                day :
                "0" + day);
    }

    /**
     * 获取某月的天数
     *
     * @param yyyyMM
     * @return
     */
    public static final int getLastDay(int yyyyMM) {
        int yyyy = yyyyMM / 100;
        int mm = yyyyMM % 100;
        if (mm != 2) {
            return MONTH_DAYS[mm - 1];
        }
        if (yyyy % 400 == 0 || (yyyy % 100 != 0 && yyyy % 4 == 0)) {
            return 29;
        }
        return 28;
    }

    /**
     * 生成计费周期月份
     *
     * @param yyyymm:基础周期
     * @param months：增加月份
     * @return int
     */
    public static int genCycle(int yyyymm, int months) {
        int year = yyyymm / 100;
        int mon = yyyymm % 100 - 1;

        int yearMonth = year * 12 + mon;
        yearMonth += months;

        year = yearMonth / 12;
        mon = yearMonth % 12 + 1;
        return year * 100 + mon;
    }

    /**
     * 得到两个日期之间相差的月数
     *
     * @param yyyymm1
     * @param yyyymm2
     * @return int
     */
    public static int diffMonths(int yyyymm1, int yyyymm2) {
        return (yyyymm1 / 100 - yyyymm2 / 100) * 12 + (yyyymm1 % 100 - yyyymm2 % 100);
    }

    public static void set(Date time, int hourOfDay, int minute, int second) {
        Calendar cal = calendarLocal.get();
        cal.setTime(time);
        cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DATE), hourOfDay, minute, second);
        time.setTime(cal.getTimeInMillis());
    }

    public static void set(Date time, int date, int hourOfDay, int minute, int second) {
        Calendar cal = calendarLocal.get();
        cal.setTime(time);
        cal.set(cal.get(YEAR), cal.get(MONTH), date, hourOfDay, minute, second);
        time.setTime(cal.getTimeInMillis());
    }

    public static void set(Date time, int month, int date, int hourOfDay, int minute, int second) {
        Calendar cal = calendarLocal.get();
        cal.setTime(time);
        cal.set(cal.get(YEAR), month - 1, date, hourOfDay, minute, second);
        time.setTime(cal.getTimeInMillis());
    }

    public static void set(Date time, int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar cal = calendarLocal.get();
        cal.setTime(time);
        cal.set(year, month - 1, date, hourOfDay, minute, second);
        time.setTime(cal.getTimeInMillis());
    }

}

