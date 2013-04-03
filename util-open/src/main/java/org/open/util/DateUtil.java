package org.open.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 处理日期、时间的共用函数类
 *
 * @author 覃芝鹏
 * @version $Id: DateUtil.java,v 1.4 2009/02/02 02:06:17 moon Exp $
 */
public class DateUtil {

    /** 日DateFormat */
    public static final String FORMAT_DAY    = "dd";

    /** 时DateFormat */
    public static final String FORMAT_HOUR   = "HH";

    /** 分DateFormat */
    public static final String FORMAT_MINUTE = "mm";

    /** 月DateFormat */
    public static final String FORMAT_MONTH  = "MM";

    /** 秒DateFormat */
    public static final String FORMAT_SECOND = "ss";

    /** 年DateFormat */
    public static final String FORMAT_YEAR   = "yyyy";

    /**
     * @see org.apache.commons.logging.Log
     */
    private static final Log   log           = LogFactory.getLog(DateUtil.class);

    /** 一秒的时长 */
    public static long         LONG_SECOND   = 1000;

    /** 一分的时长 */
    public static long         LONG_MINUTE   = 60 * LONG_SECOND;

    /** 一小时的时长 */
    public static long         LONG_HOUR     = 60 * LONG_MINUTE;

    /** 一天的时长 */
    public static long         LONG_DAY      = 24 * LONG_HOUR;

    /** 一星期的时长 */
    public static long         LONG_WEEK     = 7 * LONG_DAY;

    /**
     * 转换long类型到时,分,秒,毫秒的格式.
     *
     * @param time long type
     * @return
     */
    public static String convert(long time) {
        long ms = time % 1000;
        time /= 1000;

        int h = Integer.valueOf("" + (time / 3600));
        int m = Integer.valueOf("" + ((time - h * 3600) / 60));
        int s = Integer.valueOf("" + (time - h * 3600 - m * 60));

        return h + "小时" + m + "分" + s + "秒" + ms + "毫秒";
    }

    /**
     * 计算时间字符串 离 现在时间的间隔（天、小时、秒）
     *
     * @param defaultDate {@link #getDefault()}
     * @return 返回倒计 秒 小时 天
     */
    public static String countDown(String defaultDate) {
        try {
            Date _day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(defaultDate);

            long st = _day.getTime();
            long et = new Date().getTime();

            long time = (Math.abs(et - st)) / 1000;

            // dd=fix(time/(60*60*24))
            int d = Integer.valueOf("" + (time / 86400));
            // hh=fix((datesub-dd*60*60*24)/(60*60))
            int h = Integer.valueOf("" + (time % 86400) / 3600);
            // mm=fix((datesub-dd*60*60*24-hh*60*60)/60)
            int m = Integer.valueOf("" + ((time - d * 86400 - h * 3600) / 60));
            // ss=fix(datesub-dd*60*60*24-HH*60*60-MM*60)
            // int s = Integer.valueOf("" + (time - d * 86400 - h * 3600 - m * 60));
            // long ms = time%1000;

            String msg = "";

            /*
             * if(d!=0){ msg += (d + "天"); }
             */

            if (h != 0) {
                msg += (h + "小时");
            }

            if (m != 0) {
                msg += (m + "分");
            }

            /*
             * if(s!=0){ msg += (s + "秒"); }
             */

            return (msg == "") ? "刚刚" : (msg + ((et > st) ? "前" : "后"));

        } catch (ParseException e) {
            log.error("DateTime countDown(" + defaultDate + ") error!=>", e);
            return "刚刚";
        }
    }

    /**
     * 8位日期转换为中文日期函数，从20060909格式转换为2006年09月09日格式。
     *
     * @param str ex: 20060909
     * @return String ex: 2006年09月09日
     */
    public static String getCHSDate(String date) {
        if (date == null || date.length() != 8) return "";
        Calendar now = Calendar.getInstance();
        now.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
                Integer.parseInt(date.substring(6, 8)));
        return new SimpleDateFormat("yyyy年MM月dd日").format(now.getTime());
    }

    public static Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * 根据指定的String时间得到Date类，字符串格式:yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date getDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            log.error(BeanUtil.getMethodName(e) + "(date:" + date + ") error!", e);

            return null;
        }
    }

    /** 获得默认日期格式：yyyy-MM-dd HH:mm:ss */
    public static String getDefault() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /** 获得默认日期格式：yyyy-MM-dd HH:mm:ss */
    public static String getDefault(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
    }

    /**
     * Define the first day of week is monday.
     *
     * @return 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getFirstDayOfWeek() {
        Calendar day = Calendar.getInstance(Locale.getDefault());
        int rollDay = day.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;

        return getDefault(subtract(day.getTime(), rollDay * LONG_DAY));
    }

//    public static String getFirstDayOfMonth() {
//        Calendar day = Calendar.getInstance(Locale.getDefault());
//        int rollDay = day.get(Calendar.DAY_OF_MONTH) - Calendar.MONDAY;
//
//        return getDefault(subtract(day.getTime(), rollDay * LONG_DAY));
//    }

    /** 取得当前6位格式时间：HHmmss */
    public static String getHMS() {
        return new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
    }

    /** 取得当前8位格式日期：HH:MM:SS */
    public static String getHMS(String sep) {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /** 取得当前8位格式日期：yyyyMMdd */
    public static String getYMD() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
    }

    /** 取得当前10位格式日期：yyyy-MM-dd */
    public static String getYMD(char separator) {
        return new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd", Locale.getDefault()).format(new Date());
    }

    /** 取得当前日期和时间: yyyyMMdd HHmmss */
    public static String getYMDHMS() {
        return new SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault()).format(new Date());
    }

    /**
     * 指定date时间加上time时间后的时间：date.getTime() + time
     *
     * @param date
     * @param time
     * @return
     */
    public static Date plus(Date date, long time) {
        return new Date(date.getTime() + time);
    }

    /**
     * 指定date时间减去time时间后的时间：date.getTime() - time；
     *
     * @param date
     * @param time
     * @return
     */
    public static Date subtract(Date date, long time) {
        return new Date(date.getTime() - time);
    }
}
