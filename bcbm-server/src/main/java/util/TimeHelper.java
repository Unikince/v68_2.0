package util;

import java.util.Calendar;

/**
 * @Date: 2015年11月18日 下午1:45:24
 * @Author: zhuqd
 * @Description:
 */
public class TimeHelper {

	// public static final long DAY_MILL = 24 * 3600 * 1000L;

	/**
	 * 获取当前天数的
	 * 
	 * @return
	 */
	public static long getDayBeginMill() {
		Calendar cal0 = Calendar.getInstance();
		int year = cal0.get(Calendar.YEAR);
		int month = cal0.get(Calendar.MONTH);
		int date = cal0.get(Calendar.DAY_OF_MONTH);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(year, month, date, 0, 0, 0);
		long mill = cal1.getTimeInMillis(); // 时间精确到秒，后3为师随机的。
		mill = (mill / 1000) * 1000;
		return mill;
	}

	/**
	 * 是否是今天
	 * 
	 * @param mills
	 * @return
	 */
	public static boolean isToday(long mills) {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(mills);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		if (today != day) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是是否是同一天
	 * 
	 * @param mills0
	 * @param mills1
	 * @return
	 */
	public static boolean isSameDay(long mills0, long mills1) {
		Calendar cal0 = Calendar.getInstance();
		cal0.setTimeInMillis(mills0);
		//
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(mills1);
		//
		if (cal0.get(Calendar.YEAR) != cal1.get(Calendar.YEAR)) {
			return false;
		}
		if (cal0.get(Calendar.MONTH) != cal1.get(Calendar.MONTH)) {
			return false;
		}
		if (cal0.get(Calendar.DAY_OF_MONTH) != cal1.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取当前时间到当天结束的毫秒数
	 * 
	 * @return
	 */
	public static long getDayEndDiffMills() {
		long dayBegin = getDayBeginMill();
		return 24 * 60 * 60 * 1000L - (System.currentTimeMillis() - dayBegin);
	}

	/**
	 * 获取现在到当天0点的时间
	 * 
	 * @return
	 */
	public static long getDayWentMills() {
		long dayBegin = getDayBeginMill();
		return System.currentTimeMillis() - dayBegin;
	}

}
