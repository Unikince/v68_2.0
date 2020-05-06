/**
 * 
 */
package com.zyhy.lhj_server.service.tgpd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.common_server.util.StringUtils;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.game.gghz.poi.impl.GghzTemplateService;
import com.zyhy.lhj_server.game.gghz.poi.template.GghzOdds;
import com.zyhy.lhj_server.game.tgpd.TgpdDorpInfo;
import com.zyhy.lhj_server.game.tgpd.TgpdIconEnum;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.TgpdRollerWeightEnumModel_1;
import com.zyhy.lhj_server.game.tgpd.TgpdRollerWeightEnumModel_2;
import com.zyhy.lhj_server.game.tgpd.TgpdRollerWeightEnumModel_3;
import com.zyhy.lhj_server.game.tgpd.TgpdScatterInfo;
import com.zyhy.lhj_server.game.tgpd.WindowInfo;
import com.zyhy.lhj_server.game.tgpd.poi.impl.TgpdTemplateService;
import com.zyhy.lhj_server.game.tgpd.poi.template.TgpdOdds;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;

@Service
public class TgpdGameService extends BaseLogic {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private TgpdTemplateService templateService;

	// 窗口信息
	public List<List<WindowInfo>> windowInfo = new ArrayList<List<WindowInfo>>();
	public List<List<WindowInfo>> dropWindowInfo = new ArrayList<List<WindowInfo>>();
	public List<List<WindowInfo>> winInfos = new ArrayList<List<WindowInfo>>();
	/**
	 * 
	 * @param iconId 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public TgpdGameBetResult doGameProcess(String roleid, BetInfo betinfo, boolean isfree, int type, int iconId) throws ClassNotFoundException, IOException {
		TgpdGameBetResult res = new TgpdGameBetResult();
		
		// 获取图标
		List<TgpdOdds> baseinfos = templateService.getList(TgpdOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<TgpdOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			TgpdOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<List<WindowInfo>> list = new ArrayList<List<WindowInfo>>();
		List<List<WindowInfo>> dropList = new ArrayList<List<WindowInfo>>();
		int num = 0;
		if (type == 1) {
			for (int i = 0; i <= 3; i++) {
				list.add(i, new ArrayList<WindowInfo>(4));
				dropList.add(i, new ArrayList<WindowInfo>(4));
				for (int j = 0; j <= 3; j++) {
					if (isfree) {
						list.get(i).add(j, TgpdRollerWeightEnumModel_1.windowInfo(i , j ,TgpdIconEnum.FREE, baseinfos));
					} else {
						list.get(i).add(j, TgpdRollerWeightEnumModel_1.windowInfo(i , j ,list.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
				for (int j = 0; j <= 30; j++) {
					if (isfree) {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_1.windowInfo(i , j , TgpdIconEnum.FREE, baseinfos));
					} else {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_1.windowInfo(i , j , dropList.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
			}
			num = 4;
		}
		
		if (type == 2) {
			for (int i = 0; i <= 4; i++) {
				list.add(i, new ArrayList<WindowInfo>(5));
				dropList.add(i, new ArrayList<WindowInfo>(5));
				for (int j = 0; j <= 4; j++) {
					if (isfree) {
						list.get(i).add(j, TgpdRollerWeightEnumModel_2.windowInfo(i, j, TgpdIconEnum.FREE, baseinfos));
					} else {
						list.get(i).add(j, TgpdRollerWeightEnumModel_2.windowInfo(i, j,list.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
				for (int j = 0; j <= 30; j++) {
					if (isfree) {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_2.windowInfo(i , j , TgpdIconEnum.FREE, baseinfos));
					} else {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_2.windowInfo(i , j , dropList.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
			}
			num = 5;
		}
		
		if (type == 3) {
			for (int i = 0; i <= 5; i++) {
				dropList.add(i, new ArrayList<WindowInfo>(6));
				
			}
			for (int i = 0; i <= 5; i++) {
				list.add(i, new ArrayList<WindowInfo>(6));
				dropList.add(i, new ArrayList<WindowInfo>(6));
				for (int j = 0; j <= 5; j++) {
					if (isfree) {
						list.get(i).add(j, TgpdRollerWeightEnumModel_3.windowInfo(i, j, TgpdIconEnum.FREE, baseinfos));
					} else {
						list.get(i).add(j, TgpdRollerWeightEnumModel_3.windowInfo(i, j,list.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
				for (int j = 0; j <= 30; j++) {
					if (isfree) {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_3.windowInfo(i , j , TgpdIconEnum.FREE, baseinfos));
					} else {
						dropList.get(i).add(j, TgpdRollerWeightEnumModel_3.windowInfo(i , j , dropList.get(i),TgpdIconEnum.FREE, baseinfos));
					}
				}
			}
			num = 6;
		}
		
		// 检查是否中奖池游戏
		Map<String, Double> checkPoolGame = bgManagementServiceImp.checkPoolGame(18);
		// 添加奖池游戏中奖图标
		if (checkPoolGame.size() > 0) {
			for (int i = 0; i < checkPoolGame.size(); i++) {
				List<WindowInfo> list2 = list.get(RandomUtil.getRandom(0, list.size() -1));
				WindowInfo windowInfo2 = list2.get(RandomUtil.getRandom(0, list2.size()-1));
				windowInfo2.setIcon(TgpdIconEnum.POOL);
			}
		}
		
		List<List<WindowInfo>> deepCopy = deepCopy(list);
		res.setWindowinfos(deepCopy);
		res.setDropWindowInfos(dropList);
		
		// 检查免费游戏
		boolean checkFree = false;
		List<WindowInfo> checkFreeList = checkFree(deepCopy);
		if (!isfree) {
			if (checkFreeList.size() > 0) {
				checkFree = true;
			}
		}
		
		// 检查奖池游戏
		boolean checkPool = false;
		List<WindowInfo> checkPoolList = checkPool(deepCopy);
		if (checkPoolList.size() > 0) {
			checkPool = true;
			res.setPoolCount(checkPoolList.size());
		}
		
		// 检查升级道具
		List<WindowInfo> checkPropsList = checkProps(deepCopy);
		// 获取到获胜信息
		List<WinLineInfo> winInfo = getWinInfo(list,num,checkPropsList);
		// 计算奖励
		double reward = 0;
		if (winInfo.size() > 0) {
			for (WinLineInfo wl : winInfo) {
				double singleReward = NumberTool.multiply(
						NumberTool.multiply(NumberTool.multiply(betinfo.getGold(), 0.1), betinfo.getNum()), wl.getReward()).doubleValue();
				wl.setReward(singleReward);
				reward += singleReward;
			}
		}
		
		// 返回获奖图标
		List<WindowInfo> winIcon = new ArrayList<>();
		for (WinLineInfo wl : winInfo) {
			List<WindowInfo> temp = new ArrayList<>();
			if (wl.getWindows() != null) {
				for (Window window : wl.getWindows()) {
					WindowInfo wi = (WindowInfo) window;
					temp.add(wi);
				}
			}
			winIcon.addAll(temp);
		}
		if (checkFreeList.size() > 0) {
			winIcon.addAll(checkFreeList);
		}
		if (checkPoolList.size() > 0) {
			winIcon.addAll(checkPoolList);
		}
		if (checkPropsList.size() > 0) {
			winIcon.addAll(checkPropsList);
		}
		List<List<WindowInfo>> winList = winIcon(winIcon , num);
		
		// 是否掉落
		boolean checkReplenish = false;
		if (winIcon.size() > 0) {
			checkReplenish = true;
			windowInfo = deepCopy;
			dropWindowInfo = dropList;
			winInfos = winList;
		}
		res.setWinLine(winInfo);
		res.setWinrouteinfos(winList);
		res.setFree(checkFree);
		res.setPoolReward(checkPool);
		res.setPropsNum(checkPropsList.size());
		res.setReplenish(checkReplenish);
		res.setRewardcoin(reward);
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * @param list
	 * @param num
	 * @param checkPropsList 
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	protected List<WinLineInfo> getWinInfo(List<List<WindowInfo>> list , int num, List<WindowInfo> checkPropsList) throws ClassNotFoundException, IOException {
		// 对每列图标进行分组
		List<List<List<WindowInfo>>> groupIcons = new ArrayList<>();
		for (List<WindowInfo> wi : list) {
			List<List<WindowInfo>> groupIcon = groupIcon(wi);
			groupIcons.add(groupIcon);
		}
		//判断中奖
		List<List<List<WindowInfo>>> totalNums = new ArrayList<>();
		// 判断单列中奖
		for (int i = 0; i < groupIcons.size(); i++) {
			if (groupIcons.get(i).size() == 1) {
				TgpdIconEnum icon = groupIcons.get(i).get(0).get(0).getIcon();
				if (i == 0) {
					for (List<WindowInfo> list3 : groupIcons.get(i + 1)) {
						if (list3.get(0).getIcon() == icon) {
							break;
						} 
					}
				} else if (i == groupIcons.size() - 1) {
					for (List<WindowInfo> list3 : groupIcons.get(groupIcons.size() - 2)) {
						if (list3.get(0).getIcon() == icon) {
							break;
						} 
					}
				} else {
					for (List<WindowInfo> list3 : groupIcons.get(i - 1)) {
						if (list3.get(0).getIcon() == icon) {
							break;
						} 
					}
					for (List<WindowInfo> list3 : groupIcons.get(i + 1)) {
						if (list3.get(0).getIcon() == icon) {
							break;
						} 
					}
				}
				totalNums.add(groupIcons.get(i));
			}
		}
		// 判断其他中奖
		while (groupIcons.size() > 1) {
			List<List<List<WindowInfo>>> totalNum = new ArrayList<>();
			for (int i = 0; i < groupIcons.size() - 1; i++) {
				List<List<WindowInfo>> checkNum = checkNum(groupIcons.get(i) , groupIcons.get(i +1));
				totalNum.add(checkNum);
			}
			groupIcons.clear();
			groupIcons.addAll(totalNum);
			totalNums.addAll(totalNum);
		}
		
		// 去除重复结果
		List<List<WindowInfo>> cleanRe = cleanRe(totalNums);
		// 装入中奖图标
		List<WinLineInfo> winIcon = new ArrayList<>();
		for (List<WindowInfo> list2 : cleanRe) {
			if (list2.size() > 0) {
				Set<WindowInfo> temp1 = new HashSet<>();
				for (WindowInfo wi : list2) {
					temp1.add(wi);
				}
				if (temp1.size() >= num) {
					List<Window> temp2 = new ArrayList<>();
					Iterator<WindowInfo> iterator = temp1.iterator();
					while (iterator.hasNext()) {
						temp2.add(iterator.next());
					}
					WinLineInfo wl = new WinLineInfo();
					wl.setWindows(temp2);
					wl.setIcon(temp2.get(0).getIcon());
					wl.setNum(temp2.size());
					if (temp1.size() > temp2.get(0).getIcon().getTrs().length) {
						wl.setReward(temp2.get(0).getIcon().getTrs()[temp2.get(0).getIcon().getTrs().length -1]);
					} else {
						wl.setReward(temp2.get(0).getIcon().getTrs()[temp2.size() -1]);
					}
					winIcon.add(wl);
				}
			}
		}
		// 检查道具数量
		if (checkPropsList.size() > 0) {
			WinLineInfo wl = new WinLineInfo();
			wl.setIcon(checkPropsList.get(0).getIcon());
			wl.setNum(checkPropsList.size());
			winIcon.add(wl);
		}
		return winIcon;
	}
	
	/**
	 * 去除重复结果
	 * @param totalNums
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private List<List<WindowInfo>> cleanRe(List<List<List<WindowInfo>>> totalNums) {
		List<List<WindowInfo>> totalNum = new ArrayList<>();
		for (List<List<WindowInfo>> list : totalNums) {
			for (List<WindowInfo> list2 : list) {
				totalNum.add(list2);
			}
		}
		while (checkExist(totalNum)) {
			for (int i = 0; i < totalNum.size() -1; i++) {
				for (int j = i + 1; j < totalNum.size(); j++) {
					if (checkIcon(totalNum.get(i),totalNum.get(j)).size() > 0) {
						totalNum.get(i).addAll(totalNum.remove(j));
					}
				}
			}
		}
		return totalNum;
	}
	
/**
 * 判断是否有相同图标
 * @param totalNum
 * @return
 */
	private boolean checkExist(List<List<WindowInfo>> totalNum) {
		for (int i = 0; i < totalNum.size() -1; i++) {
			for (int j = i + 1; j < totalNum.size(); j++) {
				if (checkIcon(totalNum.get(i),totalNum.get(j)).size() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 将图标分组
	 * @param ws
	 * @return
	 */
	private List<List<WindowInfo>> groupIcon(List<WindowInfo> ws){
		List<List<WindowInfo>> groups = new ArrayList<>();
		while (ws.size() > 0) {
			List<WindowInfo> group = new ArrayList<>();
			WindowInfo wi = ws.remove(0);
			group.add(wi);
			while (ws.size() != 0 && wi.getIcon() == ws.get(0).getIcon()) {
				group.add(ws.remove(0));
			}
			groups.add(group); 
		}
		return groups;
	}
	
/**
 * 检查图标数量
 * @param ws1
 * @param ws2
 * @return
 */
	private List<List<WindowInfo>> checkNum(List<List<WindowInfo>> list1 , List<List<WindowInfo>> list2){
		List<List<WindowInfo>> nums = new ArrayList<>();
		for (List<WindowInfo> wis1 : list1) {
			for (List<WindowInfo> wis2 : list2) {
				List<WindowInfo> checkIcon = checkIcon(wis1 , wis2);
				if(checkIcon.size() > 0){
					nums.add(checkIcon);
				}
			}
		}
		return nums;
	}
	
	/**
	 * 检查图标是否相同
	 * @param wis1
	 * @param wis2
	 * @return
	 */
	private List<WindowInfo> checkIcon(List<WindowInfo> wis1 , List<WindowInfo> wis2){
		List<WindowInfo> num = new ArrayList<>();
		for (WindowInfo wi1 : wis1) {
			for (WindowInfo wi2 : wis2) {
				if(wi1.getIcon() == wi2.getIcon() && wi1.getIndex() == wi2.getIndex()
						&& (wi1.getId() - wi2.getId() == 1 || wi1.getId() - wi2.getId() == -1)){
					num.addAll(wis1);
					num.addAll(wis2);
					return num;
				}
			}
		}
		return num;
	}
	
	/**
	 * 返回中奖图标
	 * @param num 
	 * @param awi
	 * @param b
	 * @return
	 */
	protected List<List<WindowInfo>> winIcon(List<WindowInfo> winIcon, int num) {
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(num);
		for (int i = 0; i < num; i++) {
			winList.add(new ArrayList<WindowInfo>(num) {
				private static final long serialVersionUID = 1L;
				{
					for (int j = 0; j < num; j++) {
						add(null);
					}
				}
			});
		}
		for (WindowInfo wi : winIcon) {
			winList.get(wi.getId()).set(wi.getIndex(), wi);
		}
		return winList;
	}
	
	/**
	 * 检查是否有免费游戏
	 * @param list
	 * @return
	 */
	protected List<WindowInfo> checkFree(List<List<WindowInfo>> list){
		List<WindowInfo> checkFree = new ArrayList<>();
		for (List<WindowInfo> list2 : list) {
			for (WindowInfo wi : list2) {
				if (wi.getIcon() == TgpdIconEnum.FREE) {
					checkFree.add(wi);
				}
			}
		}
		return checkFree;
	}
	
	/**
	 * 检查是否有奖池奖励
	 * @param list
	 * @return
	 */
	protected List<WindowInfo> checkPool(List<List<WindowInfo>> list){
		List<WindowInfo> checkPool = new ArrayList<>();
		for (List<WindowInfo> list2 : list) {
			for (WindowInfo wi : list2) {
				if (wi.getIcon() == TgpdIconEnum.POOL) {
					checkPool.add(wi);
				}
			}
		}
		return checkPool;
	}
	
	/**
	 * 检查是否有升级道具
	 * @param list
	 * @return 
	 * @return
	 */
	protected List<WindowInfo> checkProps(List<List<WindowInfo>> list){
		List<WindowInfo> checkProps = new ArrayList<>();
		for (List<WindowInfo> list2 : list) {
			for (WindowInfo wi : list2) {
				if (wi.getIcon() == TgpdIconEnum.COUNT1 
						|| wi.getIcon() == TgpdIconEnum.COUNT2 || wi.getIcon() == TgpdIconEnum.COUNT3) {
					checkProps.add(wi);
				}
			}
		}
		return checkProps;
	}
	
	/**
	 * 深度复制list
	 * 
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
	private Map<String, Object> gameInfo = new ConcurrentHashMap<>();
	
	public void saveGameInfoCache(Object res, String InfoType, String roleid, Player userinfo, String uuid){
		gameInfo.put(roleid + "|" + uuid, res);
		saveInfo(res,InfoType,roleid,userinfo,uuid);
	}
	
	public void delFreeInfoCache(String roleid , String infoType, String uuid){
		gameInfo.remove(roleid + "|" + uuid);
		deleteInfo(roleid ,infoType,uuid);
	}
	
	
	/**
	 * 保存信息
	 * @param number
	 * @param roleid
	 */
	@Async
	public void saveInfo(Object res, String InfoType, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(InfoType + roleid, 
				JSONObject.toJSONString(res));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (Constants.REDIS_LHJ_TGPD_FREE.equals(InfoType) || Constants.REDIS_LHJ_TGPD_GAMEINFO.equals(InfoType)) {
				if (userinfo.getTourist() == -1) {
					redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
							JSONObject.toJSONString(res),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				} else {
					redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
							JSONObject.toJSONString(res),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			} else {
				redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
						JSONObject.toJSONString(res),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (Constants.REDIS_LHJ_TGPD_FREE.equals(InfoType) || Constants.REDIS_LHJ_TGPD_GAMEINFO.equals(InfoType)) {
				if (userinfo.getTourist() == 1) {
					redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
							JSONObject.toJSONString(res),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				} else {
					redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
							JSONObject.toJSONString(res),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			} else {
				redisTemplate.opsForValue().set(InfoType + roleid + "|" + uuid, 
						JSONObject.toJSONString(res),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
			}
		} 
	}
	
	/**
	 * 保存心跳信息
	 * @param number
	 * @param roleid
	 */
	public void saveHeartInfo(String roleid, String uuid) {
		redisTemplate.opsForValue().set(Constants.REDIS_LHJ_TGPD_HEART_DATA + roleid + "|" + uuid, "1",60*3, TimeUnit.SECONDS);
	}
	
	/**
	 * 检测是否有心跳数据
	 * @param roleid
	 * @return
	 */
	public Boolean checkHeartInfo(String roleid, String uuid){
		return redisTemplate.hasKey(Constants.REDIS_LHJ_TGPD_HEART_DATA + roleid + "|" + uuid);
	}
	
	/**
	 * 保存免费信息
	 * @param number
	 * @param roleid
	 */
	/*public void saveFree(TgpdScatterInfo bi, String roleid) {
		redisTemplate.opsForValue().set(Constants.REDIS_LHJ_TGPD_FREE + roleid, JSONObject.toJSONString(bi));
	}*/
	
	/**
	 * 保存重转信息
	 * @param number
	 * @param roleid
	 */
	/*public void saveReplenish(TgpdReplenish bi, String roleid) {
		redisTemplate.opsForValue().set(Constants.REDIS_LHJ_TGPD_REPLENISH + roleid, JSONObject.toJSONString(bi));
	}*/
	
	/**
	 * 获取游戏信息
	 */
	public TgpdGameBetResult getGameInfo(String roleid, String uuid) { 
		String str ="";
		if (gameInfo.containsKey(roleid + "|" + uuid)) {
			str = JSONObject.toJSONString(gameInfo.get(roleid + "|" + uuid));
		}
		 str = redisTemplate.opsForValue().get(
				Constants.REDIS_LHJ_TGPD_GAMEINFO + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			TgpdGameBetResult free = JSONObject.parseObject(str, TgpdGameBetResult.class);
			return free;
		}
		return null;
	}
	
	/**
	 * 获取免费信息
	 */
	public TgpdScatterInfo getFree(String roleid, String uuid) {
		String str ="";
		if (gameInfo.containsKey(roleid + "|" + uuid)) {
			str = JSONObject.toJSONString(gameInfo.get(roleid + "|" + uuid));
		}
		str = redisTemplate.opsForValue().get(
				Constants.REDIS_LHJ_TGPD_FREE + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			TgpdScatterInfo free = JSONObject.parseObject(str, TgpdScatterInfo.class);
			return free;
		}
		return null;
	}
	
	/**
	 * 获取重转信息
	 */
	public TgpdReplenish getReplenish(String roleid, String uuid) {
		String str ="";
		if (gameInfo.containsKey(roleid + "|" + uuid)) {
			str = JSONObject.toJSONString(gameInfo.get(roleid + "|" + uuid));
		}
		str = redisTemplate.opsForValue().get(
				Constants.REDIS_LHJ_TGPD_REPLENISH + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			TgpdReplenish free = JSONObject.parseObject(str, TgpdReplenish.class);
			return free;
		}
		return null;
	}
	
	/**
	 * 删除信息
	 * 
	 * @param number
	 * @param roleid
	 */
	@Async
	public void deleteInfo(String roleid , String infoType, String uuid) {
		redisTemplate.delete(infoType + roleid + "|" + uuid);
	}

}
