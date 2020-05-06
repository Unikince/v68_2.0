/**
 * 
 */
package com.zyhy.lhj_server.service.alsj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.alsj.AlsjIconEnum;
import com.zyhy.lhj_server.game.alsj.AlsjRollerWeightEnum;
import com.zyhy.lhj_server.game.alsj.AlsjScatterInfo;
import com.zyhy.lhj_server.game.alsj.AlsjWinIndex;
import com.zyhy.lhj_server.game.alsj.poi.impl.AlsjTemplateService;
import com.zyhy.lhj_server.game.alsj.poi.template.AlsjOdds;
import com.zyhy.lhj_server.prcess.result.alsj.AlsjGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class AlsjGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AlsjTemplateService templateService;
	private boolean isScatter;

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
	public AlsjGameBetResult doGameProcess(String roleid, BetInfo betinfo,
			boolean isfree, int iconId) throws ClassNotFoundException, IOException {

		AlsjGameBetResult res = new AlsjGameBetResult();
		// 获取图标
		List<AlsjOdds> baseinfos = templateService.getList(AlsjOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<AlsjOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			AlsjOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<List<WindowInfo>> list = new ArrayList<List<WindowInfo>>(5);

		// 不是免费游戏的时候2-4列随机全是wild
		//int ran = RandomUtil.getRandom(2, 3);
		//boolean isWild = false;
		isScatter = false;
		for (int i = 0; i <= 4; i++) {
			list.add(new ArrayList<WindowInfo>(3));
			for (int j = 0; j <= 2; j++) {
				list.get(i).add(j, AlsjRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
			}
		}
		
		/*if (!isfree) {
			// 全是wild的概率
			int random = RandomUtil.getRandom(0, 100);
			if (random >= 0) {
				isWild = true;
			}
			if (isWild) {
				list.remove(ran);
				list.add(ran, new ArrayList<WindowInfo>(3));
				for (int v = 0; v < 3; v++) {
					list.get(ran).add(v,
							new WindowInfo(ran, v, AlsjIconEnum.WILD));
				}
			}
		}
		 */		
		List<List<WindowInfo>> deepCopy = deepCopy(list);
		res.setWindowinfos(deepCopy);
		// 获胜线路信息
		AlsjWinIndex wins = getWininfo(list, betinfo,isfree,res);
		int scatter = 0;
		for (int i = 0; i < 5; i++) {
			if (chickFree(list.get(i))) {
				scatter++;
			}
		}
		
		List<WindowInfo> temp = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (WindowInfo wl : list.get(i)) {
				temp.add(wl);
			}
		}
		WinLineInfo chickRed = chickRed(temp,betinfo);
		//WinLineInfo chickWild = chickWild(temp,betinfo);
		if(scatter > 2){
			double scatterReward = 0;
			if (isfree) {
				 scatterReward = NumberTool.multiply(AlsjIconEnum.SCATTER.getTrs()[scatter - 1], betinfo.getTotalBet()).doubleValue();
			} else {
				 scatterReward = NumberTool.multiply(AlsjIconEnum.SCATTER.getTrs()[scatter - 1], betinfo.getTotalBet()*2).doubleValue();
			}
			wins.setReward(NumberTool.add(wins.getReward(), scatterReward).doubleValue());
			res.setScattericon(scatterReward);
			res.setScattericonnum(scatter);
		}
		
		if (chickRed != null) {
			if(chickRed.getNum() > 2){
				double bonusReward = 0;
				if (isfree) {
					bonusReward = chickRed.getReward()*2;
				} else {
					bonusReward = chickRed.getReward();
				}
				wins.setReward(NumberTool.add(wins.getReward(), bonusReward).doubleValue());
				res.setBonusicon(bonusReward);
				res.setBonusiconnum(chickRed.getNum());
			}
		}
		
		/*if (chickWild != null) {
			if(chickWild.getNum() > 2){
				double wildReward = 0;
				if (isfree) {
					wildReward = chickWild.getReward()*2;
				} else {
					wildReward = chickWild.getReward();
				}
				wins.setReward(NumberTool.add(wins.getReward(), wildReward).doubleValue());
				res.setWildicon(wildReward);
				res.setWildiconnum(chickWild.getNum());
			}
		}*/
		
		res.setRewardcoin(wins.getReward());

		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转
		if (wins.getNum() > 0) {
			res.setScatter(true);
			res.setScatterNum(wins.getNum());
		}

		/*if (isWild) {
			wins.setAllWild(true);
			wins.getWild().add(ran);
			res.setIsWild(ran);
		}*/

		if (wins.isRedGame()) {
			res.setBonus(true);
		}
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param isfree 
	 * @param res 
	 * @param betnum
	 *            线路数
	 * @return
	 */
	private AlsjWinIndex getWininfo(List<List<WindowInfo>> ws, BetInfo betinfo, boolean isfree, AlsjGameBetResult res) {
		AlsjWinIndex win = new AlsjWinIndex();
		
		// 总奖励
		//double all = 0;

		// 奖励免费次数
		int number = 0;
		int winNum = 0;
		int[] scatterNum = { 0, 0, 15, 15, 15 };
		
		// 中奖的图标
		//List<WindowInfo> winlist = new ArrayList<WindowInfo>();

		// 检查是否有免费游戏
		for (int i = 0; i < 5; i++) {
			if (chickFree(ws.get(i))) {
				winNum++;
			}
		}
		
		List<WindowInfo> temp = new ArrayList<>();
		for (int i = 0; i < ws.size(); i++) {
			for (WindowInfo wl : ws.get(i)) {
				temp.add(wl);
			}
		}
		WinLineInfo chickRed = chickRed(temp);
		WinLineInfo chickWild = chickWild2(temp);
		int wild = 0;
		int bonus = 0;
		if (chickRed != null) {
			bonus = chickRed.getNum();
		}
		if (chickWild != null) {
			wild = chickWild.getNum();
		}
		
		
		
		if (winNum > 2) {
			isScatter = true;
			number = scatterNum[winNum - 1];
		}

		// 检查是否触发红利游戏
		if (!isScatter && !isfree && chickRed2(ws.get(0)) && chickRed2(ws.get(4))) {
			win.setRedGame(true);
		}
		
		
		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = winNum(ws);
		Iterator<RewardInfo> iterator = winInfo.iterator();
		while (iterator.hasNext()) {
			RewardInfo next = iterator.next();
			if (next.getWinNum() > 0) {
				double reward = 0;
				if (isfree) {
					reward = NumberTool.multiply(NumberTool.multiply(betinfo.total(), 
							next.getWinIcon().get(0).getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()*2).doubleValue();
				} else {
					reward = NumberTool.multiply(NumberTool.multiply(betinfo.total(), 
							next.getWinIcon().get(0).getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
				}
				totalReward += reward;
			} else {
				iterator.remove();
			}
		}
		res.setRewardInfo(winInfo);
		win.setReward(totalReward);
		win.setRewardType(2);
		win.setNum(number);
		
		// 中奖位置
		/*List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);

		if (totalReward > 0 || number > 0) {
			int i, j;
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
									|| ws.get(i).get(j).getIcon() == AlsjIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						} else {
							if ((chickList(winlist, ws.get(i).get(j)) && chickList(
									ws.get(i - 1), ws.get(i).get(j)))
									|| ws.get(i).get(j).getIcon() == AlsjIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						}
					}
				}
			}
		}*/
		
		// 中奖的图标位置
		List<List<WindowInfo>> winList = new ArrayList<>();
		if (totalReward > 0 || number > 2 || wild > 2 || bonus > 2 ) {
			winList = winIcon(winInfo ,ws);
		}
		win.setWindows(winList);
		return win;
	}

	@Override
	public void rewardWail(WinLineInfo wl, BetInfo betinfo) {
		return;
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
				chickWild(icon);
				ari.setWinIcon(icon);
				for (WindowInfo wl : icon) {
					Icon icon2 = wl.getIcon();
					if (icon2 != AlsjIconEnum.WILD) {
						ari.setIcon(icon2);
						break;
					} 
					ari.setIcon(icon.get(0).getIcon());
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
			if ((wi.getIcon() == icon || wi.getIcon() == AlsjIconEnum.WILD)
					&& (icon != AlsjIconEnum.SCATTER && icon != AlsjIconEnum.BONUS)) {
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
		addRed(list,winList);
		//addWild(list,winList);
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
			if (w.getIcon() == AlsjIconEnum.SCATTER) {
				return true;
			}
		}
		return false;
	}
	
	public WinLineInfo chickRed(List<WindowInfo> ws, BetInfo betinfo) {
		WinLineInfo res = new  WinLineInfo();
		List<Window> bonus = new ArrayList<>();
		for (Window w : ws) {
			if(w.getIcon().IsBonus()){
				bonus.add(w);
			}
		}
		if (bonus.size() > 0) {
			// 判断正面
			List<Window> list1 = new ArrayList<>();
			int id = bonus.get(0).getId();
			if(id == 1){
				list1.add(bonus.get(0));
				for (int i = 1; i < bonus.size(); i++) {
					if(bonus.get(i).getId() == id ){
						continue;
					}
					if((bonus.get(i).getId() - 1) == id ){
						list1.add(bonus.get(i));
						id = bonus.get(i).getId();
					}
				}
			}
			
			if(list1.size() > 2){
				res.setIcon(list1.get(0).getIcon());
				res.setId("bonus");
				res.setNum(list1.size());
				res.setOrder(1);
				res.setReward(NumberTool.multiply(AlsjIconEnum.BONUS.getTrs()[list1.size() - 1], betinfo.total()).doubleValue());
				res.setWindows(list1);
				return res;
			}
		}
		return null;
	}
	
	public WinLineInfo chickRed(List<WindowInfo> ws) {
		WinLineInfo res = new  WinLineInfo();
		List<Window> bonus = new ArrayList<>();
		for (Window w : ws) {
			if(w.getIcon().IsBonus()){
				bonus.add(w);
			}
		}
		if (bonus.size() > 0) {
			// 判断正面
			List<Window> list1 = new ArrayList<>();
			int id = bonus.get(0).getId();
			if(id == 1){
				list1.add(bonus.get(0));
				for (int i = 1; i < bonus.size(); i++) {
					if(bonus.get(i).getId() == id ){
						continue;
					}
					if((bonus.get(i).getId() - 1) == id ){
						list1.add(bonus.get(i));
						id = bonus.get(i).getId();
					}
				}
			}
			if(list1.size() > 2){
				res.setIcon(list1.get(0).getIcon());
				res.setId("bonus");
				res.setNum(list1.size());
				res.setOrder(1);
				res.setReward(AlsjIconEnum.BONUS.getTrs()[list1.size() - 1]);
				res.setWindows(list1);
				return res;
			}
		}
		return null;
	}
	
	
	public boolean chickRed2(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == AlsjIconEnum.BONUS) {
				return true;
			}
		}
		return false;
	}
	
	public void chickWild(List<WindowInfo> w) {
		List<WindowInfo> ws = new ArrayList<>();
		for (WindowInfo windowInfo : w) {
			if (ws.size() > 0) {
				WindowInfo windowInfo2 = ws.get(ws.size() -1);
				if (windowInfo.getId() == windowInfo2.getId()) {
					continue;
				}
			}
			ws.add(windowInfo);
		}
		
		int wild = 0;
		for (WindowInfo wi : ws) {
			if (!wi.getIcon().isWild()) {
				break;
			}
			wild ++;
		}
		
		if (wild == 3 && ws.size() > 3) {
			//System.out.println("传进来的ws=========================" + w);
			double wildlv = AlsjIconEnum.WILD.getTrs()[2];
			double iconlv = 0;
			if (ws.size() == 4) {
				iconlv = ws.get(ws.size()-1).getIcon().getTrs()[3];
				if (wildlv > iconlv) {
					ws.remove(3);
					w.clear();
					w.addAll(ws);
					//System.out.println("传出去的ws=========================" + w);
				}
			}
			if (ws.size() == 5) {
				iconlv = ws.get(ws.size() -1).getIcon().getTrs()[4];
				if (wildlv > iconlv) {
					ws.remove(3);
					ws.remove(4);
					w.clear();
					w.addAll(ws);
					//System.out.println("传出去的ws=========================" + w);
				}
			}
		} 
		if (wild == 4 && ws.size() > 4) {
			//System.out.println("传进来的ws=========================" + w);
			double wildlv = AlsjIconEnum.WILD.getTrs()[3];
			double iconlv = ws.get(4).getIcon().getTrs()[3];
			if (wildlv > iconlv) {
				ws.remove(4);
				w.clear();
				w.addAll(ws);
				//System.out.println("传出去的ws=========================" + w);
			}
		}
	}
	
	public WinLineInfo chickWild2(List<WindowInfo> ws) {
		WinLineInfo res = new WinLineInfo();
		List<Window> wild = new ArrayList<>();
		for (Window w : ws) {
			if(w.getIcon().isWild()){
				wild.add(w);
			}
		}
		if (wild.size() > 0) {
			// 判断正面
			List<Window> list1 = new ArrayList<>();
			int id = wild.get(0).getId();
			if(id == 1){
				list1.add(wild.get(0));
				for (int i = 1; i < wild.size(); i++) {
					if(wild.get(i).getId() == id ){
						continue;
					}
					if((wild.get(i).getId() - 1) == id ){
						list1.add(wild.get(i));
						id = wild.get(i).getId();
					}
				}
			}
			
			if(list1.size() > 2){
				res.setIcon(list1.get(0).getIcon());
				res.setId("wild");
				res.setNum(list1.size());
				res.setOrder(1);
				res.setReward(AlsjIconEnum.WILD.getTrs()[list1.size() - 1]);
				res.setWindows(list1);
				return res;
			}
		}
		return null;
	}
	
	/**
	 * 保存免费游戏次数
	 * 
	 * @param AlsjScatterInfo
	 * @param roleid
	 */
	public void save(AlsjScatterInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.ALSJREDIS_SCATTER + roleid,
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
	}
	
	/**
	 * 删除免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void deleteInfo(String roleid, String uuid) {
		redisTemplate.delete(Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid);
	}

	/**
	 * 获得免费游戏次数
	 * 
	 * @param roleid
	 * @return AlsjScatterInfo
	 */
	public AlsjScatterInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.ALSJREDIS_SCATTER + roleid + "|" + uuid);
		if (str != null) {
			AlsjScatterInfo bi = JSONObject.parseObject(str, AlsjScatterInfo.class);
			return bi;
		}
		return null;
	}

	public boolean chickList(List<WindowInfo> list, WindowInfo info) {
		for (WindowInfo w : list) {
			if (w.getIcon() == info.getIcon()
					|| (w.getIcon() == AlsjIconEnum.WILD && info.getIcon() != AlsjIconEnum.SCATTER)) {
				return true;
			}
		}
		return false;
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
				if (wi.getIcon() == AlsjIconEnum.SCATTER) {
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
	 * 添加bonus图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addRed(List<List<WindowInfo>> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		List<WindowInfo> temp = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (WindowInfo wl : list.get(i)) {
				temp.add(wl);
			}
		}
		WinLineInfo chickRed = chickRed(temp);
		if (chickRed != null) {
			System.out.println("chickRed" + chickRed);
			if (chickRed.getWindows().size() > 0) {
				for (Window windowInfo : chickRed.getWindows()) {
					list1.add((WindowInfo) windowInfo);
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
	 * 添加bonus图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addWild(List<List<WindowInfo>> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		List<WindowInfo> temp = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (WindowInfo wl : list.get(i)) {
				temp.add(wl);
			}
		}
		WinLineInfo chickWild = chickWild2(temp);
		if (chickWild != null) {
			if (chickWild.getWindows().size() > 0) {
				for (Window windowInfo : chickWild.getWindows()) {
					list1.add((WindowInfo) windowInfo);
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
