package com.dmg.lobbyserver.common.util.weChat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParseUtils {

	public static String datePareseToStringOne(long time) {
		String timeStr = null;
		if (time == 0) {
			return null;
		}
		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		timeStr = sdf.format(date);
		return timeStr;
	}

	public static String datePareseToStringTwo(long time) {
		String timeStr = null;
		if (time == 0) {
			return null;
		}

		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		timeStr = sdf.format(date);

		return timeStr;
	}

	public static String datePareseToStringThree(long time) {
		String timeStr = null;
		if (time == 0) {
			return null;
		}

		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		timeStr = sdf.format(date);

		return timeStr;

	}

	public static String datePareseToStringFour(long time) {
		String timeStr = null;
		if (time == 0) {
			return null;
		}

		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		timeStr = sdf.format(date);

		return timeStr;
	}
}