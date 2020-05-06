/**
 * 
 */
package com.zyhy.lhj_server.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.JsonMesssageUtil;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.dao.imp.ShowRecordDaoImp;
import com.zyhy.lhj_server.game.GameNameEnum;
import com.zyhy.lhj_server.game.ShowRecord;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun 请求类型:http-post 消息格式:json
 */
@RestController
//@CrossOrigin
public class HttpJsonMessageController {

	@Autowired
	private MainProcess mainProcess;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private ShowRecordDaoImp showRecordDaoImp;
	@Autowired
	private StringRedisTemplate redisTemplate;
	private static final Logger LOG = LoggerFactory.getLogger("LhjController");
	private static CacheManager cache = CacheManager.instance();
	// 老虎机服务状态(0为关闭,1为开启)
	//private static int serverStatu = 1;

	@HystrixCommand(fallbackMethod = "fallback")
	@RequestMapping(value = "/m", method = RequestMethod.GET)
	public HttpMessageResult messageRequestMethod_Get(String data , HttpServletRequest  request) throws IOException, InterruptedException {
		return messageRequestMethod_Post(data);
	}

	@HystrixCommand(fallbackMethod = "fallback")
	@RequestMapping(value = "/m", method = RequestMethod.POST)
	public HttpMessageResult messageRequestMethod_Post(String data) throws IOException, InterruptedException {
		/*if (serverStatu == 0) {
			HttpMessageResult fallback = fallback(data);
			LOG.info("老虎机因为某些异常被关闭!");
			return fallback;
		}*/
		long begintime = System.currentTimeMillis();
		try {
			String c = data;
			if (c != null && c.indexOf("\n") != -1) {
				c = c.replaceAll("\n", "");
			}
			MessageClient mc = null;
			mc = JsonMesssageUtil.genClientMessage(c);
			LOG.info("MESSAGE: " + mc.toString());
			// 检查登录态
			// if
			// (!CacheConnection.getInstance(restTemplate).checkSessionIdByUuid(mc.getUuid(),
			// mc.getSid())) {
			// HttpMessageResult mr = new HttpMessageResult();
			// mr.setRet(6);
			// mr.setMsg("账号重复登录,请关注账号安全");
			// return mr;
			// }
			
			// 检测游戏是否开启
			/*String gameRedisName = MessageIdEnum.getRedisNameByMessageId(mc.getMessageid());
			if (gameRedisName != null) {
				SoltGameInfo gameInfo = cache.getGameInfo(gameRedisName);
				if (gameInfo.getState() == 0) {
					HttpMessageResult fallback = fallback(data);
					return fallback;
				}
			}*/
			
			HttpMessageResult result = mainProcess.mainProcessMsg(mc);
			long endtime = System.currentTimeMillis();
			//CommonLog.getInstance(restTemplate).createMessageLog(mc.getMessageid() + " " + (endtime - begintime) + "ms");
			// 结果 1=成功,2=失败,3=重新登陆,4=游戏币不足,5=钻石不足,6=重复登陆,7=游戏失效,8=实名认证
			LOG.info("RETURN :" + result.getRet() + ":" + result.getMsg() + ", " + mc.getMessageid() + " " + (endtime - begintime) + "ms");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			HttpMessageResult mr = new HttpMessageResult();
			mr.setRet(2);
			mr.setMsg("服务错误");
			return mr;
		}
	}
	
	/**
	 * 设置老虎机服务状态
	 * 
	 */
	/*@RequestMapping(value="/state", method = RequestMethod.GET)
	public static void setServerStatu(String data,HttpServletRequest  request) {
		String state = request.getParameter("state");
		serverStatu = Integer.parseInt(state);
		LOG.info("老虎机服务当前状态为: {}",serverStatu);
	}*/
	
