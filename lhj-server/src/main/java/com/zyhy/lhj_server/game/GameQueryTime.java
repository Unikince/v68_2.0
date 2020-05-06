/**
 * 
 */
package com.zyhy.lhj_server.game;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zyhy.common_server.util.DateUtils;

/**
 * @author ASUS
 *
 */
public class GameQueryTime {
	private static final String toDay = "1"; // 今天
	private static final String threeDay = "3"; // 三天
	private static final String oneWeek = "7"; // 一周
	private static final String oneMonth = "30"; // 一个月
	private static final String start = "-00-00"; // 开始时间
	private static final String end = "-23-59"; // 结束时间
	
	public static Map<String, String> getTime(String time){
		Map<String, String> times = new HashMap<>();
			times.put("tableTime", time);
			times.put("startTime", time + start);
			times.put("endTime", time + end);
			return times;
			
		/*if (toDay.equals(time)) {
			String[] times = {currentTime + start , currentTime + end};
			return times;
		} else if (threeDay.equals(time)) {
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day - 3);
			String threeDay = DateUtils.format(calendar.getTime(), DateUtils.fp3);
			String[] times = {threeDay + start , threeDay + end};
			return times;
		} else if (oneWeek.equals(time)) {
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day - 7);
			String threeDay = DateUtils.format(calendar.getTime(), DateUtils.fp3);
			String[] times = {threeDay + start , threeDay + end};
			return times;
		} else if (oneMonth.equals(time)) {
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day - 30);
			String threeDay = DateUtils.format(calendar.getTime(), DateUtils.fp3);
			String[] times = {threeDay + start , threeDay + end};
			return times;
		}
		return null;*/
	}
}
