package com.dmg.server.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO 日期工具类
 * @Date 14:35 2019/11/6
 **/
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {
            "yyyyMMddHHmmssSSS",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM",
            "yyyy.MM.dd",
            "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm",
            "yyyy.MM",
            "yyyyMMdd",
            "yyyy",
            "MM",
            "dd",
            "yyyy年MM月dd日",
            "yyyy年MM月"
    };

    /**
     * 获取当前日期，默认格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 获取当前时间戳，默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到指定格式的当前日期字符串
     *
     * @param pattern 指定格式
     * @return
     */
    public static String getDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 得到指定格式的日期字符串
     *
     * @param date    日期
     * @param pattern 指定格式
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 日期型字符串转化为日期
     *
     * @param str
     * @return
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @Author liubo
     * @Description //TODO 获取当日0时0分0秒
     * @Date 11:34 2019/11/28
     **/
    public static Date getZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * @Author liubo
     * @Description //TODO 获取次日0时0分0秒
     * @Date 11:34 2019/11/28
     **/
    public static Date getTomorrowZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static Date getTomorrowZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }
}
