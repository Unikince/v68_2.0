/**
 * 
 */
package com.zyhy.lhj_server.service.hjws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.game.hjws.HjwsIconEnum;
import com.zyhy.lhj_server.game.hjws.HjwsRollerWeightEnum;
import com.zyhy.lhj_server.game.hjws.HjwsScatterInfo;
import com.zyhy.lhj_server.game.hjws.HjwsWinIndex;
import com.zyhy.lhj_server.game.hjws.poi.impl.HjwsTemplateService;
import com.zyhy.lhj_server.game.hjws.poi.template.HjwsOdds;
import com.zyhy.lhj_server.prcess.result.hjws.HjwsGameBetResult;

@Service
public class HjwsGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private HjwsTemplateService templateService;
	// 获奖窗口信息
	public List<List<WindowInfo>> wininfo = new ArrayList<List<WindowInfo>>();

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
	public HjwsGameBetResult doGameProcess(String roleid, BetInfo betinfo, boolean isfree, HjwsScatterInfo bi, int iconId) throws ClassNotFoundException, IOException {
		HjwsGameBetResult res = new HjwsGameBetResult();
		// 获取图标
		List<HjwsOdds> baseinfos = templateService.getList(HjwsOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<HjwsOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			HjwsOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<List<WindowInfo>> list = new ArrayList<List<WindowInfo>>(5);

		for (int i = 0; i <= 4; i++) {
			list.add(i, new ArrayList<WindowInfo>(3));
			for (int j = 0; j <= 2; j++) {
				list.get(i).add(j, HjwsRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
			}
		}
		
		List<List<WindowInfo>> deepCopy = deepCopy(list);
		res.setWindowinfos(deepCopy);

		// 获奖位置
		HjwsWinIndex wins = getWininfo(list, betinfo, isfree, bi,res);

		
		RewardInfo chickScatter = chickScatter(list);
		int scatter = chickScatter.getWinNum();
		// scatter奖励
		if (scatter > 2) {
			wins.setReward(NumberTool.add(wins.getReward(),
					NumberTool.multiply(HjwsIconEnum.SCATTER.getTrs()[scatter - 1], betinfo.getTotalBet())).doubleValue());
			res.getRewardInfo().add(chickScatter);
		}

		res.setRewardcoin(wins.getReward());

		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转
		if (wins.getRewardType() == 2 && wins.getNum() != 0) {
			res.setScatter(true);
			res.setScatterNum(wins.getNum());
			res.setFreeNum(wins.getNum());
		}

		if (wins.getBat() > 1) {
			res.setBat(wins.getBat());
		}

		if (wins.isSpecial()) {
			res.setSpecial(true);
			res.setSpecialRward(wins.getSpecialRward());
		}
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param res 
	 * @param betnum
	 *            线路数
	 * @return
	 */
	private HjwsWinIndex getWininfo(List<List<WindowInfo>> ws, BetInfo betinfo, boolean isfree, HjwsScatterInfo bi, HjwsGameBetResult res) {
		HjwsWinIndex win = new HjwsWinIndex();
		// 总奖励
		//double all = 0;

		// 奖励免费次数
		int number = 0;
		int winNum = 0;
		// 中奖的图标
		//List<WindowInfo> winlist = new ArrayList<WindowInfo>();
		// 检查是否有免费游戏
		for (int i = 0; i < 5; i++) {
			if (chickFree(ws.get(i))) {
				winNum++;
			}
		}

		if (isfree && winNum > 2) {
			switch (bi.getModel()) {
			case 1:
				number = 5;
				break;
			case 2:
				number = 8;
				break;
			case 3:
				number = 10;
				break;
			case 4:
				number = 15;
				break;
			case 5:
				number = 20;
				break;
			default:
				break;
			}
		} else if (winNum > 2) {
			number = 1;
		}

		/*int num = 0;
		for (int j = 0; j < 3; j++) {
			// 检查前三列是否有中奖的
			if (this.chickList(ws.get(0), ws.get(0).get(j)) && this.chickList(ws.get(1), ws.get(0).get(j))
					&& this.chickList(ws.get(2), ws.get(0).get(j))) {
				num = 3;
				// 检查第四列是否有中奖的
				if (this.chickList(ws.get(3), ws.get(0).get(j))) {
					num++;
					// 检查第五列是否有中奖的
					if (this.chickList(ws.get(4), ws.get(0).get(j))) {
						num++;
					}
				}*/
				// 奖励金币
				//double d = 0;

				//if (ws.get(0).get(j).getIcon() != HjwsIconEnum.SCATTER) {
					//d = NumberTool.multiply(betinfo.getGold(), ws.get(0).get(j).getIcon().getTrs()[num - 1])
							//.doubleValue();
				//}
				//ws.get(0).get(j).setNum(num);
				//winlist.add(ws.get(0).get(j));
				//all += d;
			//}
		//}
		
		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = winNum(ws);
		Iterator<RewardInfo> iterator = winInfo.iterator();
		while (iterator.hasNext()) {
			RewardInfo next = iterator.next();
			if (next.getWinNum() > 0) {
				for (WindowInfo wi : next.getWinIcon()) {
					wi.setNum(next.getWinNum());
				}
				totalReward += NumberTool.multiply(
						NumberTool.multiply(betinfo.getGold(), next.getWinIcon().get(0).getIcon().getTrs()[next.getWinNum() - 1]),
						next.getNum()).doubleValue();
			}else {
				iterator.remove();
			}
		}
		res.setRewardInfo(winInfo);
		
		// 进行消除
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);

		if (totalReward > 0 || number > 0) {
			winList = winIcon(winInfo);
		/*	int i, j;
			for (i = 0; i < 5; i++) {
				winList.add(new ArrayList<WindowInfo>(3) {
					private static final long serialVersionUID = 1L;
					{
						add(null);
						add(null);
						add(null);
					}
				});
				if (i <= max) {
					for (j = 0; j < 3; j++) {
						if (i == 0) {
							if (chickList(winlist, ws.get(i).get(j))
									|| ws.get(i).get(j).getIcon() == HjwsIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						} else {
							if ((chickList(winlist, ws.get(i).get(j)) && chickList(ws.get(i - 1), ws.get(i).get(j)))
									|| ws.get(i).get(j).getIcon() == HjwsIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						}
					}
				}
			}*/
		}
		
		// 免费游戏根据模式来获得总奖励
				boolean isWild = false;
				if (winList.size() > 0) {
					for (List<WindowInfo> list : winList) {
						if (list != null) {
							for (WindowInfo wi : list) {
								if (wi != null) {
									if (wi.getIcon() == HjwsIconEnum.WILD) {
										isWild = true;
									}
								}
							}
						}
					}
				}
				if (isfree && bi != null) {
					switch (bi.getModel()) {
					case 1:
						if (isWild) {
							Integer[] bet = { 10, 15, 30 };
							Integer[] lv = { 70, 20, 10 };
							int random = getRandom(bet,lv);
							win.setReward(NumberTool.multiply(totalReward, random).doubleValue());
						}else {
							win.setReward(totalReward);
						}
						break;
					case 2:
						if (isWild) {
						Integer[] bet2 = { 8, 10, 15 };
						Integer[] lv2 = { 70, 20, 10 };
						int random = getRandom(bet2,lv2);
						win.setReward(NumberTool.multiply(totalReward, random).doubleValue());
						}else {
							win.setReward(totalReward);
						}
						break;
					case 3:
						if (isWild) {
						Integer[] bet3 = { 5, 8, 10 };
						Integer[] lv3 = { 85, 10, 5 };
						int random = getRandom(bet3,lv3);
						win.setReward(NumberTool.multiply(totalReward, random).doubleValue());
						}else {
							win.setReward(totalReward);
						}
						break;
					case 4:
						if (isWild) {
						Integer[] bet4 = { 3, 5, 8 };
						Integer[] lv4 = { 68, 22, 10 };
						int random = getRandom(bet4,lv4);
						win.setReward(NumberTool.multiply(totalReward, random).doubleValue());
						}else {
							win.setReward(totalReward);
						}
						break;
					case 5:
						if (isWild) {
						Integer[] bet5 = { 2, 3, 5 };
						Integer[] lv5 = { 70, 20, 10 };
						int random = getRandom(bet5,lv5);
						win.setReward(NumberTool.multiply(totalReward, random).doubleValue());
						}else {
							win.setReward(totalReward);
						}
						break;
					default:
						break;
					}
				} else {
					win.setReward(totalReward);
				}

				// 检测免费游戏1+5特殊奖励
				if (isfree && chickGoldGame(ws.get(0)) && chickGoldGame(ws.get(4))) {
					Integer[] allbet = { 2, 5, 15, 20, 50 };
					int bet = RandomUtil.getRandom(allbet);
					double reward = NumberTool.multiply(betinfo.getGold(), bet).doubleValue();
					win.setSpecialRward(reward);
					win.setReward(NumberTool.add(win.getReward(), reward).doubleValue());
					win.setSpecial(true);
				}
				
				win.setRewardType(2);
				win.setNum(number);
		
		win.setWindows(winList);

		return win;
	}
	
 public int getRandom(Integer[] bet , Integer[] lv){
	 List<Integer> random = new ArrayList<>();
	 
	 for (int i = 0; i < lv.length; i++) {
		for (int j = 0; j < lv[i]; j++) {
			random.add(bet[i]);
		}
	}
	 
	return random.get(RandomUtil.getRandom(0, random.size()-1));
	
}
	/**
	 * 获胜线路数量
	 * 
	 * @return
	 */
	public List<RewardInfo> winNum(List<List<WindowInfo>> ws) {
		List<RewardInfo> ri = new ArrayList<>();
		List<List<WindowInfo>> wiLlist = new ArrayList<>();
		for (int i = 0; i <= 2; i++) {
			RewardInfo ari = new RewardInfo();
			List<WindowInfo> icon = new ArrayList<>();
			for (int j = 1; j <= 4; j++) {
				List<WindowInfo> list = checkIcon(ws.get(0).get(i), ws.get(j));
				wiLlist.add(j - 1 , list);
			}
			int total = 0;
			if (wiLlist.get(0).size() != 0 && wiLlist.get(1).size() != 0) {
				total = wiLlist.get(0).size() * wiLlist.get(1).size();
				icon.add(ws.get(0).get(i));
				icon.addAll(wiLlist.get(0));
				icon.addAll(wiLlist.get(1));
				ari.setWinNum(3);
				if (wiLlist.get(2).size() != 0) {
					total = total * wiLlist.get(2).size();
					icon.addAll(wiLlist.get(2));
					ari.setWinNum(4);
					if (wiLlist.get(3).size() != 0) {
						total = total * wiLlist.get(3).size();
						icon.addAll(wiLlist.get(3));
						ari.setWinNum(5);
					}
				}
				ari.setNum(total);
				ari.setWinIcon(icon);
				for (WindowInfo wl : icon) {
					Icon icon2 = wl.getIcon();
					if (icon2 != HjwsIconEnum.WILD) {
						ari.setIcon(icon2);
						break;
					}
				}
				ri.add(ari);
			}
		}
		return ri;
	}

	/**
	 * 检查图标是否相同
	 * 
	 * @param awi
	 * @param b
	 * @return
	 */
	public List<WindowInfo> checkIcon(WindowInfo w, List<WindowInfo> wiList) {
		Icon icon = w.getIcon();
		List<WindowInfo> list = new ArrayList<>();
		for (WindowInfo wi : wiList) {
			if ((wi.getIcon() == icon || wi.getIcon() == HjwsIconEnum.WILD)
					&& (icon != HjwsIconEnum.SCATTER && icon != HjwsIconEnum.WILD)) {
				list.add(wi);
			}
		}
		return list;
	}
	
	/**
	 * 返回中奖图标
	 * @param awi
	 * @param b
	 * @return
	 */
	public List<List<WindowInfo>> winIcon(List<RewardInfo> winInfo) {
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);
		for (int i = 0; i < 5; i++) {
			winList.add(new ArrayList<WindowInfo>(3) {
				private static final long serialVersionUID = 1L;
				{
					add(null);
					add(null);
					add(null);
				}
			});
		}
		for (RewardInfo re : winInfo) {
			if(re.getWinIcon() != null){
				for (WindowInfo wi : re.getWinIcon()) {
					winList.get(wi.getId()).set(wi.getIndex(), wi);
				}
			}
			
		}
		return winList;
	}
	/**
	 * 检查免费游戏
	 * @param list
	 * @return
	 */
	public boolean chickFree(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == HjwsIconEnum.SCATTER) {
				w.setNum(88);
				return true;
			}
		}
		return false;
	}
	
	
	private Map<String, HjwsScatterInfo> freeGameInfo = new ConcurrentHashMap<>();
	
	public void saveFreeInfoCache(HjwsScatterInfo bi, String roleid, Player userinfo, String uuid){
		freeGameInfo.put(roleid + "|" + uuid, bi);
		save(bi,roleid,userinfo,uuid);
	}
	
	public void delFreeInfoCache(String roleid, String uuid){
		freeGameInfo.remove(roleid + "|" + uuid);
		deleteInfo(roleid,uuid);
	}
	
	/**
	 * 保存免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	@Async
	public void save(HjwsScatterInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.HJWS_REDIS_SCATTER + roleid, 
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
		
	}
	
	/**
	 * 获取免费信息
	 * @param roleid
	 * @return
	 */
	public HjwsScatterInfo getData(String roleid, String uuid) {
		if (freeGameInfo.containsKey(roleid + "|" + uuid)) {
			return freeGameInfo.get(roleid + "|" + uuid);
		}
		String str = redisTemplate.opsForValue().get(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid);
		if (str != null) {
			HjwsScatterInfo bi = JSONObject.parseObject(str, HjwsScatterInfo.class);
			return bi;
		}
		return null;
	}
	
	/**
	 * 删除免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	@Async
	public void deleteInfo(String roleid, String uuid) {
		redisTemplate.delete(Constants.HJWS_REDIS_SCATTER + roleid + "|" + uuid);
	}

	public boolean chickList(List<WindowInfo> list, WindowInfo info) {
		for (WindowInfo w : list) {
			if (w.getIcon() == info.getIcon()
					|| (w.getIcon() == HjwsIconEnum.WILD && info.getIcon() != HjwsIconEnum.SCATTER)) {
				return true;
			}
		}

		return false;
	}

	public boolean chickWild(List<Integer> list, int i) {
		for (int w : list) {
			if (w == i) {
				return true;
			}
		}

		return false;
	}

	public RewardInfo chickScatter(List<List<WindowInfo>> list) {
		RewardInfo r = new RewardInfo();
		 List<WindowInfo> winIcon = new ArrayList<>();
		 
		 for (int i = 0; i < list.size(); i++) {
			 for (WindowInfo wl : list.get(i)) {
				 if (wl.getIcon() == HjwsIconEnum.SCATTER) {
						winIcon.add(wl);
						break;
					}
			}
		}
		 r.setIcon(HjwsIconEnum.SCATTER);
		 r.setNum(1);
		 r.setWinIcon(winIcon);
		 r.setWinNum(winIcon.size());
		return r;
	}

	public boolean chickGoldGame(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == HjwsIconEnum.A2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 深度复制list
	 * 
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
}
