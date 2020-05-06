/**
 * 
 */
package com.zyhy.lhj_server.service.yzhx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.StringUtils;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.yzhx.YzhxFreeInfo;
import com.zyhy.lhj_server.game.yzhx.YzhxIconEnum;
import com.zyhy.lhj_server.game.yzhx.YzhxRollerWeightEnum;
import com.zyhy.lhj_server.game.yzhx.YzhxWinLineEnum;
import com.zyhy.lhj_server.game.yzhx.poi.impl.YzxhTemplateService;
import com.zyhy.lhj_server.game.yzhx.poi.template.YzhxOdds;
import com.zyhy.lhj_server.prcess.result.yzhx.YzhxGameBetResult;

@Service
public class YzhxGameService extends BaseLogic {
	@Autowired
	private YzxhTemplateService templateService;
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * @param iconId 
	 * @param freeGame
	 * @param rep
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public YzhxGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		YzhxGameBetResult res = getResult(betinfo, roleid,iconId);
		return res;
	}

	private YzhxGameBetResult getResult(BetInfo betinfo, String roleid, int iconId) {
		// 获取图标
		List<YzhxOdds> baseinfos = templateService.getList(YzhxOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<YzhxOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			YzhxOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		YzhxGameBetResult res = new YzhxGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(YzhxRollerWeightEnum.windowInfo(i,baseinfos));
		}
		//System.out.println("取出来的图标:" + ws);
		// 检验wild出现的位置以及类型
		List<Window> checkWild = checkWild(ws ,baseinfos);
		List<Integer> wildType = wildType(checkWild);
		res.setWindowinfos(checkWild);
		res.setWildType(wildType);

		// 获胜线路信息
		List<WinLineInfo> wins = getWininfo(checkWild, betinfo);
		res.setWinrouteinfos(wins);

		// 检测是否中了免费游戏
		int ckickScatter = CkickScatter(ws);
		if (ckickScatter > 2) {
			// 免费次数
			int[] scatterNum = { 0, 0, 15, 25, 50 };
			res.setFree(true);
			res.setFreeNum(scatterNum[ckickScatter - 1]);
			// scatter奖励
			int[] scatterReward = { 0, 0, 2, 10, 20 };
			WinLineInfo scatter = new WinLineInfo();
			scatter.setOrder(1);
			scatter.setIcon(YzhxIconEnum.SCATTER);
			scatter.setNum(ckickScatter);
			scatter.setId("scatter");
			scatter.setReward(NumberTool.multiply(NumberTool.multiply(betinfo.getGold(), betinfo.getNum()),
					scatterReward[ckickScatter - 1]).doubleValue());
			wins.add(scatter);
		}
		

		// 计算奖励
		BigDecimal reward = new BigDecimal(0);
		if (wins != null && wins.size() > 0) {
			// 总的奖励数
			for (WinLineInfo w : wins) {
				reward = reward.add(new BigDecimal(w.getReward() + ""));
			}
		}
		res.setRewardcoin(reward.doubleValue());
		return res;
	}

	/**
	 * 检验wild位置
	 * 
	 * @param ws
	 * @return
	 */
	protected List<Window> checkWild(List<Window> ws,List<YzhxOdds> baseinfos ) {
		List<List<WindowInfo>> format = formatDouble(ws);

		for (List<WindowInfo> list : format) {
			Icon icon1 = null;
			Icon icon2 = null;
			Icon icon3 = null;
			for (WindowInfo wi : list) {
				if (wi.getIndex() == 1) {
					icon1 = wi.getIcon();
				}
				if (wi.getIndex() == 2) {
					icon2 = wi.getIcon();
				}
				if (wi.getIndex() == 3) {
					icon3 = wi.getIcon();
				}
			}
			if (icon2.isWild() && !icon1.isWild() && !icon3.isWild()) {
				list.set(1, YzhxRollerWeightEnum.windowInfo(list.get(1).getId(), 2, baseinfos));
			}
			if (!icon2.isWild() && icon1.isWild() && icon3.isWild()) {
				list.set(0, YzhxRollerWeightEnum.windowInfo(list.get(1).getId(), 1, baseinfos));
				list.set(2, YzhxRollerWeightEnum.windowInfo(list.get(1).getId(), 3, baseinfos));
			}
		}
		List<Window> formatSingle = formatSingle(format);
		wildType(formatSingle);
		return formatSingle;
	}

	/**
	 * wild组合类型
	 * 
	 * @return
	 */
	protected List<Integer> wildType(List<Window> checkWild) {
		List<List<WindowInfo>> format = formatDouble(checkWild);
		List<Integer> wildType = new ArrayList<Integer>(5) {
			private static final long serialVersionUID = 1L;

			{
				add(null);
				add(null);
				add(null);
				add(null);
				add(null);
			}
		};
		for (List<WindowInfo> list : format) {
			Icon icon1 = list.get(0).getIcon();
			Icon icon2 = list.get(1).getIcon();
			Icon icon3 = list.get(2).getIcon();
			if (icon1.isWild() && !icon2.isWild() && !icon3.isWild()) {
				wildType.set((list.get(0).getId() - 1), 1);
			}
			if (icon1.isWild() && icon2.isWild() && !icon3.isWild()) {
				wildType.set((list.get(0).getId() - 1), 2);
			}
			if (icon1.isWild() && icon2.isWild() && icon3.isWild()) {
				wildType.set((list.get(0).getId() - 1), 3);
			}
			if (!icon1.isWild() && icon2.isWild() && icon3.isWild()) {
				wildType.set((list.get(0).getId() - 1), 4);
			}
			if (!icon1.isWild() && !icon2.isWild() && icon3.isWild()) {
				wildType.set((list.get(0).getId() - 1), 5);
			}
		}
		return wildType;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param betnum
	 *            线路数
	 * @return
	 */
	public List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		// 获取所有线路
		Line[] ts = ArrayUtils.subarray(YzhxWinLineEnum.values(), 0, betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);

			// 正面
			WinLineInfo wl = getWinLineInfo(lws);
			if (wl != null && wl.getIcon() != YzhxIconEnum.SCATTER) {
				wl.setOrder(1);
				wl.setWindows(lws);
				wl.setId(String.valueOf(line.getId()));
				// reward(wl, betinfo);
				wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getGold()).doubleValue());
				res.add(wl);
			}
		}
		return res;
	}

	/**
	 * 重新取图标
	 * 
	 * @param ws
	 */
	protected List<Window> refetchIcon(List<Window> ws) {
		List<List<WindowInfo>> format = formatDouble(ws);
		// 重取图标
		for (List<WindowInfo> list : format) {
			for (WindowInfo wi : list) {
				if (wi.getIcon().isWild()) {
					for (WindowInfo wi1 : list) {
						wi1.setIcon(YzhxIconEnum.WILD1);
					}
				}
			}
		}

		List<Window> formatSingle = formatSingle(format);
		return formatSingle;
	}

	/**
	 * 判断是否有免费游戏
	 * 
	 * @param ws
	 * @return
	 */
	public int CkickScatter(List<Window> ws) {
		Set<Integer> set = new HashSet<Integer>();
		for (Window w : ws) {
			if (w.getIcon() == YzhxIconEnum.SCATTER) {
				set.add(w.getId());
			}
		}
		return set.size();
	}

	/**
	 * 保存免费信息
	 * 
	 * @param free
	 */
	public void saveFreeInfo(String roleid, YzhxFreeInfo free, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid, 
				JSONObject.toJSONString(free));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid, 
						JSONObject.toJSONString(free),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid, 
						JSONObject.toJSONString(free),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid, 
						JSONObject.toJSONString(free),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid, 
						JSONObject.toJSONString(free),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
	}

	/**
	 * 获取免费信息
	 */
	public YzhxFreeInfo getFreeInfo(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			YzhxFreeInfo free = JSONObject.parseObject(str, YzhxFreeInfo.class);
			return free;
		}
		return null;
	}

	/**
	 * 删除免费信息
	 */
	public void delFreeInfo(String roleid, String uuid) {
		redisTemplate.delete(Constants.YZHX_REDIS_LHJ_YZHX_FREE + roleid + "|" + uuid);
	}

	/**
	 * 转换为双list格式
	 */
	public List<List<WindowInfo>> formatDouble(List<Window> ws) {
		List<List<WindowInfo>> allList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			List<WindowInfo> list = new ArrayList<>();
			for (Window w : ws) {
				WindowInfo wi1 = (WindowInfo) w;
				if (wi1.getId() == i) {
					list.add(wi1);
				}
			}
			allList.add(list);
		}
		return allList;
	}

	/**
	 * 转换为单list格式
	 */
	public List<Window> formatSingle(List<List<WindowInfo>> wi) {
		List<Window> formatSingle = new ArrayList<>();
		for (List<WindowInfo> list : wi) {
			for (WindowInfo windowInfo : list) {
				formatSingle.add(windowInfo);
			}
		}
		return formatSingle;
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
