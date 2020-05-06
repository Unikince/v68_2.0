/**
 * 
 */
package com.zyhy.lhj_server.service.zctz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.zctz.BaseLogic;
import com.zyhy.lhj_server.game.zctz.ZctzIconEnum;
import com.zyhy.lhj_server.game.zctz.ZctzRollerWeightEnum;
import com.zyhy.lhj_server.game.zctz.ZctzWinLineEnum;
import com.zyhy.lhj_server.game.zctz.poi.impl.ZctzTemplateService;
import com.zyhy.lhj_server.game.zctz.poi.template.ZctzOdds;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGameBetResult;
import com.zyhy.lhj_server.prcess.result.zctz.ZctzGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class ZctzGameService extends BaseLogic{
	@Autowired
	private ZctzTemplateService templateService;
	/**
	 * 
	 * @param iconId 
	 * @param jetton 档位
	 * @param betnum 下注的线路数
	 * @return
	 */
	public ZctzGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		ZctzGameBetResult res = getResult(betinfo,iconId);
		return res;
	}

	private ZctzGameBetResult getResult(BetInfo betinfo, int iconId) {
		// 获取图标
		List<ZctzOdds> baseinfos = templateService.getList(ZctzOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<ZctzOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			ZctzOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		//窗口信息
		ZctzGameBetResult res = new ZctzGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for(int i=1; i<=5; i++){
			ws.addAll(ZctzRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		
		
		/*if (oddsLv > 0) {
			oddsRewardIcon(oddsLv,betinfo.getNum(),ws);
		}
		System.out.println("最最最最后的ws" + ws);*/
		
		
		//获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		//计算总奖励
		if(wins != null && wins.size() > 0){
			//总的奖励数
			double reward = 0;
			for(WinLineInfo w : wins){
				reward += w.getReward() ;
			}
			res.setRewardcoin(reward);
		}
		res.setWinrouteinfos(wins);
		return res;
	}
	/**
	 * 检查scatter奖励
	 */
	public List<WinLineInfo>  checkScatter(List<Window> ws , BetInfo betinfo){
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		List<Window> scatter = new ArrayList<>();
		for (Window w : ws) {
			if(w.getIcon().isScatter() ||w.getIcon().isWild()){
				scatter.add(w);
			}
		}
		
		if (scatter.size() > 0) {
			// 判断正面
			List<Window> list1 = new ArrayList<>();
			
			int id = scatter.get(0).getId();
			if(id == 1){
				list1.add(scatter.get(0));
				for (int i = 1; i < scatter.size(); i++) {
					if(scatter.get(i).getId() == list1.get(list1.size() -1).getId()){
						if(scatter.get(i).getIcon() != list1.get(list1.size() -1).getIcon()){
							if(!list1.get(list1.size() -1).getIcon().isScatter()){
								list1.set(list1.size() -1, scatter.get(i));
							}
						} 
						continue;
					}
					if((scatter.get(i).getId() - 1) == id ){
						list1.add(scatter.get(i));
						id = scatter.get(i).getId();
					}
				}
			}
			if(list1.size() > 1){
				WinLineInfo wl = new WinLineInfo();
				wl.setIcon(list1.get(0).getIcon());
				wl.setId("Scatter");
				wl.setNum(list1.size());
				wl.setOrder(1);
				wl.setReward(NumberTool.multiply(ZctzIconEnum.SCATTER.getTrs()[list1.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list1);
				res.add(wl);
			}
			
			if (list1.size() == 5) {
				return res;
			}
			
			// 判断反面
			List<Window> list2 = new ArrayList<>();
			List<Window> scatter1 = new ArrayList<>(scatter);
			Collections.reverse(scatter1);
			int id1 = scatter1.get(0).getId();
			if(id1 == 5){
				list2.add(scatter1.get(0));
				for (int i = 1; i < scatter1.size(); i++) {
					if(scatter1.get(i).getId() == list2.get(list2.size() -1).getId() ){
						if(scatter1.get(i).getIcon() != list2.get(list2.size() -1).getIcon()){
							if(!list2.get(list2.size() -1).getIcon().isScatter()){
								list2.set(list2.size() -1, scatter1.get(i));
							}
						} 
						continue;
					}
					
					if((scatter1.get(i).getId() + 1) == id1 ){
						list2.add(scatter1.get(i));
						id1 = scatter1.get(i).getId();
					}
				}
			}
			if(list2.size() > 1){
				WinLineInfo wl = new WinLineInfo();
				wl.setIcon(list2.get(0).getIcon());
				wl.setId("Scatter");
				wl.setNum(list2.size());
				wl.setOrder(2);
				wl.setReward(NumberTool.multiply(ZctzIconEnum.SCATTER.getTrs()[list2.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list2);
				res.add(wl);
			}
		}
		return res;
	}
	/**
	 * 得到获胜线路信息
	 * @param ws
	 * @param betnum 线路数
	 * @return
	 */
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		//获取所有线路
		Line[] ts = ArrayUtils.subarray(ZctzWinLineEnum.values(), 0, betinfo.getNum());
		for(Line line : ts){
			List<Window> lws = getLineWindows(ws, line);
			//正面
			WinLineInfo wl = getWinLineInfo(lws);
			if(wl != null && !wl.getIcon().isScatter()){
				wl.setOrder(1);
				wl.setWindows(new ArrayList<Window>(lws.subList(0, wl.getNum())));
				wl.setId(String.valueOf(line.getId()));
				reward(wl, betinfo);
				res.add(wl);
			}
			//5个连续时，不再获得反面奖励
			if(wl != null && wl.getNum() == 5){
				continue;
			}
			//反面
			List<Window> lwsreverse = new ArrayList<Window>(lws);
			Collections.reverse(lwsreverse);
			WinLineInfo wl2 = getWinLineInfo(lwsreverse);
			if(wl2 != null && !wl2.getIcon().isScatter()){
				wl2.setOrder(2);
				ArrayList<Window> arrayList = new ArrayList<Window>(lwsreverse.subList(0, wl2.getNum()));
				Collections.reverse(arrayList);
				wl2.setWindows(arrayList);
				wl2.setId(String.valueOf(line.getId()));
				reward(wl2, betinfo);
				res.add(wl2);
			}
		}
		// 单独计算scatter
		List<WinLineInfo> checkScatter = checkScatter(ws , betinfo);
		if(checkScatter.size() > 0){
			res.addAll(checkScatter);
		}
		return res;
	}
	
	public void reward(WinLineInfo wl, BetInfo betinfo) {
		if(wl != null){
			rewardWail(wl);
			wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getGold()).doubleValue());
		}
	}
	
	/**
	 * 计算wild奖励
	 */
	public void rewardWail(WinLineInfo wl) {
		if(wl.validWailNum() > 0){
			wl.setReward(wl.getReward()*2);
		}
	}
	
	
	
	
	/**
	 * 奖池奖励图标
	 *//*
	public void oddsRewardIcon(int oddsLv, int betnum, List<Window> ws){
		System.out.println("中奖的倍率" + oddsLv);
		Icon icon = null;
		int line = 0;
		int num = 0;
		int before = oddsLv - 100;
		int after = oddsLv + 5;
		for (ZctzIconEnum e : ZctzIconEnum.values()) {
			if (e.isScatter() || e.isWild()) {
				continue;
			}
			int index = 0;
			for (double lv : e.getTrs()) {
				index ++;
				int temp1 = 1;
				int temp2 = 2;
				if ((lv*temp1) > before && (lv*temp1) < after) {
					icon = e;
					num = index;
					line = temp1;
					System.out.println("匹配出来的倍率1:" + lv*temp1);
					break;
				}
				if ((lv*temp2) > before && (lv*temp2) < after) {
					icon = e;
					num = index;
					line = temp2;
					System.out.println("匹配出来的倍率2:" + lv*temp2);
					break;
				}
			}
			if (icon != null) {
				break;
			}
		}
		System.out.println("匹配上的图标:" + icon);
		System.out.println("需要几条获胜线路:" + line);
		System.out.println("需要几个中奖图标:" + num);
		Line[] ts = ArrayUtils.subarray(ZctzWinLineEnum.values(), 0, betnum);
		List<Integer> randoms = new ArrayList<>();
		if (icon != null) {
			List<Integer> temp = new ArrayList<>();
			for (int i = 0; i < ts.length; i++) {
				temp.add(i);
			}
			for (int i = 0; i < line; i++) {
				randoms.add(temp.get(RandomUtil.getRandom(0, temp.size()-1)));
			}
		}
		
		for (Integer random : randoms) {
			System.out.println("开始封装线路图标:" + (random+1));
			List<Window> lws = getLineWindows(ws, ts[random]);
			System.out.println("取出来的线路图标:" + lws);
			for (int i = 0; i < num; i++) {
				WindowInfo wl = (WindowInfo) lws.get(i);
				wl.setIcon(icon);
			}
			System.out.println("更换后的线路图标:" + lws);
		}
	}*/
	
	/**
	 * 转换为双list格式
	 */
	public List<List<WindowInfo>> formatDouble(List<Window> ws){
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
	 * 用于后台显示的胜利信息
	 */
	public void winInfosForBgView(ZctzGameBetResult result,ShowRecordResult srr){
		List<String> windows = new ArrayList<>(); // 窗口信息
		Map<String, Double> winLine = new HashMap<>(); // 获胜线路信息
		List<Window> windowinfos = result.getWindowinfos();
		List<List<WindowInfo>> formatDouble = formatDouble(windowinfos);
		for (int i = 0; i < 3; i++) {
			for (List<WindowInfo> list : formatDouble) {
				windows.add(list.get(i).getIcon().getName());
			}
		}
		
		List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
		for (WinLineInfo wl : winrouteinfos) {
			winLine.put(wl.getId(), wl.getReward());
		}
		srr.setWindows(windows);
		srr.setWinLine(winLine);
		srr.setViewType(15);
	}
	
	
	
	
	
	
	
	
	
	
	
}