	/**
	 * 获取ip归宿地
	 * @return
	 */
	@RequestMapping(value = "getIpLocal")
	public String getIpLocal(HttpServletRequest  request) {
		//String result = HttpURLUtils.doGet("http://ip.taobao.com/service/getIpInfo.php?ip="+IpUtils.getIpAddress(request),"utf-8");
		//LOG.info("查询ip地址返回: {}",result);
       return null;
	}
	/**
	 * 查询玩家游戏记录
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="/showRecord", method = RequestMethod.POST)
	public List<List<GameLhjLog>> showRecord(String data) throws ParseException{
		try {
			String c = data;
			if (c != null && c.indexOf("\n") != -1) {
				c = c.replaceAll("\n", "");
			}
			JSONObject jsonObject = JSONObject.parseObject(c);
			LOG.info("QueryRecord: " + jsonObject);
			// 角色id
			String roleid = jsonObject.get("roleid").toString();
			// 角色平台id
			String uuid = jsonObject.get("uuid").toString();
			// 角色信息
			Player userInfo = userService.getUserInfo(roleid, uuid);
			// 游客不能进行查询
			if (Constants.PLATFORM_EBET.equals(uuid)) {
				// Ebet游客为-1
				if (userInfo.getTourist() == -1) {
					return isTourist();
				}
			} else if (Constants.PLATFORM_V68.equals(uuid)) {
				// V68游客为1
				if (userInfo.getTourist() == 1) {
					return isTourist();
				}
			}
			// 游戏名称
			String game = jsonObject.get("game").toString();
			// 查询天数
			String time = jsonObject.get("time").toString();
			
			// 玩家正常退出,删除战绩记录
			if (Integer.parseInt(time) == 0) {
				LOG.info("DeleteData : " + Constants.LHJGAME_RECORD + roleid);
				redisTemplate.delete(Constants.LHJGAME_RECORD + roleid);
				return null;
			}
			
			// 每页显示数量
			String limit = jsonObject.get("limit").toString();
			// 页面索引
			String index = jsonObject.get("index").toString();
			// 查询状态(0为查询,1为关闭)
			//String state = jsonObject.get("state").toString();
			// 查询识别码
			String code = jsonObject.get("code").toString();
			// 游戏名字
			String gameName = GameNameEnum.getById(game);
			// 查询天数
			long currentTimeMillis = System.currentTimeMillis();
			Date currentDate = new Date(currentTimeMillis);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = sdf.format(currentDate);
			
			// 在已查询出的记录中获取记录
			if (redisTemplate.opsForHash().hasKey(Constants.LHJGAME_RECORD + roleid, code)) {
				// 获取记录
				Object object = redisTemplate.opsForHash().get(Constants.LHJGAME_RECORD + roleid, code);
				ShowRecord showRecord = JSONObject.parseObject(object.toString(), ShowRecord.class);
				List<List<GameLhjLog>> resultList = new ArrayList<>();
				List<GameLhjLog> list = showRecord.getQueryResults().get(Integer.parseInt(index));
				GameLhjLog gl = new GameLhjLog();
				gl.setFujia(code);
				gl.setRoleid(index);
				list.add(gl);
				resultList.add(list);
				return resultList;
			} else {
				// 查询前先清除之前缓存记录
				redisTemplate.delete(Constants.LHJGAME_RECORD + roleid);
				// 查询出的记录
				List<GameLhjLog> queryResult = queryResult(roleid, uuid, gameName, time, currentTime, sdf);
				if (queryResult.size() == 0) {
					return null;
				}
				
				//Map<String, String> Times = GameQueryTime.getTime(currentTime);
				/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm");
				Date date = new Date(endTime);
				String format = sdf.format(date);
				System.out.println("格式化后的时间" + format);*/
				
				// 从最后日期的战绩开始显示
				Collections.reverse(queryResult);
				// 将战绩分页
				List<List<GameLhjLog>> queryResults = new ArrayList<>();
				int num = NumberTool.divide(queryResult.size(), Integer.parseInt(limit)).setScale(0,BigDecimal.ROUND_CEILING).intValue();
				// 查询记录识别码
				String newCode = roleid + "|" + num + "|" +  System.currentTimeMillis();
				for (int i = 0; i < num; i++) {
					if (queryResult.size() < Integer.parseInt(limit)) {
						queryResults.add(queryResult);
						break;
					}
					List<GameLhjLog> indexs = new ArrayList<>();
					ListIterator<GameLhjLog> listIterator = queryResult.listIterator();
					int count = 0;
					while (listIterator.hasNext()) {
						indexs.add(listIterator.next());
						count ++;
						listIterator.remove();
						if (count == Integer.parseInt(limit)) {
							queryResults.add(indexs);
							break;
						}
					}
				}
				LOG.info("pageNum : " + queryResults.size());
				// 缓存记录
				redisTemplate.opsForHash().put(Constants.LHJGAME_RECORD + roleid, newCode, JSON.toJSONString(new ShowRecord(queryResults)));
				redisTemplate.expire(Constants.LHJGAME_RECORD + roleid, 1000*60*60, TimeUnit.MILLISECONDS);
				
