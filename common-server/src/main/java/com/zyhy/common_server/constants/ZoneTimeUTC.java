/**
 * 
 */
package com.zyhy.common_server.constants;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 37979
 * 	时区格式
 */
public class ZoneTimeUTC {

	// 北京时间
	public final static ZoneId BEIJING_UTC = ZoneId.of("Asia/Shanghai");
	// 美国东部时间(纽约)
	public final static ZoneId MEIDONG_UTC = ZoneId.of("America/New_York");
	// 美国中部时间
	public final static ZoneId MEIZHONG_UTC = ZoneId.of("America/Chicago");
	// 香港时间
	public final static ZoneId XIANGGANG_UTC = ZoneId.of("Asia/Hong_Kong");
	// 台北时间
	public final static ZoneId TAIBEI_UTC = ZoneId.of("Asia/Taipei");
	// 首尔时间
	public final static ZoneId SHOUER_UTC = ZoneId.of("Asia/Seoul");
	// 日本时间
	public final static ZoneId RIBEN_UTC = ZoneId.of("Asia/Tokyo");
	
	// 时区选项
	public static Map<String, String> ZONETIMEUTCOPTION;
	static {
		ZONETIMEUTCOPTION = new HashMap<String, String>();
		ZONETIMEUTCOPTION.put("北京时间UTC+8:00", "Asia/Shanghai");
		ZONETIMEUTCOPTION.put("美东时间UTC-5:00", "America/New_York");
	}
}
