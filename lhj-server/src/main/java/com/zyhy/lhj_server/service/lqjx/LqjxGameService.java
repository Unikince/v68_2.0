/**
 * 
 */
package com.zyhy.lhj_server.service.lqjx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import com.zyhy.lhj_server.game.lqjx.LqjxIconEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxReplenish;
import com.zyhy.lhj_server.game.lqjx.LqjxRollerWeightEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxScatterInfo;
import com.zyhy.lhj_server.game.lqjx.LqjxWildEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxWildLvEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxWinIndex;
import com.zyhy.lhj_server.game.lqjx.poi.impl.LqjxTemplateService;
import com.zyhy.lhj_server.game.lqjx.poi.template.LqjxOdds;
import com.zyhy.lhj_server.prcess.result.lqjx.LqjxGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class LqjxGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private LqjxTemplateService templateService;
	// 是否已经计算免费游戏
	//public boolean isScatter = false;

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
	public LqjxGameBetResult doGameProcess(String roleid, BetInfo betinfo, boolean isfree, int iconId) throws ClassNotFoundException, IOException {
		LqjxGameBetResult res = new LqjxGameBetResult();
		// 获取图标
		List<LqjxOdds> baseinfos = templateService.getList(LqjxOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<LqjxOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			LqjxOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<List<WindowInfo>> list = new ArrayList<List<WindowInfo>>(5);
		// 不是免费游戏的时候2-4列随机全是wild
		int ran = RandomUtil.getRandom(2, 3);
		// 不是免费游戏的时候3-5列随机全是wild
		int allwild = RandomUtil.getRandom(1, LqjxWildEnum.values().length);
		int[] wilds = LqjxWildEnum.getById(allwild).getIndex();
		boolean isWild = false;
		boolean AllWild = false;
		if (!isfree) {
			// 全是wild的概率
			int random = RandomUtil.getRandom(1, 10000);
			LqjxWildLvEnum e = LqjxWildLvEnum.getLvById(iconId);
			if (e == null) {
				e = LqjxWildLvEnum.D;
			}
			int count = 0;
			if (random > 10000 - e.getLv1()) {
				AllWild = true;
				count = 1;
				if (random > 10000 - e.getLv2()) {
					count = 2;
				}
			}
			
			if (count == 1) {
				wilds =  LqjxWildEnum.getById(RandomUtil.getRandom(1,3)).getIndex();
			} else if (count == 2) {
				wilds =  LqjxWildEnum.getById(RandomUtil.getRandom(4,6)).getIndex();
			} 

			if (AllWild) {
				int i, j;
				for (i = 0; i <= 4; i++) {
					list.add(i, new ArrayList<WindowInfo>(3));
					for (j = 0; j <= 2; j++) {
						if (useArraysBinarySearch(wilds, i)) {
							// 直接赋值wild
							if (!res.getGod().contains(i)) {
								res.getGod().add(i);
							}
							list.get(i).add(j, new WindowInfo(i, j , LqjxIconEnum.WILD));
						} else {
							list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
						}
					}
				}
			} else {
				int i, j;
				for (i = 0; i <= 4; i++) {
					list.add(i, new ArrayList<WindowInfo>(3));
					for (j = 0; j <= 2; j++) {
						list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
					}
				}
			}
		} else {
			// 免费游戏走正常流程
			int i, j;
			for (i = 0; i <= 4; i++) {
				list.add(new ArrayList<WindowInfo>(3));
				for (j = 0; j <= 2; j++) {
					list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
				}
			}
		}
		
		List<List<WindowInfo>> deepCopy = deepCopy(list);
		res.setWindowinfos(deepCopy);

		// 获奖位置
		LqjxWinIndex wins = getWininfo(list, betinfo, betinfo.getTotalBet(),res);

		int scatter = 0;
		for (int i = 0; i < 5; i++) {
			if (chickFree(list.get(i))) {
				scatter++;
			}
		}

		// scatter结算
		double scatterReward = 0;
		if (scatter > 2) {
			scatterReward = NumberTool.multiply(LqjxIconEnum.SCATTER.getTrs()[scatter - 1], betinfo.getTotalBet()).doubleValue();
			res.setScattericon(scatterReward);
			res.setScattericonnum(scatter);
		}
		res.setRewardcoin(NumberTool.add(wins.getReward(), scatterReward).doubleValue());
		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转
		if (!isfree && wins.getNum() > 0) {
			res.setScatter(true);
			res.setScatterNum(wins.getNum());
		}

		if (AllWild) {
			wins.setAllWild(true);

			for (int i : wilds) {
				wins.getWild().add(i);
				wins.getRep().getWild().add(i);
			}
		}

		if (isWild) {
			wins.getRep().getWild().add(ran);
			res.setIsWild(ran);
		}

		if (wins.getRep() != null && wins.isLqjxReplenish()) {
			res.setReplenish(wins.isLqjxReplenish());
			res.setRep(wins.getRep());
		}
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param totalBet
	 * @param res 
	 * @param betnum
	 *            线路数
	 * @return
	 */
	private LqjxWinIndex getWininfo(List<List<WindowInfo>> ws, BetInfo betinfo, double totalBet, LqjxGameBetResult res) {
		LqjxWinIndex win = new LqjxWinIndex();
		// 奖励免费次数
		int number = 0;
		int winNum = 0;
		int[] scatterNum = { 0, 0, 15, 20, 25 };
		// 检查是否有免费游戏
		for (int i = 0; i < 5; i++) {
			if (chickFree(ws.get(i))) {
				winNum++;
			}
		}
		if (winNum > 2) {
			number = scatterNum[winNum - 1];
		}

		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = winNum(ws);
		Iterator<RewardInfo> iterator = winInfo.iterator();
		while (iterator.hasNext()) {
			RewardInfo next = iterator.next();
			if (next.getWinNum() > 0) {
				totalReward += NumberTool.multiply(
						NumberTool.multiply(betinfo.getTotalBet(), next.getWinIcon().get(0).getIcon().getTrs()[next.getWinNum() - 1]),
						next.getNum()).doubleValue();
			}else {
				iterator.remove();
			}
		}
		res.setRewardInfo(winInfo);
		win.setReward(totalReward);
		win.setNum(number);

		// 进行消除
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);
		// 普通中奖
		if (totalReward > 0 || number > 0) {
			winList = winIcon(winInfo,ws);
		}
		// 是否会补充数据
		LqjxReplenish rep = new LqjxReplenish(betinfo);
		// 普通中奖
		if (totalReward > 0) {
			rep.setNumber(rep.getNumber() + 1);
			win.setLqjxReplenish(true);
			wininfo = ws;
		} else {
			rep.setNumber(0);
			win.setLqjxReplenish(false);
		}
		win.setRep(rep);
		win.setWindows(winList);
		return win;
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
					if (icon2 != LqjxIconEnum.WILD) {
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
			if ((wi.getIcon() == icon || wi.getIcon() == LqjxIconEnum.WILD)
					&& (icon != LqjxIconEnum.WILD && icon != LqjxIconEnum.SCATTER )) {
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
	public List<List<WindowInfo>> winIcon(List<RewardInfo> winInfo , List<List<WindowInfo>> list) {
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);
		for (int i = 0; i <= 4; i++) {
			winList.add(new ArrayList<WindowInfo>(3) {
				private static final long serialVersionUID = 1L;
				{
					add(null);
					add(null);
					add(null);
				}
			});
		}
		addFree(list,winList);
		for (RewardInfo re : winInfo) {
			if(re.getWinIcon() != null){
				for (WindowInfo wi : re.getWinIcon()) {
					wi.setNum(99);
					winList.get(wi.getId()).set(wi.getIndex(), wi);
				}
			}
		}
		return winList;
	}
	
	
	/**
	 * 添加scatter图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addFree(List<List<WindowInfo>> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		for (List<WindowInfo> w : list) {
			for (WindowInfo wi : w) {
				if (wi.getIcon() == LqjxIconEnum.SCATTER) {
					list1.add(wi);
				}
			}
		}
		if (list1.size() > 2) {
			for (WindowInfo wi : list1) {
				list2.get(wi.getId()).set(wi.getIndex(), wi);
			}
		}
		return list2;
	}
	
	/**
	 * 添加wild图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addWild(List<WindowInfo> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		for (WindowInfo w : list) {
			if (w.getIcon() == LqjxIconEnum.WILD) {
				list1.add(w);
			}
		}
		if (list.size() != 0 && list1.size() > 2) {
			for (WindowInfo wi : list1) {
				list2.get(wi.getId()).set(wi.getIndex(), wi);
			}
		}
		return list2;
	}
	
	
	/**
	 * 检查免费游戏
	 * @param list
	 * @return
	 */
	public boolean chickFree(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == LqjxIconEnum.SCATTER) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 保存免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void save(LqjxScatterInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.LQJX_REDIS_SCATTER + roleid, 
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid, 
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
	}
	
	/**
	 * 获取免费游戏信息
	 * @param roleid
	 * @param uuid
	 * @return
	 */
	public LqjxScatterInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid);
		if (str != null) {
			LqjxScatterInfo bi = JSONObject.parseObject(str, LqjxScatterInfo.class);
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
	public void deleteInfo(String roleid, String uuid) {
		redisTemplate.delete(Constants.LQJX_REDIS_SCATTER + roleid + "|" + uuid);
	}
	
	public boolean chickList(List<WindowInfo> list, WindowInfo info) {
		for (WindowInfo w : list) {
			if (w.getIcon() == info.getIcon()
					|| (w.getIcon() == LqjxIconEnum.WILD && info.getIcon() != LqjxIconEnum.SCATTER)) {
				return true;
			}
		}
		return false;
	}

	// 查找数组中是否含有此列
	public static boolean useArraysBinarySearch(int[] arr, int targetValue) {
		if (arr.length > 1) {
			int a = Arrays.binarySearch(arr, targetValue);
			if (a >= 0)
				return true;
		} else {
			return arr[0] == targetValue;
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
