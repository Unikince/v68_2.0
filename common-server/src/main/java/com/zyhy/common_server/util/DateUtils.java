package com.zyhy.common_server.util;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description:时间工具类
 * @author nanjun.li
 * @date 2011-6-23
 */
public class DateUtils {
	public final static long ONE_DAY_MS = 24 * 60 * 60 * 1000;
	public final static String fp1 = "yyyy-MM-dd HH:mm:ss";
	public final static String fp2 = "yyyyMMddHHmmss";
	public final static String fp3 = "yyyy-MM-dd";
	public final static String fp4 = "HH:mm:ss";
	
	private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<String, DateTimeFormatter>();
	
	static{
		DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern(fp1);
		FORMATTER_CACHE.put(fp1, formatter_1);
		DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern(fp2);
		FORMATTER_CACHE.put(fp2, formatter_2);
		DateTimeFormatter formatter_3 = DateTimeFormatter.ofPattern(fp3);
		FORMATTER_CACHE.put(fp3, formatter_3);
		DateTimeFormatter formatter_4 = DateTimeFormatter.ofPattern(fp4);
		FORMATTER_CACHE.put(fp4, formatter_4);
	}
	
	/*** 定时器启动的默认时间 为次日的凌晨 */
	public static final LocalDateTime TIMER_DEFAULT_START_DATE;
	static {
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.plusDays(1);
		localDateTime = getDesignLocalDateTime(localDateTime.getYear(), localDateTime.getMonthValue(),
				localDateTime.getDayOfMonth(), 0, 0, 0, 0);
		TIMER_DEFAULT_START_DATE = localDateTime;
	}
	
	/**
	 * 创建时间格式化操作类
	 * @param pattern 格式
	 * @return
	 */
	private static DateTimeFormatter createCacheFormatter(String pattern){
		if (pattern == null || pattern.length() <= 0) {
			throw new IllegalArgumentException("Invalid pattern null or length <= 0");
		}
		DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern(pattern);
			DateTimeFormatter oldFormatter = FORMATTER_CACHE.putIfAbsent(pattern, formatter);
			if (oldFormatter != null) {
				formatter = oldFormatter;
			}else {
				throw new IllegalArgumentException("Invalid pattern create error");
			}
		}
		return formatter;
	}
	
	/**
	 * Date日期转换为格式化时间
	 * @param date 日期
	 * @param pattern 格式
	 * @return
	 */
	public static String format(Date date, String pattern){
		return format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()), pattern);
	}
	
	/**
	 * LocalDateTime 转换为格式化时间
	 * @param localDateTime 日期
	 * @param pattern 格式
	 * @return
	 */
	public static String format(LocalDateTime localDateTime, String pattern){
		DateTimeFormatter formatter = createCacheFormatter(pattern);
		return formatter.format(localDateTime);
	}

	/**
	 * 根据时区获取当前毫秒值
	 *
	 * @param zone ZoneTimeUTC
	 * @return
	 */
	public static long getEpochMilliByZoneUTC(ZoneId zone) {
		Clock clock = Clock.system(zone);
		return clock.millis();
	}

	/**
	 * 格式化时间转换为Date
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String time, String pattern){
		LocalDateTime localDateTime = parseLocalDateTime(time, pattern);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
     * 格式化字符串转为LocalDateTime
     * @param time 格式化时间
     * @param pattern 格式
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String time, String pattern){
        DateTimeFormatter formatter = createCacheFormatter(pattern);
        return LocalDateTime.parse(time, formatter);
    }
    
    /**
     * 获取指定日期
     * @param year 年
     * @param month 月
     * @param dayOfMonth 日
     * @return
     */
    public static LocalDate getDesignLocalDate(int year, int month, int dayOfMonth){
    	LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
    	return localDate;
    }
    
    /**
     * 获取指定时间
     * @param hour 时
     * @param minute 分
     * @param second 秒
     * @param nanoOfSecond 毫秒
     * @return
     */
    public static LocalTime getDesignLocalTime(int hour, int minute, int second, int nanoOfSecond){
    	LocalTime localTime = LocalTime.of(hour, minute, second, nanoOfSecond);
    	return localTime;
    }
    
    /**
     * 获取指定时间
     * @param localDate 日期
     * @param localTime 时间
     * @return
     */
    public static LocalDateTime getDesignLocalDateTime(LocalDate localDate, LocalTime localTime){
    	LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
    	return localDateTime;
    }
    
    /**
     * 获取指定时间
     * @param year 年
     * @param month 月
     * @param dayOfMonth 日
     * @param hour 时
     * @param minute 分
     * @param second 秒
     * @param nanoOfSecond 毫秒
     * @return
     */
    public static LocalDateTime getDesignLocalDateTime(int year, int month, int dayOfMonth,
    		int hour, int minute, int second, int nanoOfSecond){
    	LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
    	return localDateTime;
    }
    
    /**
     * 获取指定日期偏移指定类型之后的日期
     * @param localDate 指定日期
     * @param designType 偏移类型(1=天,2=周,3=月,4=年)
     * @param day 偏移天数(正数偏移未来,负数偏移过去)
     * @return
     */
    public static LocalDate getDesignLocalDateByDays(LocalDate localDate, int designType, int designDays){
    	if (localDate == null) {
			throw new IllegalArgumentException("Invalid localDate is null");
		}
    	if (designDays == 0) {
    		throw new IllegalArgumentException("Invalid designDays is 0");
		}
    	LocalDate nowDate = null;
    	switch (designType) {
		case 1:
			// 按天偏移
			if (designDays > 0) {
				nowDate = localDate.plusDays(designDays);
			}else {
				nowDate = localDate.minusDays(designDays > 0 ? designDays : -designDays);
			}
			break;
		case 2:
			// 按周偏移
			if (designDays > 0) {
				nowDate = localDate.plusWeeks(designDays);
			}else {
				nowDate = localDate.minusWeeks(designDays > 0 ? designDays : -designDays);
			}
			break;
		case 3:
			// 按月偏移
			if (designDays > 0) {
				nowDate = localDate.plusMonths(designDays);
			}else {
				nowDate = localDate.minusMonths(designDays > 0 ? designDays : -designDays);
			}
			break;
		case 4:
			// 按年偏移
			if (designDays > 0) {
				nowDate = localDate.plusYears(designDays);
			}else {
				nowDate = localDate.minusYears(designDays > 0 ? designDays : -designDays);
			}
			break;
		}
    	return nowDate;
    }
    
    /**
     * 判断指定日期是否是闰年
     * @param localDate 日期
     * @return
     */
    public static boolean getLocalDateIsLeapYear(LocalDate localDate){
    	return localDate.isLeapYear();
    }
    
    /**
     * 获取指定时间的毫秒值
     * @param localDateTime
     * @return
     */
    public static long getCurrentTimeMillis(LocalDateTime localDateTime){
    	final Clock clock = Clock.systemUTC();
    	return clock.millis();
    }
    
    /**
     * 获取两个日期相差的毫秒值
     * @param time1
     * @param time2
     * @return
     */
    public static long getLocalDateTimeBetweenTimes(LocalDateTime time1, LocalDateTime time2){
    	long betweentime = Duration.between(time1, time2).toMillis();
    	return betweentime > 0 ? betweentime : -betweentime;
    }
}
