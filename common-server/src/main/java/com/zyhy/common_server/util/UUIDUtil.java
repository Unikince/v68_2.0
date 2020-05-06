package com.zyhy.common_server.util;

import java.util.UUID;

/*
 Copyright:   Copyright (c)
 Company:     UUME Ico
 @author:     Hugh
 @version:    1.0
 Modification History:
 Date	   Author		Version		Description
 ------------------------------------------------------------------
 2006-08-24 	Hugh		1.0		Initialize Version.
 */
public class UUIDUtil {

	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static String createNewUUID(int length) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < length; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isLetterOrDigit(ch))
				sb.append(ch);
			else
				j--;
		}
		return sb.toString();
	}

	/**
	 * 生成一个16位的数字的唯一Id
	 * @return
	 */
	public static String createNumberUUid(){
		long currenttime = System.currentTimeMillis();
		String begin = String.valueOf(currenttime);
		String substr = begin.substring(5, begin.length());
		return substr + RandomUtil.getRandom(10000000, 99999999);
		
	}
	
	/**
	 * 
	 * @Discription 兑换码生成,移除掉大写字母O和阿拉伯数字0
	 * @author nanjun.li       
	 * @created 2017-3-24 上午10:42:00     
	 * @param length
	 * @return
	 */
	public static String createCodeUUID(int length) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < length; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isLetterOrDigit(ch))
				sb.append(ch);
			else
				j--;
		}
		String str = sb.toString();
		str = str.replace("O", "");
		str = str.replace("0", "");
		str = str.replace("I", "");
		str = str.replace("l", "");
		str = str.replace("1", "");
		return sb.toString();
	}
	
	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static String createNewUUID() {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 10; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isLetterOrDigit(ch))
				sb.append(ch);
			else
				j--;
		}
		return sb.toString();
	}

	/**
	 * 生成唯一的真实UUID
	 * 
	 * @return
	 */
	public static String createRealUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static int createDigitUUID(int length) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < length; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isDigit(ch)){
				sb.append(ch);
			}
			else {
				j--;
			}
		}
		return Integer.valueOf(sb.toString());
	}
	
	/**
	 * 随机小写字母加数字
	 * @param length
	 * @return
	 */
	public static String createTicketUUID(int length) {
		String str = "";
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < length; i++) {
				String charOrNum = RandomUtil.random(2) % 2 == 0 ? "char" : "num";
				// 输出字母还是数字
				char ch = 0;
				if ("char".equalsIgnoreCase(charOrNum)) {
					// 输出是大写字母还是小写字母
					ch = (char) (RandomUtil.random(26) + 97);
				} else if ("num".equalsIgnoreCase(charOrNum)) {
					ch = (char) (RandomUtil.random(10) + 48);
				}
				if (Character.isLetterOrDigit(ch)) {
					sb.append(ch);
				} else {
					i--;
				}

			}
			str = sb.toString();
			str = str.replace("l", "");
			str = str.replace("1", "");
		} while (str.length() != length);
		return str;
	}
}
