package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created with IntelliJ IDEA. User: hzl Date: 13-4-17 Time: 下午5:55 To change
 * this template use File | Settings | File Templates.
 */
public class DateUtil {
	public static final String FULL_TIME_PATTEN = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_TIME_PATTEN = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATE_PATTEN = "yyyy-MM-dd";
	public static final String TIME_PATTEN = "HH:mm:ss";

	public final static int MILLS_ONE_DAY = 86400000;
	public final static int MILLS_ONE_HOUR = 3600000;
	public final static int MILLS_ONE_MINUTE = 60000;

	private DateUtil() {
	}

	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getWeek(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getHours(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static boolean isSameDay(long time) {
		long now = System.currentTimeMillis();
		String date1 = new java.sql.Date(now).toString();
		String date2 = new java.sql.Date(time).toString();
		return now > time && date1.equals(date2);
	}

	public static boolean isSameDay(long time1, long time2) {
		String date1 = new java.sql.Date(time1).toString();
		String date2 = new java.sql.Date(time2).toString();

		return date1.equals(date2);
	}

	public static String mills2DateString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTEN);
		sdf.format(time * 1000).toString();
		return sdf.format(time).toString();
	}

	public static long dateString2Mills(String time) {
		if (time == null) {
			return 0;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTEN);
		Date date = null;
		try {
			date = sdf.parse(time);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date == null) {
			return 0;
		}
		return date.getTime();
	}

	@SuppressWarnings("deprecation")
	public static long getTodayMills() {
		Date pDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(pDate.getYear() + 1900, pDate.getMonth(), pDate.getDate(), 0, 0, 0);
		return calendar.getTimeInMillis();
	}

	public static boolean isInTimeRange(String beginTime, String endTime) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTEN);
		long now = System.currentTimeMillis();
		try {
			return now > format.parse(beginTime).getTime() && now < format.parse(endTime).getTime();
		} catch (ParseException e) {
			throw new IllegalArgumentException("beginTime: " + beginTime + ", endTime: " + endTime);
		}
	}

	public static String AddSeconds(int seconds) {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTEN);
		return sdf.format(new Date(today.getTime() + seconds * 1000));
	}

	/**
	 * 毫秒转日期字符串
	 *
	 * @param
	 * @return
	 */
	public static String getDateTimeByMillisecond() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 毫秒转日期字符串
	 *
	 * @param
	 * @return
	 */
	public static String getDateTimeByMillisecond(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param beginTime
	 *            开始的时间
	 * @param endTime
	 *            结束的时间
	 * @return 相差天数
	 * @throws java.text.ParseException
	 */
	public static int daysBetween(Date beginTime, Date endTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTEN);
			beginTime = sdf.parse(sdf.format(beginTime));
			endTime = sdf.parse(sdf.format(endTime));
			Calendar cal = Calendar.getInstance();
			cal.setTime(beginTime);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endTime);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Math.abs(Integer.parseInt(String.valueOf(between_days)));
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param beginTime
	 *            开始的时间
	 * @param endTime
	 *            结束的时间
	 * @return 相差天数
	 * @throws java.text.ParseException
	 */
	public static int daysBetween(long beginTime, long endTime) {
		int between_days = (int) ((endTime - beginTime) / (1000 * 3600 * 24));
		return between_days;
	}

	/**
	 * 时间转换为毫秒数
	 */
	public static long getDateTimeMillis(String beginTime) {

		try {
			SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTEN);

			return format.parse(beginTime).getTime();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 得到当前时间毫秒数
	 */
	public static long getCurMillis() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTEN);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获得本周一0点时间
	 * 
	 * @return
	 */
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 * 获得本周日24点时间
	 * 
	 * @return
	 */
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}

	/**
	 * 获得今天日期:格式为yyyyMMdd
	 */
	public static int getTodayDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		return Integer.valueOf(df.format(new Date()));
	}

	/**
	 * 获得日期:格式为yyyyMMdd
	 */
	public static int getDateDay(long dateTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		return Integer.valueOf(df.format(new Date(dateTime)));
	}

	/**
	 * 获得日期:格式为yyyy-MM-dd
	 */
	public static String get(long dateTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date(dateTime));
	}

	/**
	 * 获得当前日期
	 */
	public static long getDateMills(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTEN);
			Date d = dateFormat.parse(dateFormat.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.getTimeInMillis();
		} catch (Exception ex) {

		}
		return 0;
	}

	/**
	 * @return
	 */
	public static String DateBaseInfo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}

	/**
	 * 判断某时间为星期几,从1-7
	 */
	public static int dayForWeek(String dateTime) {
		int dayForWeek = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTEN);
			Calendar c = Calendar.getInstance();
			c.setTime(format.parse(dateTime));
			if (c.get(Calendar.DAY_OF_WEEK) == 1)
				dayForWeek = 7;
			else
				dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		} catch (Exception ex) {

		}
		return dayForWeek;
	}

	/**
	 * 判断某时间为星期几,从1-7
	 */
	public static int dayForWeek(Date dateTime) {
		int dayForWeek = 0;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(dateTime);
			if (c.get(Calendar.DAY_OF_WEEK) == 1)
				dayForWeek = 7;
			else
				dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		} catch (Exception ex) {

		}
		return dayForWeek;
	}

	/**
	 * 判断两个时间是否在同一周内: 星期一为开始日期, 星期日为结束日期
	 */
	public static boolean isSameWeek(String startTime, String endTime) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTEN);
			Date d1 = format.parse(startTime);
			Date d2 = format.parse(endTime);

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(d1);
			cal2.setTime(d2);

			int db1 = daysBetween(d2, d1);
			// 结束时间为星期几
			int intDow = dayForWeek(endTime);

			if (db1 >= 7 || db1 >= intDow)
				return false;
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * 判断两个日期是否为同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}
	public static String nowTime() {
		return new SimpleDateFormat(DATE_TIME_PATTEN).format(new Date());
	}
	public static Integer nowDay() {
		return Integer.valueOf(new SimpleDateFormat("DD").format(new Date())) ;
	}
	public static Integer nowKey() {
		return Integer.valueOf(new SimpleDateFormat("HH").format(new Date())) ;
	}
	public static Integer dayOfYear(String data) {
		try {
			//System.out.println(Integer.valueOf(new SimpleDateFormat("DD").format(new SimpleDateFormat("yyyy-MM-dd").parse(data))));
			return Integer.valueOf(new SimpleDateFormat("DD").format(new SimpleDateFormat("yyyy-MM-dd").parse(data))) ;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String infoOfOneDay(Integer data) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, data);
		return (new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
	}
	
	/**
	 * 获取指定日期的最后一秒
	 * 
	 * @param date
	 * @return
	 */
	public static long getLastSecond(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		///////////////// 得到时间的整天数

		int dayMis = 1000 * 60 * 60 * 24;// 一天的毫秒

		// 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
		long curMillisecond = date.getTime();// 当天的毫秒
		return curMillisecond + (dayMis - 60); // 获取指定日期的最后一分钟
	}
}
