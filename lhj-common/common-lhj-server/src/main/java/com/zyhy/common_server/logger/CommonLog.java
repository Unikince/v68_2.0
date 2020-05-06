
/**
  * @Description 文件描述
  * @author LNJ
  * @date 2013-7-3 下午06:02:16 
  */

package com.zyhy.common_server.logger;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;

import com.zyhy.common_server.type.CoinType;
import com.zyhy.common_server.type.LogName;
import com.zyhy.common_server.type.LogType;


/**
 * @Description 日志管理
 * @author LNJ
 * @date 2013-7-3 下午06:02:16  
 */

public class CommonLog {
	
	private Log log = LogFactory.getLog(CommonLog.class);

	public static final String DEFAULT_VALUE = "-1";
	
	public static final String SEPARATOR = " ";
	
	private static CommonLog commonLog = null;
	private RestTemplate restTemplate;
	
	private CommonLog(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	};
	
	public static CommonLog getInstance(RestTemplate restTemplate){
		if (commonLog == null) {
			commonLog = new CommonLog(restTemplate);
		}
		return commonLog;
	}
	
	/**
	 * 本方法根据传进来的值，组装成固定格式的日志内容 格式： value1空格value2空格value3
	 * 容错：为了保证日志能够被正确解析，如果value为空，则替换成固定内容 $DEFAULT_VALUE
	 * 
	 * @param type
	 *            本条日志的类型（开头）
	 * @param value
	 * @return
	 */
	private String grenLogString(Object... values) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		try {
			for (Object v : values) {
				if (v == null || "".equals(v)) {
					v = DEFAULT_VALUE;
				}
				if (isFirst) {
					sb.append(v);
					isFirst = false;
				} else {
					sb.append(SEPARATOR);
					sb.append(v);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(sb.toString());
		return sb.toString();
	}
	
	/**
	 * @Description 游戏日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:48 
	 * @param logType
	 * @param void
	 */
	public void createGameLog(LogType logType, Object... values){
		String log = grenLogString(System.currentTimeMillis(), logType.getValue(), grenLogString(values));
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.GAME_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 货币日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:48 
	 * @param logType
	 * @param void
	 */
	public void createIngotLog(CoinType logType, Object... values){
		String log = grenLogString(System.currentTimeMillis(), logType.getType(), logType.getValue(), grenLogString(values));
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.INGOT_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 用户日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:48 
	 * @param logType
	 * @param void
	 */
	public void createUserLog(LogType logType, Object... values){
		String log = grenLogString(System.currentTimeMillis(), logType.getValue(), grenLogString(values));
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.USER_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 用户lhj日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:48 
	 * @param logType
	 * @param void
	 */
	public void createUserLhjLog(LogType logType, Object... values){
		String log = grenLogString(System.currentTimeMillis(), logType.getValue(), grenLogString(values));
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.USER_LHJ_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLhjLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 游戏lhj日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:48 
	 * @param logType
	 * @param void
	 */
	public void createGameLhjLog(LogType logType, Object... values){
		String log = grenLogString(System.currentTimeMillis(), logType.getValue(), grenLogString(values));
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.GAME_LHJ_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLhjLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 消息日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:55 
	 * @param logType
	 * @param msg
	 * @param e void
	 */
	public void createMessageLog(String msg){
		String log = grenLogString(System.currentTimeMillis(), msg);
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.MESSAGE_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
	
	/**
	 * @Description 错误日志
	 * @author LNJ
	 * @date 2013-7-3 下午06:06:55 
	 * @param logType
	 * @param msg
	 * @param e void
	 */
	public void createErrorLog(LogType logType, String msg , Throwable e){
		String log = grenLogString(System.currentTimeMillis(), logType.getValue(), msg, e);
		Map<String, String> params = new HashMap<String, String>();
		params.put("logname", LogName.ERROR_LOG.getValue());
		params.put("logstr", log);
		restTemplate.getForEntity("http://log-server/sendLog?logname={logname}&logstr={logstr}", String.class, params).getBody();
	}
}