				// 返回战绩
				List<List<GameLhjLog>> resultList = new ArrayList<>();
				List<GameLhjLog> list = queryResults.get(Integer.parseInt(index));
				GameLhjLog gl = new GameLhjLog();
				gl.setFujia(newCode);
				gl.setRoleid(index);
				list.add(gl);
				resultList.add(list);
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("finally")
	public List<GameLhjLog> queryResult(
			String roleid, String uuid, String gameName, String time, String currentTime, SimpleDateFormat sdf) throws ParseException{
		String toDay = "1"; // 今天
		//String threeDay = "3"; // 三天
		//String oneWeek = "7"; // 一周
		//String oneMonth = "30"; // 一个月
		String start = "-00-00"; // 开始时间
		String end = "-23-59"; // 结束时间
		String tableTime = "";
		String startTime = "";
		String endTime = "";
		List<GameLhjLog> showRecords = new ArrayList<>();
		try {
			if (toDay.equals(time)) {
				tableTime = currentTime;
				startTime = currentTime + start;
				endTime = currentTime + end;
				LOG.info("tableTime: " + tableTime);
				//System.out.println("startTime :" + startTime);
				//System.out.println("endTime :" + endTime);
				// 开始时间
				Long startTimeParse = null;
				if(StringUtils.isNotEmpty(startTime)){
					Date date = DateUtils.parseDate(startTime, "yyyy-MM-dd-HH-mm");
					startTimeParse = date.getTime();
				}
				//System.out.println("startTimeParse :" + startTimeParse);
				// 结束时间
				Long endTimeParse = null;
				if(StringUtils.isNotEmpty(endTime)){
					Date date = DateUtils.parseDate(endTime, "yyyy-MM-dd-HH-mm");
					endTimeParse = date.getTime();
				}
				//System.out.println("endTimeParse :" + endTimeParse);
				long before = System.currentTimeMillis();
				List<GameLhjLog> showRecord = showRecordDaoImp.showRecord(tableTime, roleid, uuid, gameName, startTimeParse, endTimeParse);
				long after = System.currentTimeMillis();
				LOG.info("RecordNum: " + showRecord.size() + ", QueryTime: " + (after - before) + "ms");
				showRecords.addAll(showRecord);
			} else {
				for (int i = 0; i < Integer.parseInt(time); i ++) {
					Calendar calendar = Calendar.getInstance();
					int today = calendar.get(Calendar.DAY_OF_YEAR);
					calendar.set(Calendar.DAY_OF_YEAR, today - i);
					String calendarTime = sdf.format(calendar.getTime());
					tableTime = calendarTime;
					startTime = calendarTime + start;
					endTime = calendarTime + end;
					LOG.info("tableTime: " + tableTime);
					//System.out.println("startTime :" + startTime);
					//System.out.println("endTime :" + endTime);
					// 开始时间
					Long startTimeParse = null;
					if(StringUtils.isNotEmpty(startTime)){
						Date date = DateUtils.parseDate(startTime, "yyyy-MM-dd-HH-mm");
						startTimeParse = date.getTime();
					}
					//System.out.println("startTimeParse :" + startTimeParse);
					// 结束时间
					Long endTimeParse = null;
					if(StringUtils.isNotEmpty(endTime)){
						Date date = DateUtils.parseDate(endTime, "yyyy-MM-dd-HH-mm");
						endTimeParse = date.getTime();
					}
					//System.out.println("endTimeParse :" + endTimeParse);
					long before = System.currentTimeMillis();
					List<GameLhjLog> showRecord = showRecordDaoImp.showRecord(tableTime, roleid, uuid, gameName, startTimeParse, endTimeParse);
					long after = System.currentTimeMillis();
					LOG.info("RecordNum: " + showRecord.size() + ", QueryTime: " + (after - before) + "ms");
					if (showRecord.size() > 0) {
						showRecords.addAll(showRecord);
					}else {
						continue;
					}
				}
			}
		} finally {
			return showRecords;
		}
	}
	
	/**
	 * 游客不能进行记录查询
	 * @return
	 */
	private List<List<GameLhjLog>> isTourist() {
		List<List<GameLhjLog>> showRecords = new ArrayList<>();
		 List<GameLhjLog> showRecord = new ArrayList<>();
		GameLhjLog gameLhjLog = new GameLhjLog();
		gameLhjLog.setFujia("游客账号");
		 showRecord.add(gameLhjLog);
		 showRecords.add(showRecord);
		 return showRecords;
	}
	
	/**
	 * 服务降级
	 * @param data
	 * @return
	 */
	public HttpMessageResult fallback(String data) {
		HttpMessageResult mr = new HttpMessageResult();
		mr.setRet(2);
		mr.setMsg(Constants.LHJGAME_NAME + "关闭");
		return mr;
	}

	/*public static int getServerStatu() {
		return serverStatu;
	}*/
}
